package com.example.data.engine

import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

enum class MapNodeState {
  /** Linked to an explored rule but not opened yet. */
  GHOST,
  EXPLORED,
  /** Explored and practised with at least [GrammarMapBuilder.MASTERY_RATIO] of right answers. */
  MASTERED
}

enum class MapEdgeKind {
  /** Explicit "see also" link from [RuleGraph]. */
  CROSS_REF,
  /** Rules of the same family ([RuleGraph.family]). */
  FAMILY,
  /** Rules learned as a contrastive pair ([StructuredRule.contrastGroupId]). */
  CONTRAST
}

data class MapNode(
  val ruleId: String,
  val state: MapNodeState,
  val family: String,
  val isFocused: Boolean
)

data class MapEdge(
  val from: String,
  val to: String,
  val kind: MapEdgeKind,
  /** True when one end is a ghost: drawn fainter. */
  val toGhost: Boolean
)

data class MapGraph(val nodes: List<MapNode>, val edges: List<MapEdge>) {
  val exploredCount: Int get() = nodes.count { it.state != MapNodeState.GHOST }
  val ghostCount: Int get() = nodes.count { it.state == MapNodeState.GHOST }
}

/**
 * Builds the learner's grammar map, wikilinks-style: every explored rule is a node, and the rules
 * they link to appear as "ghost" nodes waiting to be opened.
 *
 * - Cross-references and contrast pairs of every explored rule are shown as ghosts.
 * - Same-family siblings are shown only around the focused rule (capped), to keep the map readable.
 * - Explored rules of one family are chained in curriculum order instead of all being linked to each
 *   other, which would turn large families into a hairball.
 */
object GrammarMapBuilder {

  const val MASTERY_RATIO = 0.7f
  const val MAX_FAMILY_GHOSTS = 6

  fun build(
    rules: List<StructuredRule>,
    explored: Set<String>,
    mastered: Set<String>,
    focusedId: String?
  ): MapGraph {
    val byId = rules.associateBy { it.id }
    val order = rules.withIndex().associate { it.value.id to it.index }
    val exploredRules = rules.filter { it.id in explored }
    val visible = LinkedHashMap<String, MapNodeState>()
    exploredRules.forEach { visible[it.id] = if (it.id in mastered) MapNodeState.MASTERED else MapNodeState.EXPLORED }

    val edges = LinkedHashMap<Triple<String, String, MapEdgeKind>, MapEdge>()
    fun addEdge(a: String, b: String, kind: MapEdgeKind) {
      if (a == b) return
      val (x, y) = if (a < b) a to b else b to a
      val key = Triple(x, y, kind)
      if (key !in edges) edges[key] = MapEdge(x, y, kind, toGhost = false)
    }
    fun showGhost(id: String) {
      if (id in byId && id !in visible) visible[id] = MapNodeState.GHOST
    }

    exploredRules.forEach { rule ->
      RuleGraph.crossRefs(rule.id).forEach { other ->
        if (other in byId) {
          showGhost(other)
          addEdge(rule.id, other, MapEdgeKind.CROSS_REF)
        }
      }
      rule.contrastGroupId?.let { group ->
        rules.filter { it.id != rule.id && it.contrastGroupId == group }.forEach { partner ->
          showGhost(partner.id)
          addEdge(rule.id, partner.id, MapEdgeKind.CONTRAST)
        }
      }
    }

    // Chain explored rules of the same family in curriculum order.
    exploredRules.groupBy { RuleGraph.family(it.categoryId) }.values.forEach { family ->
      family.zipWithNext().forEach { (a, b) -> addEdge(a.id, b.id, MapEdgeKind.FAMILY) }
    }

    // Around the focused rule: its nearest unexplored siblings in the curriculum.
    val focused = focusedId?.let { byId[it] }?.takeIf { it.id in explored }
    if (focused != null) {
      val family = RuleGraph.family(focused.categoryId)
      val focusedOrder = order.getValue(focused.id)
      rules.asSequence()
        .filter { it.id != focused.id && it.id !in explored && RuleGraph.family(it.categoryId) == family }
        .sortedBy { kotlin.math.abs(order.getValue(it.id) - focusedOrder) }
        .take(MAX_FAMILY_GHOSTS)
        .forEach { sibling ->
          showGhost(sibling.id)
          addEdge(focused.id, sibling.id, MapEdgeKind.FAMILY)
        }
    }

    val nodes = visible.map { (id, state) ->
      MapNode(
        ruleId = id,
        state = state,
        family = RuleGraph.family(byId.getValue(id).categoryId),
        isFocused = id == focused?.id
      )
    }
    val finalEdges = edges.values
      .filter { it.from in visible && it.to in visible }
      .map { it.copy(toGhost = visible[it.from] == MapNodeState.GHOST || visible[it.to] == MapNodeState.GHOST) }
    return MapGraph(nodes, finalEdges)
  }
}

