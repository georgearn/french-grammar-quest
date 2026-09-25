package com.example.ui.map

import androidx.compose.animation.core.animate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.MapEdgeKind
import com.example.data.engine.MapGraph
import com.example.data.engine.MapNode
import com.example.data.engine.MapNodeState
import com.example.data.engine.StructuredRule
import com.example.ui.GrammarViewModel
import com.example.ui.components.EnglishHelpText
import com.example.ui.components.LevelBadge
import com.example.ui.components.RulePickerSheet
import com.example.ui.components.ruleTitle
import com.example.ui.i18n.localized
import com.example.ui.screens.CodexScreen
import com.example.ui.theme.AppThemeColors
import com.example.ui.theme.MapFamilyColors
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.min

/**
 * "Carte": the learner's grammar map, wikilinks-style. Explored rules are filled nodes, rules
 * they link to are dashed "ghost" nodes, and the + button opens any rule, even one no link
 * leads to. A List view shows the same rules as an accessible, searchable index.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
  viewModel: GrammarViewModel,
  onExploreRule: (String) -> Unit,
  onTrainRule: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showList by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier.fillMaxSize()) {
    SingleChoiceSegmentedButtonRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      SegmentedButton(
        selected = !showList,
        onClick = { showList = false },
        shape = SegmentedButtonDefaults.itemShape(0, 2)
      ) { Text(stringResource(R.string.map_view_map)) }
      SegmentedButton(
        selected = showList,
        onClick = { showList = true },
        shape = SegmentedButtonDefaults.itemShape(1, 2)
      ) { Text(stringResource(R.string.map_view_list)) }
    }

    if (showList) {
      CodexScreen(viewModel = viewModel, onExploreRule = onExploreRule, onTrainRule = onTrainRule)
    } else {
      GrammarMap(viewModel = viewModel, onExploreRule = onExploreRule, onTrainRule = onTrainRule)
    }
  }
}

@Composable
private fun GrammarMap(
  viewModel: GrammarViewModel,
  onExploreRule: (String) -> Unit,
  onTrainRule: (String) -> Unit
) {
  val graph by viewModel.mapGraph.collectAsStateWithLifecycle()
  val positions by viewModel.mapPositions.collectAsStateWithLifecycle()
  val exploredIds by viewModel.mapExploredIds.collectAsStateWithLifecycle()
  val focusId by viewModel.mapFocus.collectAsStateWithLifecycle()
  val levelFilter by viewModel.levelFilter.collectAsStateWithLifecycle()
  val progressMap by viewModel.ruleTrainingProgressMap.collectAsStateWithLifecycle()

  var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
  var showPicker by rememberSaveable { mutableStateOf(false) }
  var confirmReset by rememberSaveable { mutableStateOf(false) }
  val camera = rememberMapCamera()

  val currentGraph = graph
  Box(modifier = Modifier.fillMaxSize().testTag("map_screen")) {
    when {
      currentGraph == null -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      currentGraph.nodes.isEmpty() -> EmptyMap(onPick = { showPicker = true })
      else -> {
        MapCanvas(
          graph = currentGraph,
          positions = positions,
          rules = viewModel.rules,
          camera = camera,
          focusId = focusId,
          onTapNode = { id -> selectedId = id },
          onMoveNode = viewModel::moveMapNode,
          modifier = Modifier.fillMaxSize()
        )
        MapOverlay(
          graph = currentGraph,
          onRecenter = { camera.requestFit() },
          onReset = { confirmReset = true },
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    FloatingActionButton(
      onClick = { showPicker = true },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("map_add_btn")
    ) {
      Icon(Icons.Default.Add, contentDescription = stringResource(R.string.map_add))
    }
  }

  selectedId?.let { id ->
    val node = currentGraph?.nodes?.firstOrNull { it.ruleId == id }
    val rule = viewModel.ruleById(id)
    val ghostLinks = currentGraph?.let { g -> linkedGhosts(g, id) }.orEmpty().map(viewModel::ruleById)
    NodeSheet(
      rule = rule,
      state = node?.state ?: MapNodeState.GHOST,
      bestScore = progressMap[id]?.let { p ->
        listOf(p.bestProductionScore to p.bestProductionTotal, p.bestSpotErrorScore to p.bestSpotErrorTotal)
          .filter { it.second > 0 }
          .maxByOrNull { it.first.toFloat() / it.second }
      },
      linksToDiscover = ghostLinks,
      onExplore = { selectedId = null; onExploreRule(id) },
      onTrain = { selectedId = null; onTrainRule(id) },
      onAddToMap = { viewModel.addToMap(id) },
      onSelectLink = { linkId -> selectedId = linkId },
      onDismiss = { selectedId = null }
    )
  }

  if (showPicker) {
    RulePickerSheet(
      rules = viewModel.rules,
      selectedRuleId = null,
      levelFilter = levelFilter,
      onLevelFilterChange = viewModel::setLevelFilter,
      onSelectRule = { id ->
        viewModel.addToMap(id)
        selectedId = id
      },
      onDismiss = { showPicker = false },
      progressMap = progressMap,
      title = stringResource(R.string.map_add_title),
      onMapIds = exploredIds,
      suggestions = remember(exploredIds, levelFilter, focusId) { viewModel.mapSuggestions(exploredIds) }
    )
  }

  if (confirmReset) {
    AlertDialog(
      onDismissRequest = { confirmReset = false },
      title = { Text(stringResource(R.string.map_reset_title)) },
      text = { Text(stringResource(R.string.map_reset_message)) },
      confirmButton = {
        TextButton(onClick = {
          viewModel.resetMap()
          selectedId = null
          confirmReset = false
        }) { Text(stringResource(R.string.map_reset), color = MaterialTheme.colorScheme.error) }
      },
      dismissButton = { TextButton(onClick = { confirmReset = false }) { Text(stringResource(R.string.action_cancel)) } }
    )
  }

  // Centre on the focused rule when it changes (e.g. after reading a rule in Comprendre).
  var lastCenteredFocus by rememberSaveable { mutableStateOf<String?>(null) }
  LaunchedEffect(focusId, positions) {
    val id = focusId ?: return@LaunchedEffect
    val position = positions[id] ?: return@LaunchedEffect
    if (id != lastCenteredFocus && camera.isReady) {
      camera.centerOn(position)
      lastCenteredFocus = id
    }
  }
}

/** Ghost rules directly linked to [ruleId] on the current map. */
private fun linkedGhosts(graph: MapGraph, ruleId: String): List<String> {
  val ghosts = graph.nodes.filter { it.state == MapNodeState.GHOST }.map { it.ruleId }.toSet()
  return graph.edges.mapNotNull { edge ->
    when (ruleId) {
      edge.from -> edge.to
      edge.to -> edge.from
      else -> null
    }
  }.filter { it in ghosts && it != ruleId }.distinct()
}

