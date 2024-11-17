package com.eygraber.compose.material3.navigation.sample

import androidx.compose.ui.window.singleWindowApplication
import com.eygraber.compose.material3.navigation.sample.shared.App

fun main() {
  singleWindowApplication(title = "Bottom Sheet Navigation Sample") {
    App()
  }
}
