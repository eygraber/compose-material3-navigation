@file:OptIn(ExperimentalMaterial3Api::class)

package com.eygraber.compose.material3.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.LocalOwnersProvider

@Composable
public fun ModalBottomSheetHost(modalBottomSheetNavigator: ModalBottomSheetNavigator) {
  val saveableStateHolder = rememberSaveableStateHolder()
  val bottomSheetBackStack by modalBottomSheetNavigator.backStack.collectAsState()
  val visibleBackStack = rememberVisibleList(bottomSheetBackStack)
  visibleBackStack.PopulateVisibleList(bottomSheetBackStack)

  val transitionInProgress by modalBottomSheetNavigator.transitionInProgress.collectAsState()
  val bottomSheetsToDispose = remember { mutableStateListOf<NavBackStackEntry>() }

  visibleBackStack.forEach { backStackEntry ->
    val destination = backStackEntry.destination as ModalBottomSheetNavigator.Destination
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = destination.skipPartiallyExpanded)
    ModalBottomSheet(
      onDismissRequest = { modalBottomSheetNavigator.dismiss(backStackEntry) },
      sheetState = sheetState,
      properties = destination.properties,
      contentWindowInsets = { WindowInsets.safeDrawing },
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
internal fun MutableList<NavBackStackEntry>.PopulateVisibleList(
  backStack: Collection<NavBackStackEntry>,
) {
  val isInspecting = LocalInspectionMode.current
  backStack.forEach { entry ->
    DisposableEffect(entry.lifecycle) {
      val observer = LifecycleEventObserver { _, event ->
        // show bottomSheet in preview
        if(isInspecting && !contains(entry)) {
          add(entry)
        }
        // ON_START -> add to visibleBackStack, ON_STOP -> remove from visibleBackStack
        if(event == Lifecycle.Event.ON_START) {
          // We want to treat the visible lists as Sets but we want to keep
          // the functionality of mutableStateListOf() so that we recompose in response
          // to adds and removes.
          if(!contains(entry)) {
            add(entry)
          }
        }
        if(event == Lifecycle.Event.ON_STOP) {
          remove(entry)
        }
      }
      entry.lifecycle.addObserver(observer)
      onDispose { entry.lifecycle.removeObserver(observer) }
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
