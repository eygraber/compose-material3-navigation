import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
}

kotlin {
  kmpTargets(
    KmpTarget.WasmJs,
    project = project,
    binaryType = BinaryType.Executable,
    webOptions = KmpTarget.WebOptions(
      isNodeEnabled = false,
      isBrowserEnabled = true,
      moduleName = "sample-wasm",
    ),
    ignoreDefaultTargets = true,
  )
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      commonWebpackConfig {
        outputFileName = "sample-wasm.js"
        experiments += "topLevelAwait"
      }
    }
  }

  sourceSets {
    wasmJsMain {
      dependencies {
        implementation(projects.sample.shared)

        implementation(compose.material3)
      }
    }
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
