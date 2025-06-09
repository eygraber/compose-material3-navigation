# Compose Material3 Navigation

A library which provides [Compose Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3)
support for [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
This features composable bottom sheet destinations.

## Usage

### Bottom Sheet Destinations

1. Create a `ModalBottomSheetNavigator` and add it to the `NavController`:
```kotlin
@Composable
fun MyApp() {
    val bottomSheetNavigator = rememberModalBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
}
```
2. Wrap your `NavHost` in the `ModalBottomSheetLayout` composable that accepts a `ModalBottomSheetNavigator`.
```kotlin
@Composable
fun MyApp() {
    val bottomSheetNavigator = rememberModalBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, "home") {
           // We'll define our graph here in a bit!
        }
    }
}
```
3. Register a bottom sheet destination
```kotlin
@Composable
fun MyApp() {
    val bottomSheetNavigator = rememberModalBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, "home") {
           composable(route = "home") {
               ...
           }
           bottomSheet(route = "sheet") {
               Text("This is a cool bottom sheet!")
           }
        }
    }
}
```

## Setup

```
repositories {
  mavenCentral()
}

dependencies {
  implementation("com.eygraber:compose-material3-navigation:0.0.8")
}
```

Snapshots can be found [here](https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-via-gradle).
