# WallyMe

A modern, futuristic Android Wallpaper App with an Admin panel.

## Modules

*   **app**: The user-facing application for browsing and downloading wallpapers.
*   **admin**: The admin application for managing wallpapers and categories.
*   **core**: Shared logic and data models.

## Setup

1.  Clone the repository.
2.  Add `google-services.json` to `app/` and `admin/` directories (download from Firebase Console).
3.  Build the project using Android Studio or `./gradlew build`.

## Tech Stack

*   **Language**: Kotlin
*   **UI**: Jetpack Compose (Material3)
*   **Architecture**: MVVM
*   **Dependency Injection**: Hilt
*   **Asynchronous**: Coroutines & Flow
*   **Network/Image Loading**: Coil
*   **Backend**: Firebase (Firestore, Storage, Analytics)
