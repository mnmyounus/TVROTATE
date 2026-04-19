# Deployment Guide - Reverse Orientation TV

Complete guide for deploying and distributing the Reverse Orientation TV application.

## 📦 Project Structure

```
ReverseOrientationTV/
├── 📱 Source Code
│   ├── app/src/main/java/com/reverselandscape/tv/
│   │   ├── MainActivity.kt           # Main UI and permission handling
│   │   ├── OrientationService.kt     # Core orientation forcing service
│   │   └── BootReceiver.kt           # Auto-start on boot
│   └── app/src/main/res/             # Resources (layouts, strings, icons)
│
├── 🔧 Configuration
│   ├── app/build.gradle.kts          # App-level Gradle configuration
│   ├── build.gradle.kts              # Project-level Gradle configuration
│   ├── settings.gradle.kts           # Gradle settings
│   ├── gradle.properties             # Gradle properties
│   └── app/proguard-rules.pro        # ProGuard rules for release builds
│
├── 🤖 CI/CD
│   └── .github/workflows/android.yml # GitHub Actions workflow
│
├── 📚 Documentation
│   ├── README.md                     # Main user documentation
│   ├── QUICKSTART.md                 # Quick start guide
│   ├── SETUP.md                      # Development setup
│   ├── CONTRIBUTING.md               # Contribution guidelines
│   ├── PROJECT_SUMMARY.md            # Technical overview
│   └── DEPLOYMENT.md                 # This file
│
└── 🛠️ Utilities
    ├── verify_build.sh               # Build verification script
    ├── create_icons.sh               # Icon generation helper
    ├── gradlew / gradlew.bat         # Gradle wrapper scripts
    ├── .gitignore                    # Git ignore rules
    └── LICENSE                       # MIT License
```

## 🚀 Build Process

### Local Development Build

#### Debug Build (Fastest)
```bash
./gradlew assembleDebug
```
**Output**: `app/build/outputs/apk/debug/app-debug.apk`
- Includes debug symbols
- Not optimized
- Debuggable via ADB
- Suitable for testing

#### Release Build (Production)
```bash
./gradlew assembleRelease
```
**Output**: `app/build/outputs/apk/release/app-release-unsigned.apk`
- Optimized with ProGuard
- Smaller size
- Better performance
- **Needs signing for distribution**

### Signing Release APK

#### Generate Keystore (One-time)
```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias my-key-alias
```

Save the keystore file and remember:
- Keystore password
- Key alias
- Key password

#### Sign the APK
```bash
# Build unsigned release
./gradlew assembleRelease

# Sign the APK
jarsigner -verbose \
  -sigalg SHA256withRSA \
  -digestalg SHA-256 \
  -keystore my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  my-key-alias

# Align the APK
zipalign -v 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/app-release.apk
```

**Final output**: `app/build/outputs/apk/release/app-release.apk`

## 🌐 GitHub Deployment

### Automated CI/CD with GitHub Actions

The project includes a complete GitHub Actions workflow that automatically:
1. Builds both debug and release APKs on every push
2. Uploads artifacts for download
3. Creates GitHub releases with version tagging

### Setup Steps

#### 1. Push to GitHub
```bash
# Create repository on GitHub
# Then push your code
git init
git add .
git commit -m "Initial commit: Reverse Orientation TV"
git branch -M main
git remote add origin https://github.com/yourusername/ReverseOrientationTV.git
git push -u origin main
```

#### 2. Automatic Build Triggers
- **Push to main/master**: Triggers build and creates release
- **Pull Requests**: Builds APK for testing
- **Manual**: Workflow can be triggered manually from Actions tab

#### 3. Access Build Artifacts
After each build:
1. Go to **Actions** tab on GitHub
2. Click on the latest workflow run
3. Download **app-debug** or **app-release** artifacts
4. Extract the APK file

#### 4. Releases
The workflow automatically creates releases:
- **Tag**: `v1.0.<run_number>`
- **Assets**: Both debug and release APKs
- **Location**: `https://github.com/yourusername/ReverseOrientationTV/releases`

## 📱 Distribution Methods

### Method 1: GitHub Releases (Recommended)
**Pros**: Free, version control, automatic builds
**Cons**: Requires GitHub account

1. Users navigate to Releases page
2. Download latest APK
3. Install on device

### Method 2: Direct APK Distribution
**Pros**: Simple, no account needed
**Cons**: Manual updates

