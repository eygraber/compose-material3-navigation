package com.eygraber.compose.material3.navigation.sample.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eygraber.compose.material3.navigation.ModalBottomSheetLayout
import com.eygraber.compose.material3.navigation.bottomSheet
import com.eygraber.compose.material3.navigation.rememberModalBottomSheetNavigator
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object BottomSheetSkippingPartial

@Serializable
data object BottomSheet

@Serializable
data object BottomSheetEdgeToEdge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
  val bottomSheetNavigator = rememberModalBottomSheetNavigator()
  val navController = rememberNavController(bottomSheetNavigator)

  MaterialTheme(
    colorScheme = if(isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
  ) {
    Surface {
      Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheetLayout(bottomSheetNavigator) {
          NavHost(
            navController = navController,
            startDestination = Home,
          ) {
            composable<Home> {
              Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Button(
                  onClick = {
                    navController.navigate(BottomSheet)
                  },
                ) {
                  Text("Open bottom sheet")
                }

                Button(
                  onClick = {
                    navController.navigate(BottomSheetSkippingPartial)
                  },
                ) {
                  Text("Open bottom sheet skipping partial state")
                }

                Button(
                  onClick = {
                    navController.navigate(BottomSheetEdgeToEdge)
                  },
                ) {
                  Text("Open edge to edge bottom sheet")
                }
              }
            }

            bottomSheet<BottomSheet> {
              Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
              ) {
                Text("This is a bottom sheet")
              }
            }

            bottomSheet<BottomSheetSkippingPartial>(
              modalBottomSheetModifier = Modifier.composed {
                Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
              },
              skipPartiallyExpanded = true,
            ) {
              Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
              ) {
                Text("This is a bottom sheet that skips the partial state")
              }
            }

            bottomSheet<BottomSheetEdgeToEdge>(
              contentWindowInsets = { sheetState ->
                when(sheetState.targetValue) {
                  SheetValue.Expanded -> WindowInsets.safeDrawing
                  else -> WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                }
              },
            ) {
              Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
              ) {
                Text("This is a bottom sheet that skips the partial state")
              }
            }
          }
        }
      }
    }
  }
}
