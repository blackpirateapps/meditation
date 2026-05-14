Here’s a much stronger, code-focused version you can paste directly into a programming assistant:

```markdown
# Build a Simple Android Meditation App Using Material 3 Expressive

## Objective

Create a simple, production-quality Android meditation tracking app using Kotlin, Jetpack Compose, and Material Design 3 / Material You Expressive principles. The app should allow users to create custom meditation presets, start meditation sessions, track meditation history, view statistics, and unlock achievements.

The final output should be maintainable, well-structured, accessible, and ready to extend later.

---

## Technical Stack

Use the following technologies unless the existing project already has different equivalents:

- Kotlin
- Jetpack Compose
- Material 3
- Material 3 Expressive APIs where appropriate
- Navigation Compose for screen navigation
- Room for local persistence
- ViewModel + StateFlow for state management
- Kotlin coroutines for asynchronous database operations
- `java.time` or `kotlinx-datetime` for date/time handling
- Optional: Hilt only if the project already uses dependency injection

If the project already has an established architecture, follow the existing conventions instead of introducing a conflicting structure.

---

## Material Design Requirements

The app must follow Material You / Material 3 Expressive design principles.

Implement:

- Dynamic color support on Android 12+ using `dynamicLightColorScheme` and `dynamicDarkColorScheme`.
- A fallback light and dark color scheme for older Android versions.
- `MaterialTheme` with color scheme, typography, shapes, and expressive motion.
- Use `MotionScheme.expressive()` if the Material 3 dependency supports it.
- Add `@OptIn(ExperimentalMaterial3ExpressiveApi::class)` only where required.
- Use semantic Material color roles instead of hardcoded colors.
- Use accessible contrast and clear visual hierarchy.
- Use touch targets of at least 48dp.
- Provide content descriptions for all meaningful icons.
- Prefer Material components such as `Scaffold`, `NavigationBar`, `NavigationBarItem`, `FloatingActionButton`, `Card`, `FilterChip`, `ModalBottomSheet`, `DatePicker` where useful, and Material Expressive components where they are a good fit.

The UI should feel calm, minimal, and focused, but not empty or unfinished.

---

## App Structure

The app has exactly four top-level screens accessible from a bottom navigation bar:

1. Meditations
2. Stats
3. History
4. Achievements

Use a single `Scaffold` at the app shell level with a bottom navigation bar. Each destination should preserve its own screen state where reasonable.

---

## Data Model

Create persistent local data models for:

### Meditation Preset

A meditation preset represents a reusable meditation the user creates.

Fields:

- `id`: unique identifier
- `name`: user-visible meditation name
- `icon`: identifier for the selected icon
- `durationMinutes`: meditation duration in minutes
- `preparationSeconds`: preparation countdown before the meditation starts
- `createdAt`: timestamp when the preset was created

Validation:

- Name must not be blank.
- Duration must be greater than 0 minutes.
- Preparation time must be 0 seconds or greater.
- Icon must have a default value if the user does not select one.

### Meditation Session

A session represents one completed meditation.

Fields:

- `id`: unique identifier
- `meditationId`: nullable reference to the preset used
- `meditationName`: copy of the meditation name at time of completion
- `icon`: copy of the icon at time of completion
- `durationMinutes`: completed duration
- `startedAt`: timestamp
- `completedAt`: timestamp
- `date`: local calendar date used for history and streak calculations

Store denormalized name/icon data so historical records remain meaningful even if a preset is later edited or deleted.

---

## Screen 1: Meditations

Purpose:

This is the main screen where users manage meditation presets and start meditation sessions.

Requirements:

- Show a list of all user-created meditation presets.
- Each item must display:
  - Icon
  - Meditation name
  - Duration in minutes
  - Preparation time if greater than 0
  - A meditate/start button, preferably an icon button with a clear content description
- Provide an empty state when no meditations exist.
- Include a Floating Action Button for creating a new meditation.
- The FAB should open a creation flow using either a modal bottom sheet or dialog.

### Create Meditation Flow

The user can specify:

- Name
- Icon
- Duration in minutes
- Preparation time in seconds

Implementation details:

- Use text fields or number inputs with validation.
- Provide a small set of selectable icons.
- Disable the Save button until required fields are valid.
- On save, persist the preset and return to the Meditations screen.
- Show errors inline using supporting text, not toast-only feedback.

### Starting a Meditation

When the user taps the meditate button:

- Start a meditation flow for that preset.
- First show a preparation countdown if `preparationSeconds > 0`.
- Then show the meditation countdown for `durationMinutes`.
- On completion, save a `MeditationSession`.
- After completion, return the user to the main app and ensure stats/history update automatically.

Keep this flow simple but functional. It can be implemented as a dedicated screen, dialog, or full-screen modal, but it must be clear and usable.

---

## Screen 2: Stats

Purpose:

Show aggregate meditation statistics and a duration-over-time graph.

### Hero Stats Section

At the top of the Stats screen, display a visually prominent hero section containing:

- Total meditation time since install / first recorded session
- Number of days meditated
- Average meditation time per meditated day
- Best past streak
- Current streak

Definitions:

- Total meditation time: sum of all completed session durations.
- Number of days meditated: count of unique dates with at least one completed session.
- Average meditation time: total meditation minutes divided by number of meditated days.
- Current streak: number of consecutive days ending today if the user meditated today, otherwise ending yesterday.
- Best past streak: longest consecutive-day streak across all historical data.

### Graph Section

Below the hero section, show a chart with:

- X-axis: date
- Y-axis: total meditation duration for that date
- One point/bar per date in the selected range
- If multiple sessions happen on the same date, aggregate their duration

Provide filters:

- 7D
- 2W
- 1M
- 6M
- 1Y
- All

Use Material filter chips or segmented controls. The selected filter must visibly update the graph data.

If a charting library already exists in the project, use it. Otherwise, implement a simple Compose canvas bar chart with accessible labels and empty-state handling.

---

## Screen 3: History

Purpose:

Show a month calendar where meditated days are highlighted.

Requirements:

- Display a month-view calendar.
- Allow the user to move to the previous and next month.
- Highlight dates that have one or more meditation sessions.
- Highlighting must not rely only on color; use shape, opacity, dot indicator, or another visible marker.
- When the user taps a date with sessions, open a modal bottom sheet.

### Date Details Bottom Sheet

The bottom sheet must show:

- Selected date
- Total meditation duration for that day
- List of all meditation sessions on that date
- For each session:
  - Meditation icon
  - Meditation name
  - Duration
  - Timestamp

If the selected date has no sessions, either do nothing or show a lightweight empty message. Choose the behavior that best matches the app’s UX.

---

## Screen 4: Achievements

Purpose:

Show meditation achievements based on user progress.

Create a simple achievement system with locked and unlocked states.

Include achievements such as:

- First meditation completed
- Meditated for 3 days
- Meditated for 7 days
- Reached a 3-day streak
- Reached a 7-day streak
- Completed 60 total minutes
- Completed 300 total minutes
- Completed 10 sessions

Each achievement should display:

- Icon
- Title
- Description
- Locked/unlocked visual state
- Progress indicator if the achievement is not yet complete

The achievement calculation should be deterministic and based on persisted session history. Do not hardcode unlocked states.

---

## Architecture Requirements

Use a clean, maintainable structure.

Suggested package structure:

```text
data/
  local/
  model/
  repository/

