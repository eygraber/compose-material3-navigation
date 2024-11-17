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
> [!IMPORTANT]
> It is important that `NavHost` is called within the same composition as `ModalBottomSheetLayout` (e.g. don't wrap `NavHost` in a `SubcomposeLayout` like `Scaffold`, etc...)
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
  implementation("com.eygraber:compose-material3-navigation:0.0.3")
}
```

Snapshots can be found [here](https://s01.oss.sonatype.org/content/repositories/snapshots/com/eygraber/compose-material3-navigation).
