# Google Services Setup Guide

## 📋 Overview

`google-services.json` is a required Firebase configuration file that contains credentials for connecting your Android app to Firebase. This guide explains how to set it up.

## ⚠️ Important Security Notes

- **Never commit** `google-services.json` to version control
- **Never share** this file in public channels
- Each Firebase project should have **separate** `google-services.json` for:
  - Development environment
  - Staging environment  
  - Production environment

---

## 🚀 Setup Steps

### Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **Create a new project**
3. Enter project name: `smartsorovnoma` (or similar)
4. Select your region
5. Click **Create project**

### Step 2: Add Android App to Firebase

1. In Firebase Console, click **Add app** → **Android**
2. Fill in the form:
   - **Package name**: `com.smartsorovnoma`
   - **App nickname**: SmartSorovnoma (optional)
   - **Debug signing certificate SHA-1**: (see below)
3. Click **Register app**

### Step 3: Get SHA-1 Fingerprint (Required)

**Option A: From Android Studio**
```bash
# In Android Studio:
1. Open Terminal in project root
2. Run: ./gradlew signingReport
3. Find SHA-1 hash under "debugAndroidTest" or "debug"
```

**Option B: Using keytool**
```bash
# For debug keystore:
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# For release keystore (after creating one):
keytool -list -v -keystore /path/to/your.keystore
```

### Step 4: Download google-services.json

1. After registering the app, Firebase will show **Download google-services.json**
2. Click the button to download
3. Move the file to: `app/google-services.json`

### Step 5: Enable Required Firebase Services

In Firebase Console, enable:

1. **Firestore Database**
   - Click **Firestore Database** in left menu
   - Click **Create database**
   - Select **Start in production mode** (or test mode for development)
   - Choose region (e.g., `asia-south1` for Central Asia)
   - Click **Create**

2. **Security Rules** (optional but recommended)
   - See [`backend/firestore.rules`](../backend/firestore.rules)
   - Deploy rules: `firebase deploy --only firestore:rules`

3. **Authentication** (for admin-app only)
   - Click **Authentication** in left menu
   - Click **Get started**
   - Enable **Email/Password** provider
   - (Optional) Enable **Google Sign-in**

---

## 📁 File Location

```
SmartSorovnoma/
├── app/
│   └── google-services.json ← Place file here
├── admin-app/
│   └── (uses .env.local instead)
└── .gitignore ← Already configured to ignore
```

---

## ✅ Verification

After placing `google-services.json`:

1. **Gradle Sync**
   ```bash
   ./gradlew clean
   ./gradlew sync
   ```

2. **Build Debug APK**
   ```bash
   ./gradlew :app:assembleDebug
   ```

3. **Check for Errors**
   - If you see "google-services.json not found" → Check file location
   - If you see "Package name mismatch" → Verify package name matches

---

## 🔐 Managing Multiple Environments

### Development Build
```bash
# Use default google-services.json
./gradlew :app:assembleDebug
```

### Production Build
1. Create separate Firebase project for production
2. Download separate `google-services.json`
3. Place it temporarily or use build variants:

```gradle
// In app/build.gradle.kts
flavorDimensions += "environment"
productFlavors {
    dev {
        dimension = "environment"
    }
    prod {
        dimension = "environment"
    }
}
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| "google-services.json not found" | Verify file is in `app/` directory |
| "Package name mismatch" | Ensure package name matches Firebase Console |
| "SHA-1 mismatch" | Generate correct SHA-1 fingerprint (see Step 3) |
| "Firestore connection fails" | Check internet permission in AndroidManifest.xml |
| "Permission denied" | Check Firestore Security Rules |

---

## 📚 Additional Resources

- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Firestore Security Rules](../backend/firestore.rules)
- [README.md](../README.md) - Full project setup
- [SECURITY.md](../SECURITY.md) - Security best practices

---

## 📝 Checklist

- [ ] Firebase Console project created
- [ ] Android app registered with correct package name
- [ ] SHA-1 fingerprint obtained
- [ ] `google-services.json` downloaded and placed in `app/`
- [ ] Firestore Database created
- [ ] Security Rules deployed (if applicable)
- [ ] Gradle sync successful
- [ ] Debug APK builds without errors

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