// --- Camera -------------------------------------------------------------------------------------

/**
 * Pan/zoom state. World coordinates are dp; screen = offset + world * density * scale.
 * Saved across rotation and tab switches; the first layout fits the whole map on screen.
 */
private class MapCamera(offsetX: Float, offsetY: Float, scale: Float) {
  var offsetX by mutableFloatStateOf(offsetX)
  var offsetY by mutableFloatStateOf(offsetY)
  var scale by mutableFloatStateOf(scale)
  var viewport by mutableStateOf(IntSize.Zero)
  var density = 1f
  var fitRequested by mutableStateOf(offsetX.isNaN())
  /** The very first view centres on the focused rule; the recenter button shows everything. */
  var fitAroundFocus = offsetX.isNaN()

  val isReady: Boolean get() = !offsetX.isNaN() && viewport != IntSize.Zero

  fun requestFit() {
    fitRequested = true
  }

  fun toScreen(x: Float, y: Float) = Offset(offsetX + x * density * scale, offsetY + y * density * scale)

  fun toWorld(p: Offset) = Offset((p.x - offsetX) / (density * scale), (p.y - offsetY) / (density * scale))

  /**
   * Fits the whole map. With [focus], a map too large to read at full view is instead shown
   * around the focused rule at a readable zoom.
   */
  fun fit(positions: Collection<Pair<Float, Float>>, focus: Pair<Float, Float>? = null) {
    if (positions.isEmpty() || viewport == IntSize.Zero) return
    val minX = positions.minOf { it.first }
    val maxX = positions.maxOf { it.first }
    val minY = positions.minOf { it.second }
    val maxY = positions.maxOf { it.second }
    val margin = 80f
    val w = (maxX - minX + margin * 2) * density
    val h = (maxY - minY + margin * 2) * density
    scale = min(viewport.width / w, viewport.height / h).coerceIn(MIN_SCALE, 1.4f)
    val cx = (minX + maxX) / 2f
    val cy = (minY + maxY) / 2f
    val (targetX, targetY) = if (focus != null && scale < READABLE_SCALE) {
      scale = READABLE_SCALE
      focus
    } else {
      cx to cy
    }
    offsetX = viewport.width / 2f - targetX * density * scale
    offsetY = viewport.height / 2f - targetY * density * scale
    fitRequested = false
    fitAroundFocus = false
  }

