@file:OptIn(ExperimentalMaterial3Api::class)

package com.eygraber.compose.material3.navigation

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.LocalOwnersProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ModalBottomSheetHost(
  modalBottomSheetNavigator: ModalBottomSheetNavigator,
  sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
  shape: Shape = BottomSheetDefaults.ExpandedShape,
  containerColor: Color = BottomSheetDefaults.ContainerColor,
  contentColor: Color = contentColorFor(containerColor),
  tonalElevation: Dp = 0.dp,
  scrimColor: Color = BottomSheetDefaults.ScrimColor,
  dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
) {
  val saveableStateHolder = rememberSaveableStateHolder()
  val bottomSheetBackStack by modalBottomSheetNavigator.backStack.collectAsState()
  val visibleBackStack = rememberVisibleList(bottomSheetBackStack)

  val scope = rememberCoroutineScope()

  val transitionInProgress by modalBottomSheetNavigator.transitionInProgress.collectAsState()
  val bottomSheetsToDispose = remember { mutableStateListOf<NavBackStackEntry>() }

  visibleBackStack.forEach { backStackEntry ->
    val destination = backStackEntry.destination as ModalBottomSheetNavigator.Destination
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = destination.skipPartiallyExpanded)

    BackHandler {
      scope.launch {
        sheetState.hide()
      }
    }

    ModalBottomSheet(
      onDismissRequest = { modalBottomSheetNavigator.dismiss(backStackEntry) },
      sheetState = sheetState,
      modifier = destination.modifier,
      sheetMaxWidth = sheetMaxWidth,
      shape = shape,
      properties = destination.properties,
      containerColor = containerColor,
      contentColor = contentColor,
      tonalElevation = tonalElevation,
      scrimColor = scrimColor,
      dragHandle = dragHandle,
      contentWindowInsets = {
        destination.contentWindowInsets(sheetState)
      },
    ) {
      DisposableEffect(backStackEntry) {
        bottomSheetsToDispose.add(backStackEntry)
        onDispose {
          modalBottomSheetNavigator.onTransitionComplete(backStackEntry)
          bottomSheetsToDispose.remove(backStackEntry)
        }
      }

      // while in the scope of the composable, we provide the navBackStackEntry as the
      // ViewModelStoreOwner and LifecycleOwner
      backStackEntry.LocalOwnersProvider(saveableStateHolder) {
        destination.content(backStackEntry)
      }
    }
  }
  // BottomSheets may have been popped before it was composed. To prevent leakage, we need to
  // mark popped entries as complete here. Check that we don't accidentally complete popped
  // entries that were composed, unless they were disposed of already.
  LaunchedEffect(transitionInProgress, bottomSheetsToDispose) {
    transitionInProgress.forEach { entry ->
      if(
        !modalBottomSheetNavigator.backStack.value.contains(entry) &&
        !bottomSheetsToDispose.contains(entry)
      ) {
        modalBottomSheetNavigator.onTransitionComplete(entry)
      }
    }
  }
}

@Composable
internal fun rememberVisibleList(
  backStack: Collection<NavBackStackEntry>,
): SnapshotStateList<NavBackStackEntry> {
  // show bottomSheet in preview
  val isInspecting = LocalInspectionMode.current
  return remember(backStack) {
    mutableStateListOf<NavBackStackEntry>().apply {
      addAll(
        backStack.filter { entry ->
          if(isInspecting) {
            true
          }
          else {
            entry.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
          }
        },
      )
    }
  }
}
