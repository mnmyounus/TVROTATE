# Gradle Wrapper JAR

The `gradle-wrapper.jar` file should be placed in this directory.

## To generate it:

If you have Gradle installed globally, run from the project root:
```bash
gradle wrapper --gradle-version 8.2
```

Or download it manually from:
https://services.gradle.org/distributions/gradle-8.2-bin.zip

Then extract `lib/plugins/gradle-wrapper-*.jar` to this directory and rename it to `gradle-wrapper.jar`.

## For GitHub Actions

The GitHub Actions workflow will automatically download and set up Gradle, so this file is not required for CI/CD builds.