  suspend fun centerOn(position: Pair<Float, Float>) {
    val startX = offsetX
    val startY = offsetY
    val endX = viewport.width / 2f - position.first * density * scale
    val endY = viewport.height / 2f - position.second * density * scale
    animate(0f, 1f) { t, _ ->
      offsetX = startX + (endX - startX) * t
      offsetY = startY + (endY - startY) * t
    }
  }

  fun zoomBy(zoom: Float, centroid: Offset, pan: Offset) {
    val newScale = (scale * zoom).coerceIn(MIN_SCALE, MAX_SCALE)
    val factor = newScale / scale
    offsetX = centroid.x - (centroid.x - offsetX) * factor + pan.x
    offsetY = centroid.y - (centroid.y - offsetY) * factor + pan.y
    scale = newScale
  }

  companion object {
    const val MIN_SCALE = 0.3f
    const val MAX_SCALE = 3f
    const val READABLE_SCALE = 0.85f
  }
}

@Composable
private fun rememberMapCamera(): MapCamera {
  var savedX by rememberSaveable { mutableFloatStateOf(Float.NaN) }
  var savedY by rememberSaveable { mutableFloatStateOf(Float.NaN) }
  var savedScale by rememberSaveable { mutableFloatStateOf(1f) }
  val camera = remember { MapCamera(savedX, savedY, savedScale) }
  LaunchedEffect(camera) {
    androidx.compose.runtime.snapshotFlow { Triple(camera.offsetX, camera.offsetY, camera.scale) }
      .collect { (x, y, s) ->
        savedX = x
        savedY = y
        savedScale = s
      }
  }
  return camera
}

// --- Canvas -------------------------------------------------------------------------------------

