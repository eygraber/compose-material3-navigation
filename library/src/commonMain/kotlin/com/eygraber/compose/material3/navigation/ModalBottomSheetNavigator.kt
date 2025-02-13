package com.eygraber.compose.material3.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.eygraber.compose.material3.navigation.ModalBottomSheetNavigator.Destination
import kotlinx.coroutines.flow.StateFlow

@Composable
public fun rememberModalBottomSheetNavigator(): ModalBottomSheetNavigator = remember { ModalBottomSheetNavigator() }

/**
 * Navigator that navigates through [Composable]s that will be hosted within a [ModalBottomSheet]. Every
 * destination using this Navigator must set a valid [Composable] by setting it directly on an
 * instantiated [Destination] or calling [bottomSheet].
 */
public expect class ModalBottomSheetNavigator() : Navigator<Destination> {

  /** Get the back stack from the [state]. */
  internal val backStack: StateFlow<List<NavBackStackEntry>>

  /** Get the transitioning modal bottom sheets from the [state]. */
  internal val transitionInProgress: StateFlow<Set<NavBackStackEntry>>

  /** Dismiss the modal bottom sheets destination associated with the given [backStackEntry]. */
  internal fun dismiss(backStackEntry: NavBackStackEntry)

  internal fun onTransitionComplete(entry: NavBackStackEntry)

  override fun navigate(
    entries: List<NavBackStackEntry>,
    navOptions: NavOptions?,
    navigatorExtras: Extras?,
  )

  override fun createDestination(): Destination

  override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean)

  /** NavDestination specific to [ModalBottomSheetNavigator] */
  @Suppress("UnusedPrivateProperty")
  public class Destination @ExperimentalMaterial3Api constructor(
    navigator: ModalBottomSheetNavigator,
    modifier: Modifier = Modifier,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    contentWindowInsets: @Composable (SheetState) -> WindowInsets = defaultWindowInsets(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable (NavBackStackEntry) -> Unit,
  ) : NavDestination, FloatingWindow {
    internal val content: @Composable (NavBackStackEntry) -> Unit

    internal val modifier: Modifier

    @ExperimentalMaterial3Api
    internal val properties: ModalBottomSheetProperties

    internal val skipPartiallyExpanded: Boolean

    @ExperimentalMaterial3Api
    internal val confirmValueChange: (SheetValue) -> Boolean

    @OptIn(ExperimentalMaterial3Api::class)
    internal val contentWindowInsets: @Composable (SheetState) -> WindowInsets

    internal val dragHandle: @Composable (() -> Unit)?
  }

  public companion object {
    internal val NAME: String
  }
}

@OptIn(ExperimentalMaterial3Api::class)
public fun ModalBottomSheetNavigator.Companion.defaultWindowInsets(): @Composable (
  SheetState,
) -> WindowInsets = {
  BottomSheetDefaults.windowInsets
}
