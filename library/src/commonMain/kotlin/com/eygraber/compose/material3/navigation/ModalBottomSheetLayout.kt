package com.eygraber.compose.material3.navigation

import androidx.compose.runtime.Composable

@Composable
public fun ModalBottomSheetLayout(
  bottomSheetNavigator: ModalBottomSheetNavigator,
  content: @Composable () -> Unit,
) {
  content()
  ModalBottomSheetHost(bottomSheetNavigator)
}