1. Build signed release APK
2. Host on your server/website
3. Provide download link
4. Users download and install

### Method 3: F-Droid
**Pros**: Open-source app store, auto-updates
**Cons**: Requires F-Droid submission process

1. Submit to F-Droid repository
2. Wait for review and approval
3. Users install via F-Droid app

### Method 4: Google Play Store
**Pros**: Wide reach, auto-updates, professional
**Cons**: $25 one-time fee, review process, requires AAB format

Not recommended for this project due to:
- Small target audience (Android TV orientation forcing)
- Play Store policies on system-level apps
- Better suited for open-source distribution

## 🔐 Security Considerations

### Code Signing
- **Always sign release APKs** for distribution
- **Keep keystore secure** - store in password manager
- **Never commit keystore** to version control
- **Backup keystore** - loss means inability to update app

### Build Verification
Before distributing:
```bash
# Verify APK signature
jarsigner -verify -verbose -certs app-release.apk

# Check APK contents
unzip -l app-release.apk

# Verify no debug symbols
apkanalyzer dex packages app-release.apk
```

## 📊 Version Management

### Versioning Strategy
Follow Semantic Versioning: `MAJOR.MINOR.PATCH`

- **MAJOR**: Breaking changes, major features
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, minor improvements

### Update Version
Edit `app/build.gradle.kts`:
```kotlin
defaultConfig {
    versionCode = 2      // Increment for each release
    versionName = "1.1.0" // Update version string
}
```

### Changelog
Maintain `CHANGELOG.md`:
```markdown
## [1.1.0] - 2026-04-20
### Added
- New orientation modes
### Fixed
- Service crash on Android 14
```

## 📈 Release Checklist

Before each release:

- [ ] Update version in `build.gradle.kts`
- [ ] Update CHANGELOG.md
- [ ] Run all tests: `./gradlew test`
- [ ] Build release APK: `./gradlew assembleRelease`
- [ ] Sign APK with keystore
- [ ] Test on multiple Android versions (10, 12, 14)
- [ ] Test auto-start on boot
- [ ] Test service persistence
- [ ] Verify permissions work correctly
- [ ] Create git tag: `git tag v1.x.x`
- [ ] Push tag: `git push origin v1.x.x`
- [ ] GitHub Actions creates release automatically
- [ ] Update release notes on GitHub
- [ ] Announce release (if applicable)

## 🎯 Distribution URLs

After deployment, share these URLs:

- **Releases**: `https://github.com/yourusername/ReverseOrientationTV/releases`
- **Latest APK**: `https://github.com/yourusername/ReverseOrientationTV/releases/latest`
- **Source Code**: `https://github.com/yourusername/ReverseOrientationTV`
- **Issues**: `https://github.com/yourusername/ReverseOrientationTV/issues`

## 🛡️ Privacy & Compliance

### Privacy Policy
Since the app:
- Collects **no data**
- Makes **no network requests**
- Has **no analytics**

A privacy policy would state:
> "Reverse Orientation TV does not collect, store, or transmit any user data. The app operates entirely offline and requires no network connection."

### Open Source License
- **License**: MIT
- **Commercial Use**: Allowed
- **Modification**: Allowed
- **Distribution**: Allowed
- **Liability**: None (use at own risk)

## 📞 Support & Maintenance

### User Support Channels
1. **GitHub Issues**: Bug reports and feature requests
2. **Documentation**: README, QUICKSTART, SETUP guides
3. **Community**: GitHub Discussions (if enabled)

### Maintenance Schedule
- **Bug Fixes**: Within 1-2 weeks of report
- **Security Issues**: Immediate (within 24-48 hours)
- **Feature Requests**: Evaluated and prioritized
- **Android Updates**: Test within 1 month of new Android release

## 🔄 Update Process for Users

Users get updates via:

1. **Manual Download**: Check Releases page for new version
2. **Notification**: GitHub Watch → Releases
3. **RSS Feed**: GitHub releases have RSS feed
4. **Automated** (future): In-app update checker (optional feature)

---

## 📝 Final Notes

- Keep keystore backed up securely
- Test on real devices, not just emulators
- Monitor GitHub Issues for user feedback
- Consider adding crash reporting (respecting privacy)
- Document all breaking changes clearly
- Maintain backward compatibility when possible

**Questions?** See [CONTRIBUTING.md](CONTRIBUTING.md) or open an issue.

---

**Happy Deploying!** 🚀
