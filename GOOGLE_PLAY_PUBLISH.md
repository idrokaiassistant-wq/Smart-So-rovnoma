# Google Play Store Listing & Compliance Package
**App Name:** Smart So‘rovnoma
**Version:** 1.0.0
**Date:** 2026-01-22

---

## 1. Compliance Checklist (Verified)

| Policy Area | Status | Justification / Implementation |
| :--- | :--- | :--- |
| **Ads** | **NO** | No Ad SDKs in build.gradle.kts. "No Ads" selected in Console. |
| **Login/Auth** | **NO** | No Firebase Auth. No account creation required. |
| **Analytics** | **NO** | No Firebase Analytics or Crashlytics. |
| **Permissions** | **CLEAN** | Only `INTERNET` & `ACCESS_NETWORK_STATE`. No sensitive permissions. |
| **Content** | **SAFE** | No violence, sexual content, or user-to-user sharing. |

---

## 2. Store Listing Details

### Short Description (80 chars)
Fast, secure, and anonymous surveys. No ads, no login required.

### Full Description
**Smart So‘rovnoma** is a streamlined productivity tool designed for conducting and participating in surveys efficiently and anonymously. 

We believe in privacy and simplicity. That's why Smart So‘rovnoma requires **no registration, no login, and collects no personal data**. Simply open the app and start participating in available surveys immediately.

**Key Features:**
*   **Instant Access:** No account creation or sign-up needed.
*   **Completely Anonymous:** Your responses are submitted without linking to your identity.
*   **Ad-Free Experience:** Focus on the content without distractions.
*   **Clean & Modern UI:** Enjoy a beautiful, user-friendly interface with dark mode support.
*   **Secure:** All data is transmitted securely using industry-standard encryption.
*   **Lightweight:** Optimized for performance.

**Privacy First:**
*   No location tracking.
*   No camera or microphone access.
*   No analytics or user tracking.

Smart So‘rovnoma is the smartest way to share your opinion securely. Download now and experience the difference!

---

## 3. Data Safety Section (Critical)

*Copy these EXACT answers to Google Play Console > App Content > Data Safety.*

**Step 1: Data Collection & Security**
*   **Does your app collect or share any of the required user data types?** -> **Yes**
*   **Is all of the user data collected by your app encrypted in transit?** -> **Yes**
*   **Do you provide a way for users to request that their data be deleted?** -> **No** (Select: *Data is collected anonymously, so specific user data cannot be identified/deleted.*)

**Step 2: Data Types**
*   **Category:** App Activity
    *   **Data Type:** Other user-generated content (Survey Responses)
    *   **Collected?** Yes
    *   **Shared?** No (Data stays in your Firestore, not shared with 3rd parties)
    *   **Processed Ephemerally?** No
    *   **Purpose:** App functionality
    *   **Justification:** Needed to record survey answers.

*   **Category:** Personal Info / Location / Photos / Contacts / Device IDs
    *   **Status:** **UNCHECKED** (None collected).

---

## 4. Content Rating (IARC Questionnaire)

*   **Category:** Utility / Productivity
*   **Violence / Fear / Sexuality / Gambling / Language:** **No** to all.
*   **User Content Sharing:**
    *   *Does the app natively allow users to interact or exchange content with other users?* -> **No**. (Users submit to Server, not to other users).
*   **Online Content:**
    *   *Does the app promote or display content from third parties?* -> **No**.

**Expected Result:** PEGI 3 / ESRB Everyone.

---

## 5. Privacy Policy

**URL:** `https://smartsorovnoma.uz/privacy`

**Content:**
*   We do not collect personal information (Name, Email, Phone, Location).
*   Survey responses are anonymous.
*   No third-party tracking or analytics.
*   Data is stored securely on Google Firebase.

---