@Composable
private fun MapCanvas(
  graph: MapGraph,
  positions: Map<String, Pair<Float, Float>>,
  rules: List<StructuredRule>,
  camera: MapCamera,
  focusId: String?,
  onTapNode: (String) -> Unit,
  onMoveNode: (String, Float, Float) -> Unit,
  modifier: Modifier = Modifier
) {
  camera.density = LocalDensity.current.density
  val textMeasurer = rememberTextMeasurer()

  // Node being dragged (after a long press) and its current world position.
  var dragId by remember { mutableStateOf<String?>(null) }
  var dragPosition by remember { mutableStateOf(Offset.Zero) }

  val currentPositions by rememberUpdatedState(positions)
  val currentGraph by rememberUpdatedState(graph)
  val currentOnTap by rememberUpdatedState(onTapNode)
  val currentOnMove by rememberUpdatedState(onMoveNode)

  LaunchedEffect(camera.fitRequested, positions, camera.viewport) {
    if (camera.fitRequested && positions.isNotEmpty()) {
      val focus = if (camera.fitAroundFocus) focusId?.let { positions[it] } else null
      camera.fit(positions.values, focus)
    }
  }

  // Colours and labels are resolved in composition; the draw pass only reads them.
  val families = graph.nodes.map { it.family }.distinct()
  val familyColors = families.associateWith { MapFamilyColors.forFamily(it) }
  val contrastColor = AppThemeColors.gold
  val masteredColor = AppThemeColors.success
  val focusColor = MaterialTheme.colorScheme.primary
  val surface = MaterialTheme.colorScheme.surface
  val background = MaterialTheme.colorScheme.background
  val exploredText = MaterialTheme.colorScheme.onSurface
  val ghostText = MaterialTheme.colorScheme.onSurfaceVariant
  val labelStyle = MaterialTheme.typography.labelMedium
  val titles = rules.associate { it.id to ruleTitle(it) }
  // Short caption under each node: the part of the title before ":", on two lines at most.
  val labelMaxWidth = with(LocalDensity.current) { 132.dp.roundToPx() }
  val labels: Map<String, TextLayoutResult> = remember(graph, titles, exploredText, labelStyle, labelMaxWidth) {
    graph.nodes.associate { node ->
      val title = titles[node.ruleId].orEmpty().substringBefore(":").trim()
      node.ruleId to textMeasurer.measure(
        title,
        style = labelStyle.copy(
          color = if (node.state == MapNodeState.GHOST) ghostText else exploredText,
          fontWeight = if (node.state == MapNodeState.GHOST) FontWeight.Normal else FontWeight.Medium,
          textAlign = TextAlign.Center
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        constraints = Constraints(maxWidth = labelMaxWidth)
      )
    }
  }

  val description = stringResource(R.string.map_description, graph.exploredCount, graph.ghostCount)

  fun positionOf(id: String): Offset? =
    if (id == dragId) dragPosition else currentPositions[id]?.let { Offset(it.first, it.second) }

  fun hitTest(screen: Offset): String? {
    val world = camera.toWorld(screen)
    val touchRadius = 28f / camera.scale
    return currentGraph.nodes
      .mapNotNull { node -> positionOf(node.ruleId)?.let { node.ruleId to hypot(it.x - world.x, it.y - world.y) } }
      .filter { it.second <= touchRadius }
      .minByOrNull { it.second }
      ?.first
  }

  Canvas(
    modifier = modifier
      .onSizeChanged { camera.viewport = it }
      .clearAndSetSemantics { contentDescription = description }
      .pointerInput(Unit) {
        detectTapGestures(onTap = { p -> hitTest(p)?.let { currentOnTap(it) } })
      }
      .pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
          onDragStart = { p ->
            hitTest(p)?.let { id ->
              dragId = id
              dragPosition = positionOf(id) ?: camera.toWorld(p)
            }
          },
          onDrag = { change, amount ->
            if (dragId != null) {
              change.consume()
              dragPosition += Offset(amount.x / (camera.density * camera.scale), amount.y / (camera.density * camera.scale))
            }
          },
          onDragEnd = {
            dragId?.let { id -> currentOnMove(id, dragPosition.x, dragPosition.y) }
            dragId = null
          },
          onDragCancel = { dragId = null }
        )
      }
      .pointerInput(Unit) {
        detectTransformGestures { centroid, pan, zoom, _ ->
          if (dragId == null) camera.zoomBy(zoom, centroid, pan)
        }
      }
  ) {
    if (!camera.isReady) return@Canvas
    val scale = camera.scale

    // Edges first, under the nodes.
    graph.edges.forEach { edge ->
      val a = positionOf(edge.from) ?: return@forEach
      val b = positionOf(edge.to) ?: return@forEach
      val from = camera.toScreen(a.x, a.y)
      val to = camera.toScreen(b.x, b.y)
      val fromNode = graph.nodes.firstOrNull { it.ruleId == edge.from }
      val base = when (edge.kind) {
        MapEdgeKind.CONTRAST -> contrastColor
        else -> familyColors[fromNode?.family] ?: ghostText
      }
      val alpha = if (edge.toGhost) 0.35f else 0.7f
      drawLine(
        color = base.copy(alpha = alpha),
        start = from,
        end = to,
        strokeWidth = (if (edge.kind == MapEdgeKind.CONTRAST) 2.5f else 1.5f).dp.toPx() * scale.coerceIn(0.6f, 1.5f),
        pathEffect = if (edge.kind == MapEdgeKind.FAMILY) {
          PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 5.dp.toPx()))
        } else {
          null
        }
      )
    }

    // Nodes, remembering where each caption would go.
    val captions = mutableListOf<Triple<MapNode, Offset, Float>>()
    graph.nodes.forEach { node ->
      val p = positionOf(node.ruleId) ?: return@forEach
      val center = camera.toScreen(p.x, p.y)
      val color = familyColors[node.family] ?: ghostText
      val nodeScale = scale.coerceIn(0.6f, 1.6f)
      val radius = (if (node.state == MapNodeState.GHOST) 8f else 11f).dp.toPx() * nodeScale
      drawNode(node, center, radius, color, surface, masteredColor, focusColor, node.ruleId == focusId)
      captions += Triple(node, center, radius)
    }

    // Captions on top of every node, most important first; a caption that would overlap one
    // already drawn is skipped (zooming in reveals it). Ghost captions need more zoom.
    val labelScale = scale.coerceIn(0.6f, 1.1f)
    val drawn = mutableListOf<Rect>()
    captions
      .sortedBy { (node, _, _) ->
        when {
          node.ruleId == focusId -> 0
          node.state != MapNodeState.GHOST -> 1
          else -> 2
        }
      }
      .forEach { (node, center, radius) ->
        val label = labels[node.ruleId] ?: return@forEach
        val visible = node.ruleId == focusId || scale >= if (node.state == MapNodeState.GHOST) 0.75f else 0.4f
        if (!visible) return@forEach
        val width = label.size.width * labelScale
        val height = label.size.height * labelScale
        val top = center.y + radius + 4.dp.toPx()
        val rect = Rect(center.x - width / 2f, top, center.x + width / 2f, top + height)
        if (drawn.any { it.overlaps(rect) }) return@forEach
        drawn += rect
        withTransform({ scale(labelScale, labelScale, pivot = Offset(center.x, top)) }) {
          val topLeft = Offset(center.x - label.size.width / 2f, top)
          drawRoundRect(
            color = background.copy(alpha = 0.8f),
            topLeft = topLeft - Offset(4.dp.toPx(), 1.dp.toPx()),
            size = Size(label.size.width + 8.dp.toPx(), label.size.height + 2.dp.toPx()),
            cornerRadius = CornerRadius(6.dp.toPx())
          )
          drawText(label, topLeft = topLeft)
        }
      }
  }
}

