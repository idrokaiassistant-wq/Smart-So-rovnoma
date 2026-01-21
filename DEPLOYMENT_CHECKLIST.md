# Deployment Checklist & Workflow (v1.0.0)

## 🔄 Internal Testing Workflow (Google Play)

This workflow describes how to build, sign, and upload artifacts for Internal Testing.

### 1. Build Signed Bundle (.aab)
**Prerequisites:**
*   `release.keystore` file (securely stored, NOT in git)
*   `local.properties` with keystore credentials (storePassword, keyPassword, keyAlias, storeFile)

**Command:**
```bash
# Generate signed bundle
./gradlew :app:bundleRelease
```
*Output:* `app/build/outputs/bundle/release/app-release.aab`

### 2. Upload to Google Play Console
1.  Go to **Release** > **Testing** > **Internal testing**.
2.  Click **Create new release**.
3.  Upload the `app-release.aab`.
4.  **Release Name:** `1.0.0` (or auto-filled).
5.  **Release Notes:** Copy from `RELEASE_NOTES_v1.0.0.md`.
6.  Click **Next** -> **Start Rollout to Internal Testing**.

### 3. Verify & Promote
1.  Add testers email list in Console.
2.  Testers download app via link.
3.  Verify:
    *   No crashes on launch.
    *   Surveys load from Firestore.
    *   Anti-spam cooldown works (60s).
    *   Privacy Policy link opens.
4.  **Promote to Production:**
    *   Go to **Internal testing** release.
    *   Click **Promote release** -> **Production**.
    *   Review & Rollout.

---

## ✅ v1.0.0 Compliance Checklist
- [x] **No Ads:** `build.gradle.kts` clean.
- [x] **No Analytics:** No Firebase Analytics/Crashlytics SDKs.
- [x] **No Login:** Auth disabled in app.
- [x] **Permissions:** Only `INTERNET` & `ACCESS_NETWORK_STATE`.
- [x] **Privacy Policy:** URL reachable from Settings.
- [x] **Data Safety:** Form matches actual usage (User Content -> Collected -> App Functionality).

## 🚀 CI/CD Pipeline
*   **Trigger:** Push to `main` or PRs.
*   **Checks:**
    *   JDK 17 setup.
    *   `./gradlew testDebugUnitTest` (Unit Tests).
    *   `./gradlew :app:assembleRelease` (Build Check).
    *   `npm run build` (Admin Panel).

## ⚠️ Admin Panel Deployment
*   **Platform:** Vercel (Recommended) or Firebase Hosting.
*   **Env Vars:** Ensure `NEXT_PUBLIC_FIREBASE_...` are set in deployment dashboard.
*   **Build:** `cd admin-panel && npm install && npm run build`.

## 🔄 Rollback Plan
If critical bug found in Production:
1.  **Fix:** Create `hotfix/v1.0.x` branch from `main`.
2.  **Patch:** Fix code, bump version code in `build.gradle.kts`.
3.  **Release:** Tag `v1.0.1`, build bundle, upload to Production as update.
