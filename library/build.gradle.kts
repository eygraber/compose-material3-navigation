plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.eygraber.compose.material3.navigation"
}

kotlin {
  defaultKmpTargets(
    project = project,
  )

  sourceSets {
    commonMain.dependencies {
      api(libs.compose.navigation)

      implementation(compose.material3)
      implementation(compose.runtime)

      implementation(libs.kotlinx.coroutines.core)
    }
  }
}
