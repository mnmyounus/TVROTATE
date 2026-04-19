# Reverse Orientation TV

A production-ready Android application that forces system-wide **Reverse Landscape** orientation on Android TV devices. Built with Kotlin, this app ensures persistent orientation even while watching YouTube or using other applications.

## 🎯 Features

- **System-Wide Orientation Forcing**: Uses WindowManager overlay to enforce reverse landscape orientation across all apps
- **Persistent Foreground Service**: Implements `START_STICKY` to survive system kills
- **High-Priority Notifications**: Ensures service remains active even during video playback
- **Auto-Start on Boot**: Automatically launches on device startup via `BroadcastReceiver`
- **No External Dependencies**: Pure Kotlin/Android SDK - no tracking, ads, or analytics
- **Android TV Optimized**: Designed specifically for Android TV devices
- **Privacy-First**: Zero data collection or network requests

## 📋 Requirements

- **Android Version**: Android 10 (API 29) through Android 14+ (API 34)
- **Target Device**: Android TV, but compatible with all Android devices
- **Permissions Required**:
  - `SYSTEM_ALERT_WINDOW` - For overlay window
  - `FOREGROUND_SERVICE` - For persistent service
  - `RECEIVE_BOOT_COMPLETED` - For auto-start
  - `POST_NOTIFICATIONS` (Android 13+) - For foreground service notifications

## 🚀 Installation

### Option 1: Download Pre-built APK (Recommended)
1. Go to [Releases](../../releases)
2. Download the latest `app-release.apk` or `app-debug.apk`
3. Transfer to your Android TV
4. Enable "Install from Unknown Sources" in Settings
5. Install the APK

### Option 2: Build from Source
```bash
# Clone the repository
git clone https://github.com/yourusername/ReverseOrientationTV.git
cd ReverseOrientationTV

# Build the APK
./gradlew assembleRelease

# APK will be at: app/build/outputs/apk/release/app-release-unsigned.apk
```

## 🔧 Setup & Usage

1. **Launch the App**: Open "Reverse Landscape TV" from your app drawer
2. **Grant Permissions**: 
   - The app will request overlay permission
   - Tap "Grant Permission" and enable in Settings
   - Grant notification permission if prompted (Android 13+)
3. **Start Service**: Tap "Start Service" button
4. **Verify**: Check that status shows "Status: Running"
5. **Done**: The orientation is now forced to reverse landscape system-wide

### Auto-Start Configuration
The service automatically starts on boot if overlay permission is granted. No additional configuration needed.

## 🏗️ Architecture

### Core Components

#### 1. **OrientationService** (`OrientationService.kt`)
- Foreground service with `START_STICKY` flag
- Creates invisible 0x0 WindowManager overlay
- Sets `screenOrientation = SCREEN_ORIENTATION_REVERSE_LANDSCAPE`
- High-priority notification channel for Android 8.0+

#### 2. **MainActivity** (`MainActivity.kt`)
- Permission request handling
- Service lifecycle management
- Status monitoring UI
- Support for Android 13+ notification permissions

#### 3. **BootReceiver** (`BootReceiver.kt`)
- Listens for `BOOT_COMPLETED` and `QUICKBOOT_POWERON`
- Auto-starts service on device boot
- Checks for overlay permission before starting

### Technical Implementation

```kotlin
// Key overlay creation logic
val layoutParams = WindowManager.LayoutParams(
    0, // Width: 0 pixels (invisible)
    0, // Height: 0 pixels (invisible)
    TYPE_APPLICATION_OVERLAY, // Overlay type
    FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCHABLE, // Non-interactive
    PixelFormat.TRANSLUCENT
).apply {
    screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
}
windowManager?.addView(overlayView, layoutParams)
```

## 🔐 Permissions Explained

| Permission | Purpose | Required |
|------------|---------|----------|
| `SYSTEM_ALERT_WINDOW` | Create overlay window to force orientation | ✅ Yes |
| `FOREGROUND_SERVICE` | Run persistent background service | ✅ Yes |
| `RECEIVE_BOOT_COMPLETED` | Auto-start on device boot | ✅ Yes |
| `POST_NOTIFICATIONS` | Show foreground service notification (Android 13+) | ✅ Yes (API 33+) |

## 🛠️ Development

### Project Structure
```
ReverseOrientationTV/
├── app/
│   ├── src/main/
│   │   ├── java/com/reverselandscape/tv/
│   │   │   ├── MainActivity.kt
│   │   │   ├── OrientationService.kt
│   │   │   └── BootReceiver.kt
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml
│   │   │   ├── values/strings.xml
│   │   │   └── values/themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── .github/workflows/android.yml
├── build.gradle.kts
└── settings.gradle.kts
```

### Building Locally
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## 🤖 GitHub Actions CI/CD

The project includes automated APK building on every push:

- **Workflow File**: `.github/workflows/android.yml`
- **Triggers**: Push to `main`/`master`, Pull Requests, Manual dispatch
- **Outputs**: 
  - Debug APK
  - Release APK (unsigned)
  - Automatic GitHub Releases with version tagging

### Artifacts
Each build produces:
1. `app-debug.apk` - Debug build with debugging symbols
2. `app-release-unsigned.apk` - Release build (needs signing for production)

## 🐛 Troubleshooting

### Service Stops During Video Playback
- Ensure notification permission is granted (Android 13+)
- Check that "Battery Optimization" is disabled for the app
- Verify overlay permission is still granted

### Auto-Start Not Working
- Confirm `RECEIVE_BOOT_COMPLETED` permission is granted
- Check that overlay permission was granted before reboot
- Some devices require manual exemption from battery optimization

### Orientation Not Changing
- Verify overlay permission is granted
- Ensure service status shows "Running"
- Check for conflicting apps that force orientation
- Try restarting the service

## 📱 Compatibility

| Android Version | API Level | Status |
|----------------|-----------|--------|
| Android 10 | 29 | ✅ Fully Supported |
| Android 11 | 30 | ✅ Fully Supported |
| Android 12 | 31 | ✅ Fully Supported |
| Android 13 | 33 | ✅ Fully Supported (requires notification permission) |
| Android 14 | 34 | ✅ Fully Supported |

## 🔒 Privacy & Security

- **Zero Network Requests**: No internet permission requested
- **No Data Collection**: No analytics, tracking, or telemetry
- **No Third-Party Libraries**: Pure Android SDK
- **Open Source**: Fully auditable code
- **No Ads**: Completely ad-free

## 📄 License

This project is released under the MIT License. See [LICENSE](LICENSE) for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## ⚠️ Disclaimer

This app modifies system orientation behavior. Use at your own risk. The developers are not responsible for any issues arising from the use of this application.

## 📞 Support

For issues, questions, or feature requests, please open an issue on GitHub.

---

**Made with ❤️ for Android TV users who need reverse landscape orientation**
