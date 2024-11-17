package com.eygraber.compose.material3.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
public expect class ModalBottomSheetNavigator() : Navigator<ModalBottomSheetNavigator.Destination> {

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
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    skipPartiallyExpanded: Boolean = false,
    content: @Composable (NavBackStackEntry) -> Unit,
  ) : NavDestination, FloatingWindow {
    internal val content: @Composable (NavBackStackEntry) -> Unit

    @ExperimentalMaterial3Api
    internal val properties: ModalBottomSheetProperties

    internal val skipPartiallyExpanded: Boolean
  }

  internal companion object {
    internal val NAME: String
  }
}