/**
 * Small force-directed layout (repulsion between all nodes, springs along edges, weak gravity).
 * Coordinates are in dp around (0, 0). Nodes with a known position start there with a low
 * temperature, so an existing map keeps its shape and only newcomers move noticeably.
 */
object ForceLayout {

  private const val EDGE_LENGTH = 110f
  private const val REPULSION = EDGE_LENGTH * EDGE_LENGTH
  private const val GRAVITY = 0.06f

  fun layout(
    nodeIds: List<String>,
    edges: List<Pair<String, String>>,
    known: Map<String, Pair<Float, Float>>,
    iterations: Int = 300
  ): Map<String, Pair<Float, Float>> {
    if (nodeIds.isEmpty()) return emptyMap()
    val index = nodeIds.withIndex().associate { it.value to it.index }
    val n = nodeIds.size
    val xs = FloatArray(n)
    val ys = FloatArray(n)
    val placed = BooleanArray(n)
    val neighbours = Array(n) { mutableListOf<Int>() }
    val links = edges.mapNotNull { (a, b) ->
      val i = index[a] ?: return@mapNotNull null
      val j = index[b] ?: return@mapNotNull null
      neighbours[i].add(j)
      neighbours[j].add(i)
      i to j
    }

    nodeIds.forEachIndexed { i, id ->
      known[id]?.let { (x, y) ->
        xs[i] = x
        ys[i] = y
        placed[i] = true
      }
    }
    val allKnown = placed.all { it }
    if (allKnown) return nodeIds.associateWith { known.getValue(it) }

    // Newcomers start next to an already placed neighbour, otherwise on a spiral around the centre.
    var spiral = known.size
    while (placed.any { !it }) {
      var placedThisPass = false
      for (i in 0 until n) {
        if (placed[i]) continue
        val anchor = neighbours[i].firstOrNull { placed[it] } ?: continue
        // Deterministic angle per rule, so the same map is laid out the same way every time.
        val angle = Math.floorMod(nodeIds[i].hashCode(), 360) * (Math.PI / 180.0)
        xs[i] = xs[anchor] + (EDGE_LENGTH * cos(angle)).toFloat()
        ys[i] = ys[anchor] + (EDGE_LENGTH * sin(angle)).toFloat()
        placed[i] = true
        placedThisPass = true
      }
      if (!placedThisPass) {
        // No placed neighbour anywhere: start a new island on a spiral around the centre.
        val i = placed.indexOfFirst { !it }
        val r = EDGE_LENGTH * (1 + spiral * 0.35f)
        val a = spiral * 2.4
        xs[i] = (r * cos(a)).toFloat()
        ys[i] = (r * sin(a)).toFloat()
        spiral++
        placed[i] = true
      }
    }

    // Fruchterman–Reingold forces. Repulsion is limited to nearby nodes so separate islands do not
    // push each other off screen; gravity keeps every island close to the centre.
    val dx = FloatArray(n)
    val dy = FloatArray(n)
    var temperature = if (known.isEmpty()) EDGE_LENGTH else EDGE_LENGTH * 0.35f
    val cooling = temperature / (iterations + 1)
    val repulsionRange2 = (EDGE_LENGTH * 3) * (EDGE_LENGTH * 3)
    repeat(iterations) {
      dx.fill(0f)
      dy.fill(0f)
      for (i in 0 until n) {
        for (j in i + 1 until n) {
          var ox = xs[i] - xs[j]
          var oy = ys[i] - ys[j]
          var d2 = ox * ox + oy * oy
          if (d2 > repulsionRange2) continue
          if (d2 < 0.01f) {
            ox = 0.1f * (i - j)
            oy = 0.1f
            d2 = ox * ox + oy * oy
          }
          val f = REPULSION / d2
          dx[i] += ox * f
          dy[i] += oy * f
          dx[j] -= ox * f
          dy[j] -= oy * f
        }
      }
      links.forEach { (i, j) ->
        val ox = xs[i] - xs[j]
        val oy = ys[i] - ys[j]
        val d = max(sqrt(ox * ox + oy * oy), 0.01f)
        val f = d / EDGE_LENGTH
        dx[i] -= ox * f
        dy[i] -= oy * f
        dx[j] += ox * f
        dy[j] += oy * f
      }
      for (i in 0 until n) {
        dx[i] -= xs[i] * GRAVITY
        dy[i] -= ys[i] * GRAVITY
        val len = max(sqrt(dx[i] * dx[i] + dy[i] * dy[i]), 0.01f)
        val step = min(len, temperature)
        xs[i] += dx[i] / len * step
        ys[i] += dy[i] / len * step
      }
      temperature = max(temperature - cooling, 1f)
    }
    return nodeIds.withIndex().associate { (i, id) -> id to (xs[i] to ys[i]) }
  }
}
