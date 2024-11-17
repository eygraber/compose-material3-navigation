import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  alias(libs.plugins.kotlinxSerialization)
}

group = "sample-shared"

android {
  namespace = "com.eygraber.compose.material3.navigation.sample.shared"
}

kotlin {
  defaultKmpTargets(
    project = project,
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.library)

        implementation(compose.material3)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
