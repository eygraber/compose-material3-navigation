package com.eygraber.compose.material3.navigation.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eygraber.compose.material3.navigation.sample.shared.App

class SampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App()
    }
  }
}
