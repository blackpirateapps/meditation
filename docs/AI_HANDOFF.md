# AI Handoff: Pause Meditation App

Last updated: 2026-05-14

## Project Summary

Pause is a Kotlin Android app built with Jetpack Compose, Material 3 Expressive, Navigation Compose, Room, StateFlow, and pure domain calculators. The repository started as an empty shell containing only `prompt.md` and `design.md`; the current implementation scaffolds a complete single-module Android app in `:app`.

The product surface has exactly four top-level destinations behind a single app-level `Scaffold` and floating bottom navigation:

- Meditations
- Stats
- History
- Achievements

## Important User Instruction

The user explicitly requested:

- Build the Android app from `prompt.md` and `design.md`.
- Use available project skills where useful.
- Add a GitHub Actions workflow that builds a signed APK from a base64 keystore stored in secrets.
- Add a keystore check in the workflow.
- Do not run verification locally.
- Create this handoff document.
- Commit and push changes.
- In future code changes, update this handoff document before committing and pushing.

Do not run local builds/tests for this request unless the user later changes that instruction.

## Build And Dependency Notes

Build files:

- `settings.gradle.kts`
- `build.gradle.kts`
- `gradle/libs.versions.toml`
- `gradle.properties`
- `app/build.gradle.kts`

Key choices:

- AGP `8.13.2`
- Kotlin `2.2.20`
- Compose BOM `2025.12.01`
- Material 3 `1.5.0-alpha15` explicitly, so `MotionScheme.expressive()` and Material 3 Expressive APIs are available while staying compatible with AGP `8.13.2` and compile SDK `36`.
- minSdk `23`, required by recent Material 3 Expressive artifacts.
- compileSdk/targetSdk `36`
- Room `2.8.4` with KSP
- Core library desugaring is enabled because domain code uses `java.time` on minSdk 23.
- `gradle.properties` enables `android.useAndroidX=true`; this is required for release builds because the app depends on AndroidX/Compose artifacts.
- `android.nonTransitiveRClass=true` is enabled to keep generated resources scoped to the app module.

Recent build fix:

- 2026-05-14: Added root `gradle.properties` after `:app:mergeReleaseNativeLibs` failed with AndroidX dependencies detected while `android.useAndroidX` was not enabled.
- 2026-05-14: Downgraded Material 3 from `1.5.0-alpha19` to `1.5.0-alpha15` after `:app:checkReleaseAarMetadata` reported that `alpha19` requires compile SDK `37` and Android Gradle Plugin `9.1.0`. The `alpha15` AAR metadata still requires only min compile SDK `35` and AGP `8.6.0`, which fits this project.

Gradle wrapper:

- Wrapper files were bootstrapped from another local Android project.
- `gradle/wrapper/gradle-wrapper.properties` points to Gradle `8.14.3`.

## CI / APK Signing

Workflow file:

- `.github/workflows/build-apk.yml`

Required GitHub Actions secrets:

- `PAUSE_RELEASE_KEYSTORE_BASE64`: base64-encoded keystore content
- `PAUSE_KEYSTORE_PASSWORD`: keystore password
- `PAUSE_KEY_ALIAS`: release key alias
- `PAUSE_KEY_PASSWORD`: key password

Workflow behavior:

- Checks all signing secrets before building.
- Decodes the keystore into `app/keystore/release.jks`.
- Uses `keytool -list` to verify the keystore and alias.
- Runs `./gradlew assembleRelease`.
- Uploads the release APK artifact from `app/build/outputs/apk/release/*.apk`.

The local repo ignores `*.jks`, `*.keystore`, and `keystore/`.

## Architecture

Package root:

- `com.blackpirateapps.pause`

Main entry points:

- `PauseApplication.kt`: owns the lazy Room database and repository.
- `MainActivity.kt`: enables edge-to-edge rendering and sets `PauseTheme` + `PauseApp`.

Data layer:

- `data/local/MeditationPresetEntity.kt`
- `data/local/MeditationSessionEntity.kt`
- `data/local/PauseDao.kt`
- `data/local/PauseDatabase.kt`
- `data/model/EntityMappers.kt`
- `data/repository/MeditationRepository.kt`

Domain layer:

