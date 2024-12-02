package com.eygraber.compose.material3.navigation

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost

/**
 * A composable that hosts modal bottom sheet navigation.
 *
 * Use with a [NavHost] to manage bottom sheet destinations.
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
  )
}
