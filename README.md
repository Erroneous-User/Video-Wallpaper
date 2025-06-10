# Video Live Wallpaper

## Introduction

Video Live Wallpaper is a lightweight and highly efficient Android application that allows you to set your favorite videos as a dynamic live wallpaper on your device. Designed with battery and memory optimization in mind, it provides a seamless visual experience without unnecessary resource drain.

### Key Optimizations

* **Extremely Low Memory Usage:** Optimized to consume minimal RAM.
* **Small Package Size:** The core app is compact, as videos are chosen by the user and not bundled.
* **Battery-Efficient Playback:**
    * Video plays **only once** every time you unlock your phone.
    * Automatically **pauses at the last frame** upon completion.
    * **Does not play** when closing apps or when the wallpaper is not visible (e.g., when another app is in the foreground or the screen is off/locked).

## Features

* **Set Custom Videos:** Easily choose any video from your device's gallery or provide a direct file path.
* **Smart Playback Control:** Automatically plays on phone unlock and pauses after one cycle.
* **Audio Control:** Toggle video sound on/off directly from settings.
* **Hide App Icon:** Option to hide the launcher icon for a cleaner app drawer.
* **Wide Android Compatibility:** Supports Android 5.0 (Lollipop) and above.
* **Responsive Scaling:** Videos are scaled to fit with cropping, adapting well to various screen sizes.


## Getting Started

### Installation (for Users)

Currently, this app is primarily for self-building or direct distribution.

1.  **Download the APK/App Bundle:**
    * **From GitHub Releases:** [Download the latest release APK/AAB here](https://github.com/your-username/your-repo-name/releases) (link will be available after you create a release).
    * **Build Yourself:** Follow the "Building from Source" instructions below.
2.  **Install the App:**
    * If you downloaded an APK, transfer it to your Android device and tap on it to install. You might need to enable "Install from unknown sources" in your device settings.
    * If you downloaded an AAB, you'll need `bundletool` or Android Studio to install it (see "Building from Source" for `bundletool` instructions).
3.  **Set as Wallpaper:**
    * Launch the "Video Live Wallpaper" app.
    * Follow the in-app instructions to choose your video and set it as your live wallpaper.

### Usage

1.  **Choose Video:** Tap "Choose Video File" to select a video from your gallery, or "Add Video File Path" to manually enter a path.
2.  **Set Wallpaper:** The app will guide you to set the selected video as your live wallpaper.
3.  **Settings:** Tap "Settings" to access options like muting/unmuting the video sound or hiding the app icon.

## Building from Source (for Developers)

To build this application from its source code, you'll need Android Studio and a compatible Java Development Kit (JDK).

### Prerequisites

* Android Studio (latest stable version recommended)
* JDK 11 or higher (Android Studio often bundles one)
* An Android device or emulator for testing

### Steps to Build

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Erroneous-User/Video-Wallpaper-app.git
    cd video-live-wallpaper
    ```

2.  **Open in Android Studio:**
    * Launch Android Studio.
    * Select `Open an existing Android Studio project`.
    * Navigate to the cloned `video-live-wallpaper` directory and click `OK`.

3.  **Sync Gradle:**
    * Android Studio will automatically sync Gradle. If not, click `File` > `Sync Project with Gradle Files`.

4.  **Configure Signing (for Release Builds):**
    * For `release` builds (e.g., when generating an APK or AAB for distribution), ensure your `app/build.gradle` has the correct `signingConfigs` pointing to your release keystore. Refer to previous instructions if you need to set this up.

5.  **Build the App:**
    * To build a **debug APK** for quick testing: `Build` > `Build Bundles / APKs` > `Build APKs`.
    * To build a **release Android App Bundle (`.aab`)** for publishing: `Build` > `Generate Signed Bundle / APK...` > Select `Android App Bundle` > Follow the wizard.

### Installing an App Bundle (`.aab`) for Testing

You cannot directly install an `.aab` file. You need `bundletool` to generate device-specific APKs.

1.  **Download `bundletool`:** Download the `bundletool-all-x.x.x.jar` from [bundletool GitHub Releases](https://github.com/google/bundletool/releases).

2.  **Generate APK Set (`.apks` file):**
    Open your terminal/command prompt in the directory where your `.aab` and `bundletool.jar` are located.
    ```bash
    java -jar bundletool-all-1.18.1.jar build-apks --bundle=app-release.aab --output=app.apks --mode=universal --ks=/path/to/your/release.jks --ks-pass=pass:your_keystore_password --ks-key-alias=your_key_alias --key-pass=pass:your_key_password
    ```
    * Replace `1.18.1` with your `bundletool` version.
    * Adjust paths and signing info (`--ks`, `--ks-pass`, etc.) as needed. For debug builds, you can often use `~/.android/debug.keystore` with `androiddebugkey` and `android` as passwords.

3.  **Install the APK Set:**
    Ensure your Android device is connected via USB and ADB is working.
    ```bash
    java -jar bundletool-all-1.18.1.jar install-apks --apks=app.apks
    ```

## Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue. If you'd like to contribute code, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

* Inspired by existing Android live wallpaper concepts.
* Built with Kotlin and Android Jetpack libraries.

---