private fun DrawScope.drawNode(
  node: MapNode,
  center: Offset,
  radius: Float,
  color: Color,
  surface: Color,
  masteredColor: Color,
  focusColor: Color,
  focused: Boolean
) {
  when (node.state) {
    MapNodeState.GHOST -> {
      drawCircle(surface, radius, center)
      drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 3.dp.toPx())))
      )
    }
    MapNodeState.EXPLORED, MapNodeState.MASTERED -> {
      drawCircle(color, radius, center)
      if (node.state == MapNodeState.MASTERED) {
        drawCircle(masteredColor, radius + 4.dp.toPx(), center, style = Stroke(width = 2.dp.toPx()))
      }
    }
  }
  if (focused) {
    drawCircle(focusColor, radius + 8.dp.toPx(), center, style = Stroke(width = 2.5.dp.toPx()))
  }
}

// --- Overlay, empty state, legend ---------------------------------------------------------------

@Composable
private fun MapOverlay(
  graph: MapGraph,
  onRecenter: () -> Unit,
  onReset: () -> Unit,
  modifier: Modifier = Modifier
) {
  var menuOpen by remember { mutableStateOf(false) }
  Box(modifier = modifier) {
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(start = 16.dp, top = 4.dp)
    ) {
      Text(
        text = stringResource(R.string.map_stats, graph.exploredCount, graph.ghostCount),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
      )
    }

    Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp)) {
      IconButton(onClick = { menuOpen = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.map_more))
      }
      DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
        DropdownMenuItem(
          text = { Text(stringResource(R.string.map_reset)) },
          onClick = {
            menuOpen = false
            onReset()
          }
        )
      }
    }

    MapLegend(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp))

    SmallFloatingActionButton(
      onClick = onRecenter,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 20.dp, bottom = 88.dp)
    ) {
      Icon(Icons.Default.CenterFocusStrong, contentDescription = stringResource(R.string.map_recenter))
    }
  }
}

@Composable
private fun MapLegend(modifier: Modifier = Modifier) {
  val color = MaterialTheme.colorScheme.primary
  val surface = MaterialTheme.colorScheme.surface
  val mastered = AppThemeColors.success
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      LegendRow(stringResource(R.string.map_state_explored)) {
        drawCircle(color, size.minDimension / 2.6f)
      }
      LegendRow(stringResource(R.string.map_state_mastered)) {
        drawCircle(color, size.minDimension / 3.4f)
        drawCircle(mastered, size.minDimension / 2.2f, style = Stroke(1.5.dp.toPx()))
      }
      LegendRow(stringResource(R.string.map_state_ghost)) {
        drawCircle(surface, size.minDimension / 2.6f)
        drawCircle(
          color,
          size.minDimension / 2.6f,
          style = Stroke(1.2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 2.dp.toPx())))
        )
      }
    }
  }
}

