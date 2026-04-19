# Contributing to Reverse Orientation TV

Thank you for your interest in contributing! This document provides guidelines for contributing to this project.

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help maintain a welcoming environment

## How to Contribute

### Reporting Bugs

Before creating a bug report:
1. Check existing issues to avoid duplicates
2. Test on the latest version
3. Verify it's not a device-specific issue

When filing a bug report, include:
- Android version and device model
- Steps to reproduce
- Expected vs actual behavior
- Logcat output if available
- Screenshots/screen recordings if helpful

### Suggesting Features

Feature requests should:
- Align with the project's core purpose (orientation forcing)
- Be technically feasible on Android
- Consider privacy and performance implications
- Include use cases and benefits

### Pull Requests

1. **Fork** the repository
2. **Create a branch**: `git checkout -b feature/your-feature-name`
3. **Make your changes** following the code guidelines below
4. **Test thoroughly** on multiple Android versions
5. **Commit** with clear messages
6. **Push** to your fork
7. **Open a Pull Request** with a detailed description

## Code Guidelines

### Kotlin Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions focused and concise

### Android Best Practices
- Use AndroidX libraries
- Follow Material Design guidelines
- Handle configuration changes properly
- Implement proper error handling
- Test on multiple API levels (29-34)

### Code Example
```kotlin
// Good: Clear, documented, handles edge cases
private fun createOrientationOverlay() {
    try {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Create invisible overlay to force orientation
        overlayView = View(this)
        
        val layoutParams = WindowManager.LayoutParams(
            0, 0, // Invisible dimensions
            TYPE_APPLICATION_OVERLAY,
            FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            screenOrientation = SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }
        
        windowManager?.addView(overlayView, layoutParams)
    } catch (e: Exception) {
        Log.e(TAG, "Failed to create overlay", e)
    }
}
```

### Testing Requirements
- Test on Android 10, 12, and 14 minimum
- Verify service persistence during:
  - App switching
  - Video playback (YouTube, Netflix)
  - Device rotation
  - Low memory conditions
- Test auto-start after reboot
- Verify all permissions work correctly

## Project Structure

```
app/src/main/
├── java/com/reverselandscape/tv/
│   ├── MainActivity.kt          # UI and permission handling
│   ├── OrientationService.kt    # Core service logic
│   └── BootReceiver.kt          # Auto-start receiver
├── res/
│   ├── layout/                  # UI layouts
│   ├── values/                  # Strings, colors, themes
│   └── xml/                     # Data extraction rules
└── AndroidManifest.xml          # Permissions and components
```

## Development Workflow

### Setting Up Development Environment
```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/ReverseOrientationTV.git
cd ReverseOrientationTV

# Add upstream remote
git remote add upstream https://github.com/ORIGINAL/ReverseOrientationTV.git

# Create feature branch
git checkout -b feature/my-feature
```

### Making Changes
```bash
# Make your changes
# Test thoroughly

# Stage changes
git add .

# Commit with descriptive message
git commit -m "Add feature: detailed description"

# Push to your fork
git push origin feature/my-feature
```

### Syncing with Upstream
```bash
# Fetch upstream changes
git fetch upstream

# Merge into your branch
git checkout main
git merge upstream/main

# Update your fork
git push origin main
```

## Commit Message Guidelines

Use clear, descriptive commit messages:

```
# Good
Add notification priority configuration for Android 8+
Fix service crash when overlay permission is revoked
Update README with troubleshooting section

# Bad
Fix bug
Update code
Changes
```

## Code Review Process

1. Maintainers review PRs within 1-2 weeks
2. Address feedback with additional commits
3. Once approved, PR will be merged
4. Delete your feature branch after merge

## What We're Looking For

**High Priority:**
- Bug fixes
- Performance improvements
- Compatibility with new Android versions
- Documentation improvements
- Test coverage

**Medium Priority:**
- UI/UX enhancements
- Additional orientation options
- Battery optimization

**Low Priority:**
- Code refactoring (unless improving readability significantly)
- Style changes without functional benefit

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

## Questions?

- Open a GitHub Issue for questions
- Check existing issues and PRs
- Read the [SETUP.md](SETUP.md) for development setup

---

Thank you for contributing! 🎉
