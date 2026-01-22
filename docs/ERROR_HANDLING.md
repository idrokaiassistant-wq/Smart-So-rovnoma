# Error Handling & Resilience Guide

## 📋 Overview

This document describes the error handling mechanisms in SmartSorovnoma to ensure reliability and good user experience.

---

## 🔄 Submission Error Handling

### Response Submission Flow

```
User submits answers
    ↓
[Validation Check]
    ├─ Success → [Submit to Firebase]
    └─ Error → Show validation message + Retry
        ↓
    [Network Request]
        ├─ Success → [Save timestamp + Cooldown]
        └─ Network Error → [Automatic Retry 3x]
            ├─ Retry 1: 1 second delay
            ├─ Retry 2: 2 second delay
            ├─ Retry 3: 3 second delay
            └─ Final Failure → Show error + Manual Retry Button
```

---

## 🚨 Error Types & Responses

### 1. Network Errors (Retryable)

**Detection**: Connection timeout, socket exception, DNS resolution failure

**Handling**:
- Automatic retry: 3 attempts with exponential backoff
- User message: "Internet ulanishida xatolik. Iltimos, internetni tekshiring."
- UI: Show retry button after all attempts fail

**Code**:
```kotlin
// ResponseSubmissionService.kt
private fun isNetworkError(error: Throwable?): Boolean {
    return error?.message?.contains("network|connection|timeout", ignoreCase = true) == true
}
```

### 2. Permission Errors (Not Retryable)

**Detection**: User doesn't have permission to submit

**Handling**:
- No retry
- User message: "Yo'q, siz bu so'rovnomaga javob bera olmaysiz."
- Close submission UI

**Code**:
```kotlin
is SubmitResult.Error -> {
    canRetry = attempt < MAX_RETRIES  // false for permission errors
}
```

### 3. Survey Not Found (Not Retryable)

**Detection**: Survey ID doesn't exist or was deleted

**Handling**:
- No retry
- User message: "So'rovnoma topilmadi. Iltimos, qayta urinib ko'ring."
- Offer back button

### 4. Validation Errors (User Action Required)

**Detection**: Missing required fields, invalid answers

**Handling**:
- Show field-specific error message
- Highlight affected field
- Allow user to fix and retry

**Examples**:
- "Bu savol majburiy" (This question is required)
- "Iltimos, raqam kiriting" (Please enter a number)

---

## 💾 Anti-Spam Mechanism

### Cooldown System

To prevent abuse, users can only submit once per minute:

```kotlin
// QuestionFlowViewModel.kt
private val COOLDOWN_MS = 60_000L // 1 minute

fun submitResponses(): Boolean {
    if (_uiState.value.cooldownSeconds > 0) {
        _uiState.update {
            it.copy(
                submitError = "Iltimos, qayta yuborishdan oldin ${seconds} soniya kuting."
            )
        }
        return false
    }
}
```

**UI**: Countdown timer shows remaining time

---

## 🔁 Manual Retry Mechanism

### ReviewScreen Retry Button

When submission fails, user can:

1. **Read the error message** - Understand what went wrong
2. **Click "Retry" button** - Manual retry (up to 3 times)
3. **Check internet** - If connection issue, fix and retry
4. **Go back** - Edit answers if validation error

**Implementation**:
```kotlin
// ReviewScreen.kt
OutlinedButton(
    onClick = {
        viewModel.clearSubmitError()
        coroutineScope.launch {
            val success = viewModel.submitResponses()
            if (success) onSubmitClick()
        }
    },
    text = stringResource(R.string.retry)
)
```

---

## 📝 Error Messages (Uzbek)

| Error Type | Message | Retryable |
|-----------|---------|-----------|
| Network | "Internet ulanishida xatolik. Iltimos, internetni tekshiring." | ✅ Yes |
| Permission | "Yo'q, siz bu so'rovnomaga javob bera olmaysiz." | ❌ No |
| Not Found | "So'rovnoma topilmadi. Iltimos, qayta urinib ko'ring." | ❌ No |
| Generic | "Javoblarni yuborishda xatolik" | ✅ Yes |
| Required Field | "Bu savol majburiy" | ❌ No |
| Cooldown | "Iltimos, qayta yuborishdan oldin {X} soniya kuting." | ⏱️ Timed |

---

## 🔧 Future Enhancements (Offline Support)

### Planned: Offline Queue with Room Database

```
User offline & submits
    ↓
[Validation passes]
    ↓
[Save to local queue (Room DB)]
    ↓
[Show "Saved locally, will sync when online"]
    ↓
[Background worker monitors connection]
    ↓
[When online: Sync queue items]
    ├─ Success → Delete from queue
    └─ Failure → Keep in queue + notify user
```

**Benefits**:
- Users won't lose responses due to network issues
- Automatic sync when connection restored
- Manual sync button in UI

**Implementation**: See `Phase 3 - Task 3.2`

---

## ✅ Best Practices

1. **Always validate input** before submission
2. **Show clear error messages** in user's language
3. **Provide retry mechanism** for network errors
4. **Log errors** for debugging (future: Firebase Crashlytics)
5. **Test offline scenarios** with airplane mode
6. **Set reasonable timeouts** (Firebase default: 30s)

---

## 🧪 Testing Error Scenarios

### Manual Testing Checklist

- [ ] Network offline - Submit fails, shows retry button
- [ ] Network timeout - Automatic retry 3x, then manual retry
- [ ] Invalid answer - Shows specific error, allows fix
- [ ] Missing required field - Blocks navigation, shows error
- [ ] Cooldown active - Submit button disabled, shows countdown
- [ ] Submission success - Navigates to success screen

### Network Simulation

**Android**: Settings → Developer Options → Simulate Mobile Network Type
- None (offline)
- EDGE
- HSPA
- LTE

---

## 🐛 Debugging

### Enable verbose logging:

```kotlin
// In ResponseRepository or ResponseSubmissionService
try {
    // Make request
} catch (e: Exception) {
    Log.e("ResponseSubmission", "Submission failed", e)
    // Handle error
}
```

### Check Firestore Security Rules:

If getting permission denied, check:
1. User is authenticated
2. Firestore rules allow write to `/responses/{documentId}`
3. User has correct role (admin for admin-app, authenticated for user app)

---

## 📚 Related Files

- `app/src/main/java/com/smartsorovnoma/data/service/ResponseSubmissionService.kt` - Retry logic
- `app/src/main/java/com/smartsorovnoma/presentation/viewmodel/QuestionFlowViewModel.kt` - Error state management
- `app/src/main/java/com/smartsorovnoma/presentation/screen/ReviewScreen.kt` - Error UI + retry button
- `backend/firestore.rules` - Security rules that may cause permission errors

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
