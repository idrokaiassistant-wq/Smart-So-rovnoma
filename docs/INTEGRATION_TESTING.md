# Integration Testing Guide

## 📋 Overview

Integration tests verify that different components work together correctly. This guide covers setting up and running integration tests for SmartSorovnoma.

---

## 🏗 Architecture for Testing

### Layers Being Tested

```
┌─────────────────────────────────┐
│  Presentation (UI Screens)      │  ← UI Integration Tests
├─────────────────────────────────┤
│  ViewModel (State Management)   │  ← ViewModel Tests
├─────────────────────────────────┤
│  Repository (Data Layer)        │  ← Repository Tests
├─────────────────────────────────┤
│  Firebase (Remote Data)         │  ← Firebase Emulator Tests
└─────────────────────────────────┘
```

---

## 🧪 Test Types

### 1. Unit Tests

**Scope**: Single function/class in isolation  
**Tools**: JUnit, Mockito  
**Location**: `app/src/test/java/`

**Example**:
```kotlin
@Test
fun `isAnswerValid should return true for non-empty text`() {
    val answer = AnswerValue.Text("Valid text")
    assertTrue(isAnswerValid(answer))
}
```

### 2. Integration Tests

**Scope**: Multiple components together  
**Tools**: Espresso, Firebase Emulator  
**Location**: `app/src/androidTest/java/`

**Example**:
```kotlin
@Test
fun `submit survey flow should succeed`() {
    // Load survey
    val survey = repository.getSurveyById("test_survey")
    
    // Fill answers
    val answers = mapOf("q1" to AnswerValue.Text("answer"))
    
    // Submit
    val result = service.submitResponses("test_survey", answers)
    
    // Verify
    assertTrue(result is SubmitResult.Success)
}
```

### 3. End-to-End Tests

**Scope**: Complete user flow  
**Tools**: Espresso for UI, Firebase Emulator for backend  

**Example Flow**:
1. Launch app → Survey List Screen
2. Click survey → Survey Detail Screen
3. Start survey → Question Flow Screen
4. Answer questions → Review Screen
5. Submit → Success Screen

---

## 🚀 Setup Firebase Emulator

### Prerequisites

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login
```

### Start Emulator

```bash
cd backend/

# Start all emulators
firebase emulators:start

# Or specific emulators
firebase emulators:start --only firestore,pubsub
```

**Output**:
```
┌─────────────────────────────────────────────────────────┐
│ ✔  All emulators ready! It is now safe to connect.     │
├─────────────────────────────────────────────────────────┤
│ │
│ Firestore Emulator: http://localhost:8080              │
│ Pub/Sub Emulator: http://localhost:8085                │
│ │
│ Other user can now connect using config:               │
│ │
│ * Firestore: const db = initializeApp({                │
│   ...                                                   │
│   host: 'localhost:8080'                               │
│ })                                                      │
│ │
└─────────────────────────────────────────────────────────┘
```

### Configure App to Use Emulator

In `QuestionFlowViewModel.kt` or Repository:

```kotlin
if (BuildConfig.DEBUG) {
    // Connect to Firebase emulator
    val settings = FirebaseFirestoreSettings.Builder()
        .setHost("10.0.2.2:8080")  // Android emulator localhost
        .setSslEnabled(false)
        .build()
    
    Firebase.firestore.firestoreSettings = settings
}
```

---

## 📝 Writing Integration Tests

### Example: Submission Integration Test

```kotlin
@RunWith(AndroidTestRunner::class)
class SubmissionIntegrationTest {
    
    private val db = Firebase.firestore
    private val repository = ResponseRepository()
    
    @Before
    fun setup() {
        // Clear firestore emulator before each test
        // FirestoreEmulator.clearData()
    }
    
    @Test
    fun `submit responses and verify in firestore`() {
        val surveyId = "test_survey_123"
        val answers = mapOf(
            "q1" to AnswerValue.Text("Answer 1"),
            "q2" to AnswerValue.Rating(5)
        )
        
        // Submit
        runBlocking {
            val result = repository.submitResponse(surveyId, answers)
            
            // Verify success
            assertTrue(result.isSuccess)
            
            // Verify in Firestore
            val responseId = result.getOrNull()
            val doc = db.collection("responses")
                .document(responseId!!)
                .get()
                .await()
            
            assertTrue(doc.exists())
            assertEquals(surveyId, doc.get("surveyId"))
        }
    }
}
```

---

## 🎯 Test Scenarios

### QuestionFlow Screen

- [ ] Load survey questions successfully
- [ ] Display first question
- [ ] Navigate forward with valid answer
- [ ] Navigate backward
- [ ] Show error for missing required field
- [ ] Block navigation if validation fails
- [ ] Display all question types (Text, Number, Choice, Rating)

### ReviewScreen

- [ ] Display all submitted answers
- [ ] Show submit button when not on cooldown
- [ ] Show cooldown timer when active
- [ ] Display error message on submission failure
- [ ] Show retry button on recoverable error
- [ ] Navigate to success screen on successful submission

### Error Handling

- [ ] Retry on network error
- [ ] Show permission error (not retryable)
- [ ] Handle survey not found
- [ ] Anti-spam cooldown enforcement
- [ ] Clear error on retry

---

## 🏃 Running Tests

### All Tests
```bash
./gradlew test
```

### Unit Tests Only
```bash
./gradlew testDebugUnitTest
```

### Integration Tests (requires emulator)
```bash
./gradlew connectedAndroidTest
```

### Specific Test Class
```bash
./gradlew testDebugUnitTest --tests "*.QuestionFlowViewModelTest"
```

### With Logging
```bash
./gradlew test --info
```

---

## 📊 Test Coverage

### Current Status
- Unit Tests: 28 tests (basic coverage)
- Integration Tests: Planned
- UI Tests: Planned

### Target
- Unit Tests: 40+ tests
- Integration Tests: 15+ tests
- UI Tests: 10+ tests
- **Overall Coverage**: >80%

---

## 🐛 Debugging Failed Tests

### Common Issues

| Issue | Solution |
|-------|----------|
| Firebase not initialized | Use Firebase emulator + manual init in test setup |
| Firestore permission denied | Check emulator is running + rules set to test mode |
| Timeout errors | Increase timeout, check emulator health |
| Async/coroutine issues | Use `runBlocking {}` or `runTest {}` |

### Enable Debug Logging

```kotlin
// In test setup
FirebaseAuth.getInstance().useEmulator("localhost", 9099)
Firebase.firestore.useEmulator("localhost", 8080)

// With logging
val logger = Logger.getLogger(FirebaseAuth::class.qualifiedName)
logger.level = Level.FINE
```

---

## ✅ Continuous Integration

### GitHub Actions Example

```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      
      - name: Run unit tests
        run: ./gradlew testDebugUnitTest
      
      - name: Upload coverage
        uses: codecov/codecov-action@v2
```

---

## 📚 Additional Resources

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Firestore Testing](https://firebase.google.com/docs/firestore/security/test-rules-emulator)
- [Firebase Emulator Suite](https://firebase.google.com/docs/emulator-suite)
- [Espresso Testing](https://developer.android.com/training/testing/espresso)

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