- `domain/model/MeditationIcon.kt`
- `domain/model/MeditationPreset.kt`
- `domain/model/MeditationSession.kt`
- `domain/model/MeditationStats.kt`
- `domain/model/Achievement.kt`
- `domain/usecase/StatsCalculator.kt`
- `domain/usecase/AchievementCalculator.kt`

UI layer:

- `ui/theme/Theme.kt`
- `ui/navigation/PauseApp.kt`
- `ui/navigation/Screen.kt`
- `ui/navigation/PauseViewModelFactory.kt`
- `ui/components/*`
- `ui/screens/meditations/*`
- `ui/screens/stats/*`
- `ui/screens/history/*`
- `ui/screens/achievements/*`

Tests:

- `app/src/test/java/com/blackpirateapps/pause/StatsCalculatorTest.kt`
- `app/src/test/java/com/blackpirateapps/pause/AchievementCalculatorTest.kt`

## Data Model Details

`MeditationPresetEntity` stores:

- auto-generated `id`
- `name`
- `icon`
- `durationMinutes`
- `preparationSeconds`
- `createdAtEpochMillis`

`MeditationSessionEntity` stores denormalized session history:

- auto-generated `id`
- nullable `meditationId`
- copied `meditationName`
- copied `icon`
- `durationMinutes`
- `startedAtEpochMillis`
- `completedAtEpochMillis`
- `dateEpochDay`

Denormalized name/icon data ensures history remains meaningful if preset editing/deletion is added later.

## Feature Status

Meditations:

- Lists persisted presets.
- Empty state.
- FAB opens `CreateMeditationSheet`.
- Validates name, duration, and prep time in the form.
- Persists new presets through Room.
- Starts a full-screen meditation flow.
- Optional preparation countdown precedes the meditation countdown.
- Completed sessions are persisted after the meditation countdown.

Stats:

- Shows total minutes, meditated days, average minutes per meditated day, best streak, current streak, and session count-derived state.
- Supports 7D, 2W, 1M, 6M, 1Y, and All filters.
- Uses a simple Compose Canvas rounded bar chart.
- Aggregates multiple sessions on the same date.

History:

- Displays a month calendar.
- Supports previous/next month navigation.
- Highlights meditated days with a tonal filled cell and dot marker.
- Tapping a meditated date opens a bottom sheet with total duration and session details.

Achievements:

- Uses deterministic rules from persisted sessions.
- Includes all requested achievements:
  - first meditation
  - 3 meditated days
  - 7 meditated days
  - 3-day streak
  - 7-day streak
  - 60 total minutes
  - 300 total minutes
  - 10 sessions
- Shows locked/unlocked state and progress.

## Calculation Rules

`StatsCalculator` owns:

- total minutes
- unique meditated days
- average meditation time per meditated day
- current streak
- best historical streak
- daily totals by range

Current streak logic:

- Ends today if today has a session.
- Otherwise ends yesterday if yesterday has a session.
- Otherwise returns `0`.

Best streak logic:

- Uses sorted unique meditation dates and finds the longest consecutive run.

`AchievementCalculator` derives progress from `StatsCalculator`; no achievement state is hardcoded.

## UI / Material Notes

The app uses:

- dynamic colors on Android 12+
- fallback light/dark color schemes
- `MaterialTheme(..., motionScheme = MotionScheme.expressive())`
- semantic color roles
- rounded `Shapes` with `extraLarge = 32.dp`
- edge-to-edge setup in `MainActivity`
- floating translucent bottom navigation
- Material components such as `Scaffold`, `NavigationBar`, `FloatingActionButton`, `ModalBottomSheet`, `FilterChip`, cards, buttons, and text fields

Known simplifications versus `design.md`:

- The stats header does not yet compress into a sticky app bar.
- Shared element transitions and an edit screen for tapping preset cards are not implemented.
- The bottom navigation uses translucency and elevation, but not true platform background blur.

These are good future enhancements, not blockers for the acceptance criteria in `prompt.md`.

## Verification Status

Per user instruction, no local verification was run. Specifically:

- No Gradle build was run.
- No unit tests were run.
- No APK was assembled locally.

Future agents should mention this status if asked about confidence or release readiness.

## Future Change Checklist

Before committing future code changes:

1. Update this handoff document with architectural, workflow, or known-risk changes.
2. Keep domain calculations UI-free and testable.
3. Keep Room access out of composables.
4. Preserve the four top-level destinations.
5. Keep signing secrets out of the repo.
6. Commit and push only after the handoff is updated.
