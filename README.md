# Komikta Android

Project Android Kotlin + Jetpack Compose for Komikta. Root project files must be uploaded directly to the GitHub repository (do not upload an extra nested `Komikta/Komikta` folder).

## Build APK with GitHub Actions

1. Upload/commit `settings.gradle.kts`, `build.gradle.kts`, `app/`, and `.github/workflows/build.yml` at repository root.
2. Open **Actions → Build Komikta APK → Run workflow**.
3. When the run is green, download the `Komikta-debug-APK` artifact.

## Image handling updates

- Covers accept common API field aliases (`coverUrl`, `thumbnail`, `imageUrl`, etc.).
- Absolute HTTP image URLs are upgraded to HTTPS; protocol-relative and server-relative URLs are normalized.
- Cover images try the proxy/primary URL first, then the raw/original URL. A visible placeholder appears if both fail.
- Reader pages try both primary and raw URLs and display a fallback instead of a blank area.
- Explore loads the default category on entry, and detail/reader retry buttons trigger a real request again.

## Important verification note

The APK is produced by GitHub Actions and is not included in this ZIP. This project has not been verified by a completed Android build in this environment. If Actions fails, use the first Kotlin/Gradle error in the run log to identify the remaining issue. Remote image availability also depends on the API and source hosts.


## UI theme update: Glass Black & White
- Removed the red accent in favor of monochrome white/black/gray.
- Added a subtle translucent charcoal surface helper with thin highlights for glass-inspired cards.
- This is a glass-inspired Compose treatment, not true background blur.
- Build and test via GitHub Actions before treating the APK as verified.
