package com.eygraber.compose.material3.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.eygraber.compose.material3.navigation.ModalBottomSheetNavigator.Destination
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Navigator that navigates through [Composable]s that will be hosted within a [ModalBottomSheet]. Every
 * destination using this Navigator must set a valid [Composable] by setting it directly on an
 * instantiated [Destination] or calling [bottomSheet].
 */
@Navigator.Name("modalBottomSheet")
public actual class ModalBottomSheetNavigator actual constructor() : Navigator<Destination>() {
  /** Get the back stack from the [state]. */
  internal actual val backStack
    get() = when {
      isAttached -> state.backStack
      else -> MutableStateFlow(emptyList())
    }

  /** Get the transitioning modal bottom sheets from the [state]. */
  internal actual val transitionInProgress
    get() = when {
      isAttached -> state.transitionsInProgress
      else -> MutableStateFlow(emptySet())
    }

  /** Dismiss the modal bottom sheets destination associated with the given [backStackEntry]. */
  internal actual fun dismiss(backStackEntry: NavBackStackEntry) {
    popBackStack(backStackEntry, false)
  }

  internal actual fun onTransitionComplete(entry: NavBackStackEntry) {
    state.markTransitionComplete(entry)
  }

  actual override fun navigate(
    entries: List<NavBackStackEntry>,
    navOptions: NavOptions?,
    navigatorExtras: Extras?,
  ) {
    entries.fastForEach { entry -> state.pushWithTransition(entry) }
  }

  @ExperimentalMaterial3Api
  actual override fun createDestination(): Destination = Destination(this) {}

  actual override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
    state.popWithTransition(popUpTo, savedState)
  }

  /** NavDestination specific to [ModalBottomSheetNavigator] */
  @NavDestination.ClassType(Composable::class)
  public actual class Destination @ExperimentalMaterial3Api
  actual constructor(
    navigator: ModalBottomSheetNavigator,
    internal actual val modifier: Modifier,
    internal actual val properties: ModalBottomSheetProperties,
    internal actual val skipPartiallyExpanded: Boolean,
    internal actual val confirmValueChange: (SheetValue) -> Boolean,
    internal actual val contentWindowInsets: @Composable (SheetState) -> WindowInsets,
    internal actual val dragHandle: @Composable (() -> Unit)?,
    internal actual val content: @Composable (NavBackStackEntry) -> Unit,
  ) : NavDestination(navigator), FloatingWindow

  public actual companion object {
    internal actual const val NAME = "modalBottomSheet"
  }
}
