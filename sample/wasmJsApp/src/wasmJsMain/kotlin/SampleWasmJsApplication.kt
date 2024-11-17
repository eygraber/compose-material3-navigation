import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.eygraber.compose.material3.navigation.sample.shared.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() = CanvasBasedWindow("Bottom Sheet Navigation Sample") {
  App()
}
