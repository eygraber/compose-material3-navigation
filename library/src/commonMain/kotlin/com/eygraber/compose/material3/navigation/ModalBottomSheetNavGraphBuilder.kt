package com.eygraber.compose.material3.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.get
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

/**
 * Add the [Composable] to the [NavGraphBuilder] that will be hosted within a
 * [ModalBottomSheet]. This is suitable only when this ModalBottomSheet represents a separate
 * screen in your app that needs its own lifecycle and saved state, independent of any other
 * destination in your navigation graph.
 *
 * @param route route for the destination
 * @param arguments list of arguments to associate with destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param modalBottomSheetProperties properties that should be passed to
 *   [ModalBottomSheet].
 * @param skipPartiallyExpanded Whether the partially expanded state, if the sheet is tall enough,
 *   should be skipped. If true, the sheet will always expand to the [Expanded] state and move to
 *   the [Hidden] state when hiding the sheet, either programmatically or by user interaction.
 * @param content composable content for the destination that will be hosted within the ModalBottomSheet
 */
@ExperimentalMaterial3Api
public fun NavGraphBuilder.bottomSheet(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
  skipPartiallyExpanded: Boolean = false,
  content: @Composable (NavBackStackEntry) -> Unit,
) {
  destination(
    ModalBottomSheetNavigatorDestinationBuilder(
      provider[ModalBottomSheetNavigator::class],
      route,
      modalBottomSheetProperties,
      skipPartiallyExpanded,
      content,
    )
      .apply {
        arguments.forEach { (argumentName, argument) -> argument(argumentName, argument) }
        deepLinks.forEach { deepLink -> deepLink(deepLink) }
      },
  )
}

/**
 * Add the [Composable] to the [NavGraphBuilder] that will be hosted within a
 * [ModalBottomSheet]. This is suitable only when this ModalBottomSheet represents a separate
 * screen in your app that needs its own lifecycle and saved state, independent of any other
 * destination in your navigation graph.
 *
 * @param T route from a KClass for the destination
 * @param typeMap map of destination arguments' kotlin type [KType] to its respective custom
 *   [NavType]. May be empty if [T] does not use custom NavTypes.
 * @param deepLinks list of deep links to associate with the destinations
 * @param modalBottomSheetProperties properties that should be passed to
 *   [ModalBottomSheet].
 * @param skipPartiallyExpanded Whether the partially expanded state, if the sheet is tall enough,
 *   should be skipped. If true, the sheet will always expand to the [Expanded] state and move to
 *   the [Hidden] state when hiding the sheet, either programmatically or by user interaction.
 * @param content composable content for the destination that will be hosted within the ModalBottomSheet
 */
@ExperimentalMaterial3Api
public inline fun <reified T : Any> NavGraphBuilder.bottomSheet(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
  skipPartiallyExpanded: Boolean = false,
  noinline content: @Composable (NavBackStackEntry) -> Unit,
) {
  destination(
    ModalBottomSheetNavigatorDestinationBuilder(
      provider[ModalBottomSheetNavigator::class],
      T::class,
      typeMap,
      modalBottomSheetProperties,
      skipPartiallyExpanded,
      content,
    )
      .apply { deepLinks.forEach { deepLink -> deepLink(deepLink) } },
  )
}
