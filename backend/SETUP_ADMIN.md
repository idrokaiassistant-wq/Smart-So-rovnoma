# Admin User Setup Guide

**Sana**: 2026-01-22  
**Versiya**: 1.0

---

## 📋 Overview

This guide explains how to set up administrator users for SmartSorovnoma. Admins can:
- Create and manage surveys
- View survey responses and analytics
- Manage other admin users
- Configure system settings

---

## 🔐 Prerequisites

Before starting, you need:

1. **Firebase Project** - Active Firebase project for SmartSorovnoma
2. **Firebase CLI** - Installed on your machine
3. **Admin Credentials** - Access to Firebase Console with owner role
4. **Database** - Firestore Database initialized with security rules

```bash
# Check Firebase CLI installation
firebase --version

# If not installed:
npm install -g firebase-tools

# Verify Firebase login
firebase login
```

---

## 📝 Method 1: Firebase Console (Manual)

### Step 1: Create Firestore Collection

1. Open [Firebase Console](https://console.firebase.google.com/)
2. Select your SmartSorovnoma project
3. Go to **Firestore Database** (left sidebar)
4. Click **Start collection**

**Collection Settings**:
- Collection ID: `admins`
- Auto-ID for first document (or custom ID)

### Step 2: Create Admin Document

After creating collection, Firebase prompts to add first document:

**Document ID**: Use Firebase user UID (you'll get this from Authentication step)

**Fields** (click **Add field** for each):

| Field | Type | Value |
|-------|------|-------|
| `role` | string | `admin` |
| `email` | string | admin@smartsorovnoma.uz |
| `enabled` | boolean | `true` |
| `createdAt` | timestamp | (current date/time) |
| `displayName` | string | (optional) |

**Example**:
```json
{
  "role": "admin",
  "email": "admin@smartsorovnoma.uz",
  "enabled": true,
  "createdAt": Timestamp(2026, 1, 22),
  "displayName": "Admin User"
}
```

### Step 3: Create Firebase Auth User

1. Go to **Authentication** (left sidebar)
2. Click **Get started** (if first time)
3. Select **Email/Password** sign-in method
4. Click **Enable**
5. Click **Add user** at top
   - **Email**: admin@smartsorovnoma.uz
   - **Password**: Create strong password (min. 6 chars)
   - Save the **User UID** shown in the user list

**IMPORTANT**: The User UID must match the document ID you used in Step 2!

### Step 4: Verify Setup

1. Go to admin-panel folder:
   ```bash
   cd admin-panel
   ```

2. Create/update `.env.local` with your Firebase credentials:
   ```env
   NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key
   NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
   NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
   NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.firebasestorage.app
   NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your-sender-id
   NEXT_PUBLIC_FIREBASE_APP_ID=1:your-sender-id:web:your-app-id
   ```

3. Install dependencies:
   ```bash
   npm install
   ```

4. Start development server:
   ```bash
   npm run dev
   ```

5. Open browser: `http://localhost:3000`

6. Login with admin credentials:
   - Email: admin@smartsorovnoma.uz
   - Password: (the password you set)

7. If login succeeds, setup is complete! ✅

---

## 🖥️ Method 2: Firebase CLI (Command Line)

### Prerequisites

```bash
npm install -g firebase-tools
firebase login
firebase use smartsorovnoma-c7086  # Replace with your project ID
```

### Create Admin User

```bash
# 1. Create Firebase Auth user
# (Use Firebase Console for this - CLI doesn't support user creation yet)

# 2. Get the User UID from Firebase Console

# 3. Create admin document
firebase firestore:set admins/YOUR_USER_UID \
  '{"role":"admin","email":"admin@smartsorovnoma.uz","enabled":true,"createdAt":"2026-01-22"}'

# 4. Verify
firebase firestore:get admins/YOUR_USER_UID
```

**Example Output**:
```
Document at 'admins/XyZ123abc' exists!
{
  "role" : "admin",
  "email" : "admin@smartsorovnoma.uz",
  "enabled" : true
}
```

---

## 🔄 Add More Admin Users

To add additional admins (repeat the process):

1. **Firebase Console** → Authentication → Add new user
2. Copy the new User UID
3. **Firestore** → admins collection → Add new document with same UID
4. Fill in the same fields (role, email, enabled, etc.)

---

## 🧪 Testing Admin Access

### Local Testing

```bash
cd admin-panel
npm run dev
```

Visit `http://localhost:3000` and:
- [ ] Login page loads
- [ ] Can login with admin email and password
- [ ] Dashboard loads after login
- [ ] Can view surveys (if any exist)
- [ ] Can create new survey

### Remote Testing

After deploying admin-panel to production:
- [ ] Visit production URL
- [ ] Login works with admin credentials
- [ ] Can manage surveys
- [ ] All features functional

---

## 🔐 Security Best Practices

### 1. Strong Passwords

Create strong admin passwords:
- **Minimum**: 12 characters
- **Include**: Uppercase, lowercase, numbers, symbols
- **Example**: `Xd#9kL$mPq@2024`

**❌ Bad**: `admin123`, `password`, `smartsorovnoma`  
**✅ Good**: `Xd#9kL$mPq@2024`, `R7*tB%Nc8&Yw9@2`

### 2. Two-Factor Authentication (2FA)

**Enable in Firebase**:
1. Firebase Console → Authentication → Settings
2. Under "User providers", enable **Multi-factor authentication**
3. Require admin users to set up 2FA

### 3. Firestore Security Rules

Verify rules allow only admins:

```firestore
match /admins/{userId} {
  allow read: if isAdmin();
  allow write: if false;  // Only manual Firebase console writes
}

match /surveys/{surveyId} {
  allow read: if isAdmin();
  allow write: if isAdmin();
}
```

### 4. Limit Admin Access

- **Only authorized people** should have admin credentials
- **Don't share passwords** - use secure password manager
- **Rotate passwords** every 3 months
- **Remove admin access** immediately if person leaves team

### 5. Monitor Admin Activity

Enable Firestore audit logging:
1. Firebase Console → Firestore Database → Audit logs
2. Enable Cloud Audit Logs integration
3. Review logs regularly for suspicious activity

---

## 🐛 Troubleshooting

### Issue: "Permission denied" when logging in

**Cause**: Firestore rules don't allow read access to user document

**Solution**:
1. Check Firestore security rules have `isAdmin()` function
2. Verify admin document exists with correct UID
3. Check `enabled: true` is set in document

```firestore
function isAdmin() {
  let userDoc = get(/databases/$(database)/documents/users/$(request.auth.uid));
  return userDoc.data.role == "admin" && userDoc.data.enabled == true;
}
```

### Issue: "User not found" error

**Cause**: Email used doesn't match Firebase Auth user

**Solution**:
1. Firebase Console → Authentication → Users
2. Find your admin user
3. Copy exact email address
4. Update email in both:
   - Firebase Auth user (if needed)
   - Admins document in Firestore

### Issue: ".env.local not found" warning

**Cause**: Missing environment variables file

**Solution**:
1. Copy `.env.example` to `.env.local`
2. Fill in your Firebase credentials
3. Restart development server

### Issue: "Cannot read property 'currentUser' of null"

**Cause**: Firebase not initialized in admin-panel

**Solution**:
1. Check `.env.local` has all required Firebase keys
2. Verify Firebase project ID is correct
3. Check network requests in browser DevTools
4. Clear browser cache and refresh

---

## 📋 Checklist: Complete Admin Setup

- [ ] Firebase project created
- [ ] Firestore Database initialized
- [ ] Security rules deployed
- [ ] Authentication enabled (Email/Password)
- [ ] Admin user created in Firebase Auth
- [ ] Admin document created in Firestore (matching UID)
- [ ] `.env.local` created in admin-panel folder
- [ ] `.env.local` contains all Firebase credentials
- [ ] `npm install` completed in admin-panel
- [ ] `npm run dev` starts without errors
- [ ] Can access `http://localhost:3000`
- [ ] Can login with admin credentials
- [ ] Dashboard loads and shows surveys
- [ ] No console errors in browser DevTools

---

## 🚀 Production Deployment

When deploying to production:

1. **Use different Firebase project** for production
2. **Download new `google-services.json`** from production Firebase project
3. **Create production admin user** in production Firebase project
4. **Deploy updated security rules** to production
5. **Enable Firebase App Check** for security
6. **Set up monitoring** for admin activity
7. **Test admin panel** in staging environment first

---

## 📞 Support

If you encounter issues:

1. **Check Firebase Console** for any errors/alerts
2. **Review Firestore Rules** - most issues are permission-related
3. **Clear browser cache** and try again
4. **Check network tab** in browser DevTools
5. **Look at server logs**: `npm run dev` terminal output

---

## 📚 Related Documentation

- [Firebase Console](https://console.firebase.google.com/)
- [Firestore Security Rules](../backend/firestore.rules)
- [Admin Panel README](../admin-panel/README.md)
- [SECURITY.md](../SECURITY.md)
- [README.md](../README.md)

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
