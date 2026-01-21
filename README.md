# SmartSo'rovnoma — Android Mobil Ilova

<p align="center">
  <img src="app/src/main/res/drawable/ic_launcher_foreground.png" width="120" alt="SmartSorovnoma Logo">
</p>

**SmartSo'rovnoma** — foydalanuvchilar so'rovnomalarni to'ldiradigan, zamonaviy va qulay Android ilovasi.

## 📱 Ekranlar

| Ekran | Tavsif |
|-------|--------|
| **So'rovnomalar ro'yxati** | Barcha faol so'rovnomalarni ko'rish |
| **So'rovnoma tafsilotlari** | Tanlangan so'rovnoma haqida ma'lumot |
| **Savol oqimi** | Savollarni birma-bir to'ldirish |
| **Ko'rib chiqish** | Barcha javoblarni tekshirish |
| **Muvaffaqiyat** | Yuborish tasdig'i |

## 🛠 Texnologiyalar

- **Kotlin** — asosiy dasturlash tili
- **Jetpack Compose** — zamonaviy deklarativ UI
- **MVVM** — arxitektura patterni
- **Navigation Compose** — ekranlar orasida navigatsiya
- **Material Design 3** — zamonaviy dizayn tizimi

## 📋 Savol Turlari

- `TEXT` — matn kiritish
- `NUMBER` — raqam kiritish
- `SINGLE_CHOICE` — bitta variant tanlash
- `MULTI_CHOICE` — bir nechta variant tanlash
- `RATING` — 1-5 ballda baholash

## 🚀 O'rnatish

### Android Ilova

1. **Android Studio'ni oching**
   - Minimum: Android Studio Hedgehog (2023.1.1) yoki yangi
   - JDK 17 kerak

2. **Loyihani oching**
   ```bash
   git clone <repository-url>
   cd SmartSorovnoma
   ```

3. **Firebase konfiguratsiyasi**
   - [Firebase Console](https://console.firebase.google.com/)da proyekt yarating
   - Android app qo'shing (package: `com.smartsorovnoma`)
   - `google-services.json` faylini yuklab oling
   - `app/` papkasiga joylashtiring

4. **Dependencies o'rnatish**
   - Android Studio Gradle sync'ni kutish
   - Yoki terminal'da: `./gradlew build`

5. **Ishga tushirish**
   - Run → "app" modulini tanlang
   - Emulator yoki haqiqiy qurilmada test qiling

### Admin Panel

1. **Admin panel papkasiga o'ting**
   ```bash
   cd admin-panel
   ```

2. **Dependencies o'rnatish**
   ```bash
   npm install
   ```

3. **Environment variables sozlash**
   ```bash
   cp .env.local.example .env.local
   # .env.local faylini Firebase ma'lumotlar bilan to'ldiring
   ```

4. **Development server ishga tushirish**
   ```bash
   npm run dev
   ```
   Browser'da: `http://localhost:3000`

### Firebase Setup

1. **Firestore Database yaratish**
   - Firebase Console → Firestore Database
   - "Create Database" → Start in production mode
   - Location tanlang (asia-south1 tavsiya)

2. **Security Rules deploy qilish**
   ```bash
   firebase deploy --only firestore:rules
   ```

3. **Admin foydalanuvchi yaratish**
   - Qo'llanma: [`backend/SETUP_ADMIN.md`](backend/SETUP_ADMIN.md)

## 📁 Loyiha Strukturasi

```
app/src/main/java/com/smartsorovnoma/
├── data/
│   └── repository/          # Ma'lumotlar manbai
├── domain/
│   ├── model/               # Data modellar
│   └── repository/          # Repository interface
├── presentation/
│   ├── navigation/          # NavGraph
│   ├── screen/              # UI ekranlar
│   └── viewmodel/           # ViewModel
└── ui/theme/                # Ranglar, tipografiya
```

## 📚 Hujjatlar

Batafsil hujjatlar `docs/` papkasida:
- [Loyiha haqida](docs/00_PROJECT_OVERVIEW.md)
- [Talablar](docs/01_REQUIREMENTS.md)
- [Arxitektura](docs/02_ARCHITECTURE.md)
- [Data Model](docs/03_DATA_MODEL.md)
- [User Stories](docs/04_USER_STORIES.md)

## 📝 Litsenziya

MIT License

---

> Sana: 2026-01-21
