# Environment Setup Guide

**Sana**: 2026-01-22  
**Versiya**: 1.0

---

## 📋 Overview

This guide explains how to configure environment variables for the SmartSorovnoma Admin Panel.

---

## 🔐 Environment Variables Required

The admin panel requires Firebase configuration to connect to your backend:

| Variable | Description | Example |
|----------|-------------|---------|
| `NEXT_PUBLIC_FIREBASE_API_KEY` | Firebase API key | `AIzaSyAZ7LwdF...` |
| `NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN` | Firebase auth domain | `project.firebaseapp.com` |
| `NEXT_PUBLIC_FIREBASE_PROJECT_ID` | Firebase project ID | `smartsorovnoma-c7086` |
| `NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET` | Storage bucket | `project.firebasestorage.app` |
| `NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID` | Messaging sender ID | `615381500145` |
| `NEXT_PUBLIC_FIREBASE_APP_ID` | App ID | `1:123456:web:abc...` |

---

## 🚀 Quick Setup

### Step 1: Copy Template

```bash
cd admin-panel
cp .env.example .env.local
```

### Step 2: Get Firebase Credentials

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Click ⚙️ Settings (gear icon)
4. Go to **Project Settings**
5. Find "Your apps" section → Web app (or add one)
6. Copy the Firebase config values

### Step 3: Fill .env.local

Open `.env.local` and replace values:

```env
NEXT_PUBLIC_FIREBASE_API_KEY=YOUR_API_KEY
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=YOUR_PROJECT.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=YOUR_PROJECT_ID
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=YOUR_PROJECT.firebasestorage.app
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=YOUR_SENDER_ID
NEXT_PUBLIC_FIREBASE_APP_ID=YOUR_APP_ID
```

### Step 4: Verify

```bash
npm install
npm run dev
```

If all variables are set correctly, app starts without errors ✅

---

## ⚠️ Important Notes

### Do Not Commit .env.local

⚠️ **NEVER commit `.env.local` to version control!**

The file `.env.local` is already in `.gitignore`:

```gitignore
.env.local
.env
!.env.example
```

This prevents accidentally exposing credentials.

### Use .env.example Instead

The `.env.example` file is safe to commit:

```env
# .env.example - Template file (SAFE to commit)
NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key-here
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
...
```

Use this as template for new developers or deployments.

### Public vs Private Variables

All Firebase credentials in this app are marked `NEXT_PUBLIC_`:

- ✅ OK to be in frontend (Firebase security is managed by Firestore Rules)
- ❌ Do NOT include sensitive data like API tokens or database URLs

---

## 🔍 Find Your Firebase Credentials

### Option 1: Firebase Console

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select project
3. Click ⚙️ → Project Settings
4. Scroll to "Your apps"
5. Click your web app
6. Find "Firebase SDK snippet" → "Config"

**Example config**:
```javascript
const firebaseConfig = {
  apiKey: "AIzaSyAZ7LwdF3X2YtX_43RgkemxKkLRN2zR7lI",
  authDomain: "smartsorovnoma-c7086.firebaseapp.com",
  projectId: "smartsorovnoma-c7086",
  storageBucket: "smartsorovnoma-c7086.firebasestorage.app",
  messagingSenderId: "615381500145",
  appId: "1:615381500145:web:e96fe1d931fb8443f22950"
};
```

Map to `.env.local`:
```env
NEXT_PUBLIC_FIREBASE_API_KEY=AIzaSyAZ7LwdF3X2YtX_43RgkemxKkLRN2zR7lI
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=smartsorovnoma-c7086.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=smartsorovnoma-c7086
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=smartsorovnoma-c7086.firebasestorage.app
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=615381500145
NEXT_PUBLIC_FIREBASE_APP_ID=1:615381500145:web:e96fe1d931fb8443f22950
```

### Option 2: Firebase CLI

```bash
firebase apps:list
firebase apps:sdkconfig WEB smartsorovnoma-c7086
```

---

## ✅ Verify Setup

### Check Variables Are Loaded

The app will validate all required variables on startup:

```bash
npm run dev
```

**Success**:
```
> Next.js development server running...
✅ Environment variables loaded successfully
```

**Error**:
```
❌ Missing required environment variables:
  - NEXT_PUBLIC_FIREBASE_API_KEY
  - NEXT_PUBLIC_FIREBASE_PROJECT_ID
```

### Browser Console Check

After starting the app, open browser DevTools (F12):

```javascript
// In console, type:
Object.keys(process.env).filter(k => k.startsWith('NEXT_PUBLIC'))
```

Should show all Firebase variables ✅

---

## 🚨 Troubleshooting

### Issue: "Missing required environment variables"

**Cause**: `.env.local` not created or missing values

**Solution**:
1. Verify `.env.local` exists in `admin-panel/` folder
2. Check all 6 variables are present
3. Verify no typos in variable names
4. Restart dev server after updating

### Issue: Firebase initialization fails

**Cause**: Credentials are wrong or Firebase project not created

**Solution**:
1. Verify credentials in Firebase Console
2. Make sure Firebase Project exists
3. Check auth domain is accessible
4. Try different web app in Firebase if multiple exist

### Issue: "Permission denied" when logging in

**Cause**: Firestore security rules don't match credentials

**Solution**:
1. Check Firestore rules in `backend/firestore.rules`
2. Deploy rules: `firebase deploy --only firestore:rules`
3. Verify admin user exists in `admins` collection
4. Check user email matches Firebase Auth user

### Issue: ".env.local" appears in git status

**Cause**: `.gitignore` not working properly

**Solution**:
```bash
# Remove from git tracking
git rm --cached admin-panel/.env.local

# Verify it's in .gitignore
cat admin-panel/.gitignore  # Should contain: .env.local

# Commit
git commit -m "Stop tracking .env.local"
```

---

## 🔄 Multiple Environments

### Development

`.env.local` - Development Firebase project:
```env
NEXT_PUBLIC_FIREBASE_PROJECT_ID=smartsorovnoma-dev-12345
...
```

### Staging

`.env.staging` - Staging Firebase project:
```env
NEXT_PUBLIC_FIREBASE_PROJECT_ID=smartsorovnoma-staging-67890
...
```

### Production

Deployed via CI/CD with secrets:
```bash
# GitHub Actions example
- name: Build
  env:
    NEXT_PUBLIC_FIREBASE_API_KEY: ${{ secrets.FIREBASE_API_KEY }}
    NEXT_PUBLIC_FIREBASE_PROJECT_ID: ${{ secrets.FIREBASE_PROJECT_ID }}
  run: npm run build
```

---

## 📋 Checklist

- [ ] `.env.local` file created
- [ ] All 6 Firebase variables filled in
- [ ] Values copied from Firebase Console correctly
- [ ] No typos in variable names
- [ ] `.env.local` is in `.gitignore`
- [ ] Dev server starts: `npm run dev`
- [ ] Can access `http://localhost:3000`
- [ ] Can login with admin credentials

---

## 📚 Related Documentation

- [Firebase Console](https://console.firebase.google.com/)
- [Firebase Project Settings](https://firebase.google.com/docs/projects/learn-more)
- [Admin Setup Guide](./SETUP_ADMIN.md)
- [README.md](../README.md)

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
