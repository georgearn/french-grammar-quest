package com.example

import com.example.data.engine.ForceLayout
import com.example.data.engine.GrammarMapBuilder
import com.example.data.engine.GrammarRuleRepository
import com.example.data.engine.MapEdgeKind
import com.example.data.engine.MapNodeState
import com.example.data.engine.RuleGraph
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

class GrammarMapTest {

  private val rules = GrammarRuleRepository.getAllRules().sortedBy { it.level }

  @Test
  fun `empty map has no nodes`() {
    val graph = GrammarMapBuilder.build(rules, emptySet(), emptySet(), null)
    assertTrue(graph.nodes.isEmpty())
  }

  @Test
  fun `cross references of an explored rule appear as ghosts`() {
    val (a, b) = RuleGraph.links.first()
    val graph = GrammarMapBuilder.build(rules, setOf(a), emptySet(), a)
    val ghost = graph.nodes.single { it.ruleId == b }
    assertEquals(MapNodeState.GHOST, ghost.state)
    assertTrue(graph.edges.any { it.kind == MapEdgeKind.CROSS_REF && setOf(it.from, it.to) == setOf(a, b) && it.toGhost })
  }

  @Test
  fun `family ghosts are capped around the focused rule only`() {
    val focus = rules.first().id
    val graph = GrammarMapBuilder.build(rules, setOf(focus), emptySet(), focus)
    val familyGhosts = graph.edges.count { it.kind == MapEdgeKind.FAMILY }
    assertTrue(familyGhosts <= GrammarMapBuilder.MAX_FAMILY_GHOSTS)
    assertTrue(graph.nodes.single { it.ruleId == focus }.isFocused)
  }

  @Test
  fun `mastered rules are flagged`() {
    val id = rules.first().id
    val graph = GrammarMapBuilder.build(rules, setOf(id), setOf(id), null)
    assertEquals(MapNodeState.MASTERED, graph.nodes.single { it.ruleId == id }.state)
  }

  @Test
  fun `layout keeps known positions and spreads new nodes`() {
    val ids = listOf("a", "b", "c", "d")
    val edges = listOf("a" to "b", "b" to "c")
    val first = ForceLayout.layout(ids, edges, emptyMap())
    assertEquals(ids.toSet(), first.keys)
    ids.forEach { i ->
      ids.filter { it > i }.forEach { j ->
        val (x1, y1) = first.getValue(i)
        val (x2, y2) = first.getValue(j)
        assertTrue("$i and $j overlap", hypot(x1 - x2, y1 - y2) > 20f)
      }
    }
    assertEquals(first, ForceLayout.layout(ids, edges, first))
  }
}
