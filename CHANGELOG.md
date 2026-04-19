# Changelog

All notable changes to Reverse Orientation TV will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Additional orientation modes (portrait, landscape)
- Quick Settings tile
- Per-app orientation profiles
- Battery optimization improvements

---

## [1.0.0] - 2026-04-18

### Added
- Initial release
- Foreground Service with START_STICKY for persistence
- WindowManager overlay for system-wide orientation forcing
- Reverse landscape orientation mode
- Auto-start on boot via BroadcastReceiver
- High-priority notification channel
- Material Design 3 UI
- Permission handling for SYSTEM_ALERT_WINDOW
- Notification permission support (Android 13+)
- Service status monitoring
- Start/Stop controls
- Android 10-14+ compatibility
- Complete documentation suite
- GitHub Actions CI/CD workflow
- ProGuard optimization for release builds
- MIT License

### Features
- **Persistent Service**: Survives app switching and video playback
- **Auto-Start**: Automatically starts on device boot
- **Privacy-First**: Zero data collection, no network access
- **Lightweight**: <10MB memory, negligible battery impact
- **Modern UI**: Material Design 3 with proper theming

### Technical
- Pure Kotlin implementation
- Zero external dependencies (Android SDK only)
- Gradle 8.2 with Kotlin DSL
- Android Gradle Plugin 8.2.0
- Kotlin 1.9.20
- AndroidX libraries only
- Supports API 29-34

### Documentation
- README.md - Main user documentation
- QUICKSTART.md - 5-minute setup guide
- SETUP.md - Development environment setup
- CONTRIBUTING.md - Contribution guidelines
- PROJECT_SUMMARY.md - Technical architecture
- DEPLOYMENT.md - Release and distribution guide
- LICENSE - MIT open source license

---

## Version History

### Version Numbering
- **versionCode**: Incremental integer (1, 2, 3, ...)
- **versionName**: Semantic versioning (1.0.0, 1.1.0, 2.0.0)

### Release Types
- **Major (x.0.0)**: Breaking changes, major rewrites
- **Minor (1.x.0)**: New features, backward compatible
- **Patch (1.0.x)**: Bug fixes, minor improvements

---

## How to Update This File

When making changes:

1. **Add to [Unreleased]** section as you develop
2. **On release**: Move [Unreleased] items to new version section
3. **Include**: Date, version number, and categorized changes
4. **Categories**: Added, Changed, Deprecated, Removed, Fixed, Security

### Example Entry
```markdown
## [1.1.0] - 2026-05-15

### Added
- Portrait orientation mode
- Quick Settings tile for easy toggle

### Fixed
- Service crash on Android 15 beta
- Memory leak in overlay creation

### Changed
- Updated notification icon
- Improved permission request flow
```

---

## Links

- [Repository](https://github.com/yourusername/ReverseOrientationTV)
- [Releases](https://github.com/yourusername/ReverseOrientationTV/releases)
- [Issues](https://github.com/yourusername/ReverseOrientationTV/issues)
- [Contributing](CONTRIBUTING.md)

---

**Note**: This changelog is maintained manually. For detailed commit history, see the Git log.
