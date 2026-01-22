# Admin Onboarding Guide

## Overview
The Smart So‘rovnoma Admin Application requires manual bootstrapping for the initial admin user. This guide details the process of creating an admin user and managing roles.

## Prerequisites
- Access to the Firebase Console for the project.
- The `uid` of the user you wish to grant admin privileges to.

## Steps to Create an Admin User

1. **User Registration**
   - The user must first sign in to the Admin App using Google Sign-In at least once, OR you can create the user manually in Firebase Authentication.
   - Note the user's `UID` from the Firebase Authentication dashboard.

2. **Firestore Configuration**
   - Navigate to the **Firestore Database** section in the Firebase Console.
   - Go to the `users` collection.
   - Create a new document with the **Document ID** equal to the user's `UID`.

3. **Set Admin Fields**
   - Add the following fields to the document:
     - `role` (string): Set to `"admin"`.
     - `enabled` (boolean): Set to `true`.
     - `email` (string, optional): The user's email for reference.

   **Example Document Structure:**
   ```json
   {
     "role": "admin",
     "enabled": true,
     "email": "admin@example.com"
   }
   ```

4. **Verification**
   - Ask the user to sign in to the Admin App.
   - The app will verify the existence of this document and the `role`/`enabled` fields.
   - If successful, they will be redirected to the Dashboard.

## Revoking Access
To revoke admin access, simply change the `enabled` field to `false` or delete the document in the `users` collection.

## Troubleshooting
- **"Access Denied" Error**: Ensure the Firestore document ID matches the Auth UID exactly. Check that `role` is "admin" (lowercase) and `enabled` is boolean `true`.
- **Google Sign-In Failure**: Verify the SHA-1 fingerprint is added to the Firebase Console for the Admin App variant.
