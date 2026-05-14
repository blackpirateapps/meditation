

# Design Specification: "Pause" (Meditation App)

**Target UI Framework:** Jetpack Compose (Material 3 Expressive / Android 16+ Guidelines)
**Core Philosophy:** Window-First, Edge-to-Edge, Glassmorphism, and Organic Geometry.

## 1. System-Wide UI/UX Paradigms

To achieve the modern 2026 Google app feel, the agent must adhere to the following global rules:

* **Mandatory Edge-to-Edge:** The app must draw behind the status and navigation bars. Use `WindowCompat.setDecorFitsSystemWindows(window, false)` and handle safe areas strictly using `WindowInsets`.
* **Theming & Colors:** Utilize the system's 16-tone dynamic color palette. The app should default to the **"Soft"** or **"Neutral"** intensity presets to evoke a calming, meditative vibe, avoiding overly harsh saturations.
* **Typography:** Strict use of `MaterialTheme.typography` driven by Google Sans Variable. Expect large, tightly-leaded headers and spacious tracking for body text.
* **Corner Radii:** Apply extreme rounding. Standard cards use `32dp` radii, and inner elements use `16dp` to `24dp`.
* **Motion:** Predictive back gestures are mandatory. Screen transitions should utilize Compose `sharedElement` APIs with Material spring physics.

---

## 2. Navigation Architecture

* **Component:** Floating Bottom Navigation Bar.
* **Styling:** Instead of a full-width block, use a pill-shaped `NavigationBar` hovering slightly above the system gesture handle.
* **Material:** Implement a translucent blur (Glassmorphism) using an alpha surface over a blur modifier, allowing the scrolling content of the pages to bleed elegantly underneath the navigation.

---

## 3. Screen-by-Screen Breakdown

### Screen 1: Meditations (The Hub)

This is the landing page, built around organic, media-forward cards.

* **Layout Structure:** Edge-to-edge `LazyColumn` or `LazyVerticalGrid`.
* **Floating Action Button (FAB):**
* *Design:* A large, expressive FAB (minimum `96dp` width) anchored above the floating bottom bar.
* *Action:* Triggers a **Bottom Sheet** (with background blur) for creating a new meditation.
* *Inputs:* Text field for Name, an Icon picker (using dynamic fluid grids), and numeric inputs for Duration (minutes) and Prep Time (seconds).


* **Meditation Cards:**
* *Geometry:* `MaterialTheme.shapes.extraLarge` (`32dp` rounding).
* *Content Layout:* The selected icon sits in a tonal circular container. Text is left-aligned.
* *Action:* The "Meditate" button is an expressive, filled tonal icon button nested inside the card. Tapping the card itself (not the button) triggers a `sharedElement` transition into a full-screen editing view.



### Screen 2: Stats (Data & Growth)

This screen visualizes user progress using dynamic, borderless charts and hero typography.

* **Hero Header:**
* *Behavior:* The top 30% of the screen is a dynamic header. As the user scrolls up to view the graph, this header smoothly compresses into a sticky top app bar.
* *Data Display:* Use a masonry grid of small, pill-shaped metric chips (Total Time, Days, Avg Time, Streaks). The "Current Streak" should use a prominent, high-intensity color role (e.g., `TertiaryContainer`) to stand out.


* **Graph Section:**
* *Style:* No hard X/Y axis lines. Use a smooth, organic cubic Bezier curve for the line graph or heavily rounded vertical bars. The area under the curve should feature a soft, fading gradient.
* *Filters:* A horizontally scrollable row of selectable filter chips (7D, 2W, 1M, 6M, 1Y, All) placed immediately above the graph.



### Screen 3: History (The Journey)

A fluid calendar view that prioritizes readability and seamless contextual popups.

* **Calendar View:**
* *Layout:* A fluid grid that dynamically resizes based on the device width.
* *Indicators:* Days with recorded meditations should not use harsh dots. Instead, fill the date's background with a soft, tonal color (e.g., `SecondaryContainer`) and increase the corner rounding to make it a perfect circle.


* **Interaction (Contextual Island):**
* *Behavior:* Clicking a highlighted date does not navigate to a new page. Instead, it triggers a **Modal Bottom Sheet**.
* *Content:* The sheet lists the sessions for that day, showing the icon, total duration, and timestamp, utilizing the same `extraLarge` card styling found on the Home screen.



### Screen 4: Achievements (Milestones)

A playful, visually distinct section to reward consistency.

* **Layout:** A justified, adaptive grid (similar to the Photos grid pattern).
* **Badges/Cards:**
* *Unlocked:* Displayed with vibrant colors (temporarily overriding the "Soft" app theme with a "Bold" intensity preset for the individual card) and a subtle, looping entry animation.
* *Locked:* Displayed in a desaturated, frosted glass style (`Surface` with low alpha) with a lock icon.



---

