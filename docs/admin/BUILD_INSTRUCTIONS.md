# Build and Deployment Instructions

## Environment Setup

1. **Prerequisites**
   - Android Studio Iguana or later.
   - JDK 17.
   - Firebase Project with Firestore and Authentication enabled.

2. **Firebase Configuration**
   - Obtain the `google-services.json` file from your Firebase Console.
   - Place it in the `admin-app/` root directory (and `app/` directory for the user app).
   - **Important**: Ensure the package name `com.smartsorovnoma.admin` is registered in the Firebase Console.
   - Add the SHA-1 Debug and Release keystore fingerprints to the Firebase Console Project Settings.

## Building the Application

### Debug Build
Run the following command in the terminal:
```bash
./gradlew :admin-app:assembleDebug
```
The APK will be located at `admin-app/build/outputs/apk/debug/admin-app-debug.apk`.

### Release Build
1. **Keystore Setup**:
   - Create a release keystore if you haven't already.
   - Configure signing in `admin-app/build.gradle.kts` (currently set to default/debug for simplicity, update `signingConfigs` for production).

2. **Build Command**:
   ```bash
   ./gradlew :admin-app:bundleRelease
   ```
   The AAB will be located at `admin-app/build/outputs/bundle/release/admin-app-release.aab`.

## Dependency Management
- The project uses Gradle Version Catalogs (implied) or standard dependencies in `build.gradle.kts`.
- Sync Gradle after any changes: `File > Sync Project with Gradle Files`.

## Troubleshooting
- **Gradle Sync Failed**: Check internet connection and JDK version (JDK 17 required).
- **Google Sign-In 12500 Error**: This usually means the SHA-1 fingerprint is missing in Firebase Console. Add your debug/release keystore SHA-1.
