# Project Setup Guide

## Prerequisites

1. **Android Studio** (Latest stable version recommended)
   - Download from: https://developer.android.com/studio

2. **JDK 17** (Required for Android Studio Hedgehog+)
   - Usually bundled with Android Studio

3. **Android SDK**
   - API Level 29 (Android 10) minimum
   - API Level 34 (Android 14) target
   - Install via Android Studio SDK Manager

## Initial Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ReverseOrientationTV.git
cd ReverseOrientationTV
```

### 2. Generate Gradle Wrapper (If Missing)
If the `gradle/wrapper/gradle-wrapper.jar` file is missing:

**Option A: Using Android Studio**
- Open the project in Android Studio
- Android Studio will automatically download the Gradle wrapper

**Option B: Using Command Line (if Gradle is installed)**
```bash
gradle wrapper --gradle-version 8.2
```

**Option C: Manual Download**
```bash
# Download Gradle distribution
wget https://services.gradle.org/distributions/gradle-8.2-bin.zip

# Extract and copy wrapper jar
unzip gradle-8.2-bin.zip
cp gradle-8.2/lib/plugins/gradle-wrapper-*.jar gradle/wrapper/gradle-wrapper.jar
```

### 3. Open in Android Studio
1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the `ReverseOrientationTV` folder
4. Click "OK"
5. Wait for Gradle sync to complete

### 4. Configure Android Device/Emulator

**For Android TV Emulator:**
1. Open AVD Manager in Android Studio
2. Create New Virtual Device
3. Select "TV" category
4. Choose a TV device (e.g., "Android TV 1080p")
5. Select System Image (API 29 or higher)
6. Finish setup

**For Physical Android TV:**
1. Enable Developer Options on your TV
2. Enable USB Debugging
3. Connect via USB or ADB over network

## Building the Project

### Debug Build
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Install on Connected Device
```bash
./gradlew installDebug
```

## Running from Android Studio

1. Select your target device from the device dropdown
2. Click the "Run" button (green play icon) or press `Shift+F10`
3. The app will install and launch automatically

## Troubleshooting

### Gradle Sync Failed
- Ensure you have a stable internet connection
- Try: File → Invalidate Caches → Invalidate and Restart
- Check that JDK 17 is properly configured

### SDK Not Found
- Open SDK Manager: Tools → SDK Manager
- Install API Level 29 and 34
- Install Android SDK Build-Tools 34.0.0

### Gradle Wrapper Missing
- Follow step 2 in Initial Setup above
- Or let Android Studio download it automatically

### Build Fails
- Clean the project: Build → Clean Project
- Rebuild: Build → Rebuild Project
- Check the Build output tab for specific errors

## Testing on Android TV

1. **Install the APK** on your Android TV
2. **Grant Permissions**:
   - Open the app
   - When prompted, grant "Display over other apps" permission
   - Grant notification permission (Android 13+)
3. **Start the Service**:
   - Tap "Start Service"
   - Check that status shows "Running"
4. **Test Persistence**:
   - Open YouTube or another app
   - Verify orientation remains in reverse landscape
5. **Test Auto-Start**:
   - Reboot the device
   - Service should start automatically

## Development Tips

### Enable Logging
Add to `gradle.properties`:
```properties
android.enableJetifier=true
android.useAndroidX=true
```

### View Logs
```bash
adb logcat | grep ReverseOrientationTV
```

### Debug Service
1. Run → Debug 'app'
2. Set breakpoints in `OrientationService.kt`
3. Attach debugger when service starts

## Next Steps

- Read the [README.md](README.md) for feature details
- Check [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines
- Review the code documentation in source files

## Support

If you encounter issues not covered here:
1. Check existing GitHub Issues
2. Search Android Studio documentation
3. Open a new issue with full error logs
