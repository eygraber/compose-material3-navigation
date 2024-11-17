import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  kotlin("jvm")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
}

group = "sample-desktop"

dependencies {
  implementation(projects.sample.shared)
  implementation(compose.desktop.currentOs)
  implementation(libs.kotlinx.coroutines.swing)
}

compose.desktop {
  application {
    mainClass = "com.eygraber.compose.material3.navigation.sample.nav.desktop.SampleDesktopAppKt"
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
