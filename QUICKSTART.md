# Quick Start Guide - Reverse Orientation TV

Get the app running in under 5 minutes!

## 🚀 For Users (Install APK)

### Option 1: Download Pre-built APK
1. Go to [Releases](https://github.com/yourusername/ReverseOrientationTV/releases)
2. Download `app-debug.apk` or `app-release.apk`
3. Transfer to your Android TV via USB or network
4. Install the APK (enable "Unknown Sources" if needed)
5. Open the app, grant permissions, and tap "Start Service"

**Done!** Your Android TV is now in reverse landscape mode.

---

## 🛠️ For Developers (Build from Source)

### Prerequisites
- Android Studio (latest version)
- JDK 17+
- Git

### Steps

#### 1. Clone & Open
```bash
git clone https://github.com/yourusername/ReverseOrientationTV.git
cd ReverseOrientationTV
```

Open in Android Studio:
- File → Open → Select the `ReverseOrientationTV` folder
- Wait for Gradle sync

#### 2. Build APK (Command Line)
```bash
# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

**Windows:**
```bash
gradlew.bat assembleDebug
```

#### 3. Build APK (Android Studio)
1. Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Wait for build to complete
3. Click "locate" in the notification to find the APK

#### 4. Install on Device
```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or from Android Studio
# Click the green "Run" button (▶️)
```

---

## 📱 First-Time Setup on Device

1. **Open the app** from your launcher
2. **Grant Overlay Permission**:
   - Tap "Grant Permission"
   - Enable "Display over other apps"
   - Press back to return to app
3. **Grant Notification Permission** (Android 13+):
   - Allow when prompted
4. **Start Service**:
   - Tap "Start Service"
   - Status should show "Running"
5. **Test**:
   - Open YouTube or another app
   - Screen should remain in reverse landscape

---

## ✅ Verify Installation

Run the verification script:
```bash
./verify_build.sh
```

This checks that all required files are present.

---

## 🔧 Common Issues

### "Gradle sync failed"
- **Solution**: File → Invalidate Caches → Invalidate and Restart

### "SDK not found"
- **Solution**: Tools → SDK Manager → Install API 29 and 34

### "Permission denied: ./gradlew"
- **Solution**: `chmod +x gradlew`

### "Service stops during video playback"
- **Solution**: Disable battery optimization for the app
- Settings → Apps → Reverse Orientation TV → Battery → Unrestricted

---

## 📚 More Information

- **Full Documentation**: [README.md](README.md)
- **Setup Guide**: [SETUP.md](SETUP.md)
- **Contributing**: [CONTRIBUTING.md](CONTRIBUTING.md)
- **Technical Details**: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)

---

## 🎯 Quick Commands Reference

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Clean project
./gradlew clean

# Run tests
./gradlew test

# Check for updates
./gradlew --refresh-dependencies

# Verify project structure
./verify_build.sh
```

---

## 🆘 Need Help?

1. Check existing [GitHub Issues](https://github.com/yourusername/ReverseOrientationTV/issues)
2. Read the [FAQ in README](README.md)
3. Open a new issue with:
   - Android version
   - Device model
   - Error logs (`adb logcat`)
   - Steps to reproduce

---

**That's it! You're ready to go!** 🎉
