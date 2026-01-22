# SmartSorovnoma — Android Survey Application

<p align="center">
  <img src="app/src/main/res/drawable/ic_launcher_foreground.png" width="120" alt="SmartSorovnoma Logo">
</p>

**SmartSorovnoma** is a modern, user-friendly Android application that allows users to complete surveys easily.

[📖 Uzbek Version (O'zbekcha)](README.md) | [🇬🇧 English](README_EN.md)

---

## 📱 Features

| Screen | Description |
|--------|-------------|
| **Survey List** | View all active surveys |
| **Survey Details** | View survey information and start button |
| **Question Flow** | Answer questions one by one |
| **Review** | Review all answers before submitting |
| **Success** | Confirmation message after submission |

---

## 🛠 Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Navigation Compose
- **Design System**: Material Design 3
- **Backend**: Firebase Firestore
- **Authentication**: Firebase Auth (Admin App only)

---

## 📋 Question Types

The application supports multiple question types:

| Type | Input | Example |
|------|-------|---------|
| `TEXT` | Text input | "What is your name?" |
| `NUMBER` | Numeric input | "How old are you?" |
| `SINGLE_CHOICE` | Select one option | "Which option do you prefer?" |
| `MULTI_CHOICE` | Select multiple options | "What are your interests?" |
| `RATING` | 1-5 star rating | "Rate your experience" |

---

## 🚀 Installation & Setup

### Android Application

#### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or later
- Android device or emulator (API 24+)

#### Setup Steps

1. **Clone repository**
   ```bash
   git clone https://github.com/your-org/SmartSorovnoma.git
   cd SmartSorovnoma
   ```

2. **Firebase Configuration**
   - Create Firebase project in [Firebase Console](https://console.firebase.google.com/)
   - Add Android app (package: `com.smartsorovnoma`)
   - Download `google-services.json`
   - Place in `app/` folder
   - See [GOOGLE_SERVICES_SETUP.md](docs/GOOGLE_SERVICES_SETUP.md) for detailed guide

3. **Build & Run**
   ```bash
   # Sync Gradle
   ./gradlew sync

   # Build debug APK
   ./gradlew :app:assembleDebug

   # Run on device
   ./gradlew :app:installDebug
   ```

### Admin Panel (Next.js)

#### Prerequisites
- Node.js 18+
- npm or yarn

#### Setup Steps

1. **Navigate to admin-panel**
   ```bash
   cd admin-panel
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment**
   ```bash
   cp .env.example .env.local
   # Edit .env.local with your Firebase credentials
   # See ENV_SETUP.md for detailed guide
   ```

4. **Start development server**
   ```bash
   npm run dev
   ```

5. **Access admin panel**
   - Open browser: `http://localhost:3000`
   - Login with admin credentials

### Firebase Setup

#### Create Firestore Database

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Create Firestore Database
4. Choose location (e.g., `asia-south1`)
5. Deploy security rules:
   ```bash
   firebase deploy --only firestore:rules
   ```

#### Create Admin User

See [SETUP_ADMIN.md](backend/SETUP_ADMIN.md) for complete guide:

1. Create admin user in Firebase Authentication
2. Add admin document to Firestore `admins` collection
3. Set role to `admin` and enabled to `true`

---

## 📁 Project Structure

```
SmartSorovnoma/
├── app/                                    # Android App Module
│   ├── src/main/java/com/smartsorovnoma/
│   │   ├── data/                          # Data layer (repositories, services)
│   │   │   ├── repository/                # Repositories (interface implementations)
│   │   │   └── service/                   # Services (Firebase, submission)
│   │   ├── domain/                        # Domain layer (business logic)
│   │   │   ├── model/                     # Data models (Survey, Question, etc.)
│   │   │   └── repository/                # Repository interfaces
│   │   └── presentation/                  # Presentation layer (UI)
│   │       ├── navigation/                # Navigation graph
│   │       ├── screen/                    # Compose screens
│   │       └── viewmodel/                 # ViewModels (state management)
│   └── src/main/res/                      # Resources (strings, colors, drawables)
│
├── admin-app/                             # Android Admin App Module
│   └── src/main/java/com/smartsorovnoma/admin/
│
├── admin-panel/                           # Next.js Admin Web Panel
│   ├── app/                               # Next.js app directory
│   ├── public/                            # Static files
│   └── .env.example                       # Environment template
│
├── backend/                               # Backend Configuration
│   ├── firestore.rules                    # Firestore Security Rules
│   ├── SETUP_ADMIN.md                     # Admin setup guide
│   └── firebase.json                      # Firebase config
│
├── docs/                                  # Documentation
│   ├── 00_PROJECT_OVERVIEW.md             # Project overview
│   ├── 01_REQUIREMENTS.md                 # Requirements
│   ├── 02_ARCHITECTURE.md                 # Architecture
│   ├── 03_DATA_MODEL.md                   # Data models
│   ├── 04_USER_STORIES.md                 # User stories
│   ├── ERROR_HANDLING.md                  # Error handling
│   ├── INTEGRATION_TESTING.md             # Testing guide
│   └── GOOGLE_SERVICES_SETUP.md           # Firebase setup
│
└── README.md (Uzbek), README_EN.md (English)
```

---

## 🏗 Architecture

### MVVM Pattern

```
View (Compose UI)
    ↓
ViewModel (State Management)
    ↓
Repository (Data Access)
    ↓
Data Source (Firebase)
```

### Data Flow

```
User Action
    ↓
ViewModel.method()
    ↓
Repository.operation()
    ↓
Firebase Service
    ↓
Update StateFlow
    ↓
Recompose UI
```

---

## 🔐 Security

### Firestore Rules

Security rules restrict access:

```firestore
match /surveys/{surveyId} {
  allow read: if resource.data.isPublished == true;
  allow read: if isAdmin();
}

match /responses/{responseId} {
  allow create: if request.auth != null;
  allow read: if false;
}

match /admins/{userId} {
  allow read: if isAdmin();
  allow write: if false;
}
```

### API Key Protection

- Firebase API key is public (that's intentional - Firebase uses Firestore rules for security)
- Sensitive data is never sent to frontend
- All operations verified on backend via Firestore rules

### Best Practices

- Never commit `.env.local` or `google-services.json`
- Use strong passwords for admin accounts
- Enable Two-Factor Authentication (2FA)
- Regularly rotate credentials
- Monitor Firestore audit logs

See [SECURITY.md](SECURITY.md) for complete security guide.

---

## 🧪 Testing

### Unit Tests

```bash
./gradlew testDebugUnitTest
```

Current test coverage:
- 28 unit tests
- Model tests
- ViewModel tests
- Repository tests

### Integration Tests

```bash
./gradlew connectedAndroidTest
```

### Firebase Emulator

For local testing with emulator:

```bash
firebase emulators:start
```

See [INTEGRATION_TESTING.md](docs/INTEGRATION_TESTING.md) for detailed guide.

---

## 🐛 Error Handling

### Submission Errors

The app handles submission errors gracefully:

1. **Network errors** → Automatic retry (3 attempts)
2. **Permission errors** → Display error message
3. **Survey not found** → Show error and back button
4. **Validation errors** → Highlight field and show message

### Retry Mechanism

Failed submissions can be retried:
- Manual retry button in Review screen
- Automatic retry for network errors
- Cooldown (1 minute) between submissions

See [ERROR_HANDLING.md](docs/ERROR_HANDLING.md) for complete guide.

---

## 📊 Performance

### Optimization Techniques

- Lazy loading of questions
- Compose recomposition optimization
- Firebase query optimization
- Minimal network requests

### App Size

- APK: ~12-15 MB (debug), ~8-10 MB (release)
- With ProGuard minification enabled for release

---

## 🚀 Deployment

### Mobile App

1. **Generate release keystore**
   ```bash
   keytool -genkey -v -keystore release.keystore \
     -keyalg RSA -keysize 2048 -validity 10000 -alias release
   ```

2. **Build release APK**
   ```bash
   ./gradlew :app:bundleRelease
   ```

3. **Sign and publish to Google Play Store**

See [DEPLOYMENT_CHECKLIST.md](DEPLOYMENT_CHECKLIST.md) for full checklist.

### Admin Panel

1. **Build for production**
   ```bash
   npm run build
   ```

2. **Deploy to hosting**
   ```bash
   firebase deploy --only hosting
   ```

---

## 📝 Documentation

- **[PROJECT_OVERVIEW.md](docs/00_PROJECT_OVERVIEW.md)** - Project overview and goals
- **[REQUIREMENTS.md](docs/01_REQUIREMENTS.md)** - Functional and technical requirements
- **[ARCHITECTURE.md](docs/02_ARCHITECTURE.md)** - System architecture and design
- **[DATA_MODEL.md](docs/03_DATA_MODEL.md)** - Database schema and models
- **[USER_STORIES.md](docs/04_USER_STORIES.md)** - User stories and flows
- **[ERROR_HANDLING.md](docs/ERROR_HANDLING.md)** - Error handling strategies
- **[INTEGRATION_TESTING.md](docs/INTEGRATION_TESTING.md)** - Testing setup and guide
- **[SECURITY.md](SECURITY.md)** - Security best practices
- **[DEPLOYMENT_CHECKLIST.md](DEPLOYMENT_CHECKLIST.md)** - Production deployment

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Code Style

- Use Kotlin conventions
- Follow Material Design 3
- Add comments for complex logic
- Write tests for new features

---

## 🐛 Known Issues

- UI tests not yet implemented (planned for Phase 3)
- Offline sync not yet implemented (planned for Phase 3)
- Dark theme support minimal (basic theme only)

See [GitHub Issues](https://github.com/your-org/SmartSorovnoma/issues) for full list.

---

## 📞 Support

For issues and questions:

- 📧 Email: support@smartsorovnoma.uz
- 🐛 GitHub Issues: [Create Issue](https://github.com/your-org/SmartSorovnoma/issues)
- 💬 Telegram: @smartsorovnoma_support

---

## 📄 License

This project is licensed under MIT License - see [LICENSE](LICENSE) file for details.

---

## 📈 Roadmap

- [x] MVP: Survey completion
- [x] Error handling with retry
- [ ] Offline support (Room database)
- [ ] Analytics integration
- [ ] Face recognition (optional)
- [ ] Biometric authentication (optional)
- [ ] Dark mode improvements
- [ ] Multiple language support

---

## 👥 Team

- **Product**: SmartSorovnoma Team
- **Engineering**: Full-stack team
- **QA**: Quality assurance team
- **Design**: UI/UX design team

---

**Last Updated**: 2026-01-22  
**Version**: 1.0.0  
**Status**: Active Development