@Composable
private fun LegendRow(label: String, icon: DrawScope.() -> Unit) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Canvas(modifier = Modifier.size(14.dp), onDraw = icon)
    Spacer(Modifier.width(8.dp))
    Text(label, style = MaterialTheme.typography.labelMedium)
  }
}

@Composable
private fun EmptyMap(onPick: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(72.dp)) {
      Box(contentAlignment = Alignment.Center) {
        Icon(Icons.Default.Hub, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(36.dp))
      }
    }
    Spacer(Modifier.height(20.dp))
    Text(
      stringResource(R.string.map_empty_title),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(8.dp))
    Text(
      stringResource(R.string.map_empty_desc),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(24.dp))
    Button(onClick = onPick, modifier = Modifier.heightIn(min = 48.dp)) {
      Text(stringResource(R.string.map_empty_cta))
    }
  }
}

// --- Node sheet ---------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NodeSheet(
  rule: StructuredRule,
  state: MapNodeState,
  bestScore: Pair<Int, Int>?,
  linksToDiscover: List<StructuredRule>,
  onExplore: () -> Unit,
  onTrain: () -> Unit,
  onAddToMap: () -> Unit,
  onSelectLink: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val scope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState()
  val familyColor = MapFamilyColors.forFamily(com.example.data.engine.RuleGraph.family(rule.categoryId))

  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    Column(
      modifier = Modifier
        .padding(horizontal = 20.dp)
        .padding(bottom = 24.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        LevelBadge(rule.level)
        Spacer(Modifier.width(8.dp))
        Canvas(Modifier.size(8.dp)) { drawCircle(familyColor) }
        Spacer(Modifier.width(6.dp))
        Text(rule.categoryName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.weight(1f))
        StatusPill(state)
      }
      Spacer(Modifier.height(10.dp))
      Text(ruleTitle(rule), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
      if (ruleTitle(rule) == rule.titleFr) EnglishHelpText(rule.titleEn)
      Spacer(Modifier.height(8.dp))
      Text(
        localized(rule.summaryFr, rule.summaryEn),
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 5,
        overflow = TextOverflow.Ellipsis
      )
      bestScore?.let { (score, total) ->
        Spacer(Modifier.height(8.dp))
        Text(
          stringResource(R.string.train_best_score, score, total),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.primary
        )
      }

      Spacer(Modifier.height(16.dp))
      if (state == MapNodeState.GHOST) {
        Button(
          onClick = {
            onAddToMap()
            scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
          },
          modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        ) { Text(stringResource(R.string.map_add_to_map)) }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onExplore, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
          Text(stringResource(R.string.map_open_in_explore))
        }
      } else {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(onClick = onExplore, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
            Text(stringResource(R.string.nav_explore))
          }
          OutlinedButton(onClick = onTrain, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
            Text(stringResource(R.string.nav_train))
          }
        }
      }

      if (state != MapNodeState.GHOST) {
        Spacer(Modifier.height(20.dp))
        Text(stringResource(R.string.map_links_to_discover), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        if (linksToDiscover.isEmpty()) {
          Text(
            stringResource(R.string.map_no_links),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
          )
        } else {
          linksToDiscover.forEach { link ->
            HorizontalDivider()
            ListItem(
              headlineContent = { Text(ruleTitle(link)) },
              leadingContent = { LevelBadge(link.level) },
              colors = ListItemDefaults.colors(containerColor = Color.Transparent),
              modifier = Modifier.clickable(role = Role.Button) { onSelectLink(link.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatusPill(state: MapNodeState) {
  val (label, container, content) = when (state) {
    MapNodeState.GHOST -> Triple(R.string.map_state_ghost, MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    MapNodeState.EXPLORED -> Triple(R.string.map_state_explored, AppThemeColors.primaryContainer, AppThemeColors.onPrimaryContainer)
    MapNodeState.MASTERED -> Triple(R.string.map_state_mastered, AppThemeColors.successContainer, AppThemeColors.onSuccessContainer)
  }
  Surface(shape = RoundedCornerShape(8.dp), color = container) {
    Text(
      stringResource(label),
      style = MaterialTheme.typography.labelMedium,
      color = content,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}
