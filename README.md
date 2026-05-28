# World App

World App is a modern Android application that provides detailed information about continents, countries, and states. It demonstrates clean architecture principles, reactive UI with Jetpack Compose, and robust dependency injection with Hilt.

## Features

- **Continent Explorer**: Browse all continents of the world.
- **Country Discovery**: View a list of countries for a specific continent, including their flags (emojis).
- **State Details**: Drill down into specific countries to see their constituent states.
- **Offline Support**: Uses a local database (Room) to cache data for faster access and offline availability.
- **Modern Navigation**: Implements type-safe navigation using the latest Jetpack Compose Navigation components.

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM (Model-ViewModel-Intent)
- **Dependency Injection**: Hilt (Dagger)
- **Networking & Data**:
    - [CountriesService](https://github.com/Combonary/CountriesService): A custom library for fetching country and state data.
    - Apollo GraphQL (via library)
    - Room Database (via library)
- **Navigation**: Compose Navigation (Type-safe)
- **Concurrency**: Kotlin Coroutines & Flow
- **Build System**: Gradle Kotlin DSL with Version Catalog (libs.versions.toml)

## Setup & Installation

### Prerequisites

- Android Studio Ladybug or newer.
- JDK 17+.
- A GitHub Personal Access Token (PAT) with `read:packages` scope to access the `CountriesService` library hosted on GitHub Packages.

### Configuration

1. **GitHub Packages Authentication**:
   Add your GitHub credentials to your global `gradle.properties` file (usually located at `~/.gradle/gradle.properties`):

   ```properties
   gpr.user=YOUR_GITHUB_USERNAME
   gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
   ```

2. **Project Sync**:
   Open the project in Android Studio and sync with Gradle files.

## Project Structure

```text
app/src/main/java/com/pamtech/worldapp/
├── di/                 # Dependency Injection modules (Hilt)
├── presentation/       # UI layer
│   ├── continents/     # Continents screen, ViewModel, and UI State
│   ├── countries/      # Countries screen, ViewModel, and UI State
│   ├── states/         # States screen, ViewModel, and UI State
│   ├── navigation/     # Navigation routes and destinations
│   └── ui/theme/       # App theme and styling
└── WorldApp.kt         # Hilt Application class
```

## How it Works

The app uses a repository pattern provided by the `CountriesService`. Data is fetched from a GraphQL endpoint and cached locally in a Room database.

1. **Continents**: The landing screen fetches all available continents.
2. **Countries**: Selecting a continent navigates to the Countries screen, which filters countries based on the selected continent code.
3. **States**: Selecting a country shows the list of states/provinces for that country.

## License

This project is for demonstration purposes.