domain/
  model/
  usecase/

ui/
  navigation/
  theme/
  screens/
    meditations/
    stats/
    history/
    achievements/
  components/
```

Implement:

- Room entities and DAO queries
- Repository layer
- ViewModels for each screen or feature area
- Immutable UI state data classes
- Composable screens that receive state and callbacks
- Reusable UI components for cards, stat tiles, chart, calendar cells, and achievement rows

Avoid placing database logic directly inside composables.

---

## UX and Accessibility Requirements

The app must be usable and polished. Read the design.md file in this folder for detailed design guidelines.

Ensure:

- Loading states where data is being collected.
- Empty states for screens with no data.
- Error states for invalid user input.
- Meaningful content descriptions for icons and buttons.
- Large enough tap targets.
- Layout works on small phones and larger screens.
- Text does not overlap or truncate awkwardly.
- Dark theme works correctly.
- The app does not use hardcoded colors that break dynamic theming.

---

## Implementation Expectations

Before coding:

1. Inspect the existing project structure.
2. Identify the current Compose, Material, Navigation, and database setup.
3. Reuse existing patterns where possible.
4. Only add dependencies that are necessary.
5. Explain any dependency additions.

While coding:

- Keep functions small and focused.
- Use clear names.
- Avoid duplicated business logic.
- Put date and streak calculations in testable functions.
- Keep UI rendering separate from data calculation.
- Do not create placeholder-only screens; every screen should be functional.

---

## Testing Requirements

Add tests for business logic where practical, especially:

- Total meditation time calculation
- Unique meditated days calculation
- Current streak calculation
- Best streak calculation
- Achievement unlock calculation
- Date range filtering for stats

If the project does not currently support tests, structure the logic so tests can be added easily later.

---

## Acceptance Criteria

The implementation is complete when:

- The app builds successfully.
- The bottom navigation has four working destinations.
- Users can create meditation presets.
- Created meditations appear on the Meditations screen.
- Users can complete a meditation session.
- Completed sessions are persisted.
- Stats update based on real session data.
- The graph reflects the selected date filter.
- History calendar highlights meditated days.
- Tapping a meditated date shows session details.
- Achievements lock and unlock based on real progress.
- The app follows Material 3 / Material You Expressive styling.
- Dynamic color and dark theme are supported.
- Core calculations are separated from UI code and are testable.
```

## Verification and CICD workflow
Make a github action workflow that builds the apk.I have a keystore in base64 format, use github actions secrets to fetch the keystore then build the apk. Add a checking in the workflow for keystore.  Do not run any verification here. Make an ai handoff document for future agents (must be comprehensive) and commit and push the changes. 

In future when changing code, you have to update the ai handoff then commit and push the changes. 