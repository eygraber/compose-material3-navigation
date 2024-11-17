package com.eygraber.compose.material3.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost

/**
 * A composable that hosts modal bottom sheet navigation.
 *
 * Use with a [NavHost] to manage bottom sheet destinations.
 *
 * It is important that `NavHost` is called within the same composition as `ModalBottomSheetLayout`
 * (e.g. don't wrap `NavHost` in a [SubcomposeLayout] like [Scaffold], etc...)
 *
 * Example usage:
 * ```kotlin
 * val bottomSheetNavigator = rememberModalBottomSheetNavigator()
 * val navController = rememberNavController(bottomSheetNavigator)
 *
 * ModalBottomSheetHost(
 *     modalBottomSheetNavigator = bottomSheetNavigator
 * ) {
 *     NavHost(navController, "home") {
 *         composable("home") { /* Home content */ }
 *         bottomSheet("details") { /* Details content */ }
 *     }
 * }
 * ```
 */
@ExperimentalMaterial3Api
@Composable
public fun ModalBottomSheetLayout(
  modalBottomSheetNavigator: ModalBottomSheetNavigator,
  sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
  shape: Shape = BottomSheetDefaults.ExpandedShape,
  containerColor: Color = BottomSheetDefaults.ContainerColor,
  contentColor: Color = contentColorFor(containerColor),
  tonalElevation: Dp = 0.dp,
  scrimColor: Color = BottomSheetDefaults.ScrimColor,
  dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
  contentWindowInsets: @Composable (SheetState) -> WindowInsets = { sheetState ->
    when(sheetState.targetValue) {
      SheetValue.Expanded -> WindowInsets.safeDrawing
      else -> WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
    }
  },
  content: @Composable () -> Unit,
) {
  content()
  ModalBottomSheetHost(
    modalBottomSheetNavigator = modalBottomSheetNavigator,
    sheetMaxWidth = sheetMaxWidth,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    tonalElevation = tonalElevation,
    scrimColor = scrimColor,
    dragHandle = dragHandle,
    contentWindowInsets = contentWindowInsets,
  )
}
