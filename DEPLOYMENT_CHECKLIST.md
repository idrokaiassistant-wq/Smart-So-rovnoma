# Production Deployment Checklist

## Security

### ✅ Completed
- [x] Android INTERNET va CAMERA permissions qo'shilgan
- [x] Firestore Security Rules yangilangan
- [x] Admin kolleksiyasi setup dokumentatsiyasi yaratilgan
- [x] Environment variables admin panel'ga qo'shilgan
- [x] `.gitignore` fayllar to'g'ri sozlangan

### ⚠️ Todo (Production'ga chiqishdan oldin)
- [ ] Firebase API keys'ni rotate qilish (production uchun yangi keys)
- [ ] Firebase App Check yoqish (bot protection)
- [ ] Firestore Rules'da `isAdmin()` funksiyasini faollashtirish
- [ ] Rate limiting qo'shish
- [ ] ProGuard rules to'liq qo'shish
- [ ] Release keystore yaratish va xavfsiz saqlash
- [ ] Firebase Analytics events qo'shish
- [ ] Crashlytics o'rnatish

## Testing

### ✅ Completed
- [x] Basic unit tests yozilgan (AnswerValue, Survey, ResponseRepository)

### ⚠️ Todo
- [ ] UI tests qo'shish (Compose UI testing)
- [ ] Integration tests qo'shish
- [ ] Manual testing qatnashuvchilar bilan
- [ ] Beta testing bir guruh foydalanuvchilar bilan

## Performance

### ⚠️ Todo
- [ ] Firestore offline cache yoqish
- [ ] Image optimization (agar rasmlar bo'lsa)
- [ ] APK size optimization
- [ ] ProGuard/R8 shrinking yoqish
- [ ] Memory leak testing

## Firebase Setup

### Production Environment

1. **Firebase Console'da production proyekt yaratish**
   ```
   Proyekt nomi: smartsorovnoma-production
   ```

2. **Firestore Rules deploy qilish**
   ```bash
   firebase deploy --only firestore:rules
   ```

3. **Admin foydalanuvchi yaratish**
   - Firebase Console → Authentication
   - Email/Password provider yoqish
   - Admin user yaratish
   - `admins` kolleksiyasiga qo'shish

4. **Security**
   - App Check yoqish
   - reCAPTCHA v3 sozlash
   - SafetyNet Attestation yoqish (Android)

## Build Configuration

### Release Build Tayyorlash

1. **Keystore yaratish**
   ```bash
   keytool -genkey -v -keystore smartsorovnoma.jks \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias smartsorovnoma
   ```

2. **Build variant sozlash**
   ```kotlin
   // app/build.gradle.kts
   buildTypes {
       release {
           isMinifyEnabled = true
           isShrinkResources = true
           proguardFiles(
               getDefaultProguardFile("proguard-android-optimize.txt"),
               "proguard-rules.pro"
           )
           signingConfig = signingConfigs.getByName("release")
       }
   }
   ```

3. **Build qilish**
   ```bash
   ./gradlew assembleRelease
   # yoki
   ./gradlew bundleRelease  # AAB uchun (Google Play)
   ```

## Admin Panel Deployment

### Vercel'ga Deploy

1. **Vercel account yaratish** → vercel.com

2. **GitHub'ga push qilish**
   ```bash
   git add .
   git commit -m "Production ready"
   git push origin main
   ```

3. **Vercel'da proyekt import qilish**
   - GitHub repository'ni tanlang
   - `admin-panel` papkasini root directory sifatida belgilang
   - Environment variables qo'shing (Firebase config)

4. **Environment Variables (Vercel)**
   ```
   NEXT_PUBLIC_FIREBASE_API_KEY=your_production_key
   NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
   NEXT_PUBLIC_FIREBASE_PROJECT_ID=your_project_id
   ...
   ```

## Google Play Store

### Tayorlik

1. **App ikonga diqqat bering**
   - 512x512 PNG (adaptive icon)
   - Foreground va background layers

2. **Store listing tayyorlash**
   - App nomi
   - Qisqa tavsif (80 belgi)
   - To'liq tavsif (4000 belgi)
   - Screenshots (telefon, planshet)
   - Feature graphic (1024x500)

3. **Privacy Policy**
   - Firebase ma'lumotlar to'plashini disclosure qilish
   - Privacy Policy URL tayyorlash

4. **App kategoriyasi va content rating**
   - Kategoriya tanlash
   - Content questionnaire to'ldirish

### Submission

```bash
# Release AAB yaratish
./gradlew bundleRelease

# AAB fayl joylashuvi:
# app/build/outputs/bundle/release/app-release.aab
```

## Post-Deployment Monitoring

- [ ] Firebase Analytics'ni tekshirish
- [ ] Crashlytics reportlarni monitoring qilish
- [ ] User feedback to'plash
- [ ] Performance metrics kuzatish
- [ ] Error rates monitoring

## Rollback Plan

Agar muammo bo'lsa:
1. Google Play Console'da oldingi versiyaga rollback
2. Firestore rules'ni oldingi holatiga qaytarish
3. Users'ga xabar berish

## Contact & Support

- Email: support@smartsorovnoma.uz
- Telegram: @smartsorovnoma_support
- GitHub Issues: github.com/your-repo/issues
