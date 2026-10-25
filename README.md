# Komikta Android

Project Android Kotlin + Jetpack Compose for Komikta.

## GitHub Actions build

Repository root must contain `settings.gradle.kts`, `build.gradle.kts`, `app/`, and `.github/workflows/build.yml`. Upload the *contents* of this folder to the repository root, not an extra nested `Komikta/Komikta/` directory.

Open **Actions → Build Komikta APK → Run workflow**. On success, download the `Komikta-debug-APK` artifact. The workflow uploads the APK only if the build succeeds. This ZIP does not contain a prebuilt APK.
