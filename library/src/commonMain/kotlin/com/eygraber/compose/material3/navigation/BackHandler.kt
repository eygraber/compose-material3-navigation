package com.eygraber.compose.material3.navigation

import androidx.compose.runtime.Composable

@Composable
internal expect fun BackHandler(
  onBack: () -> Unit,
)
