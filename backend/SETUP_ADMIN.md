# Admin Setup Guide

## Firebase Admin Foydalanuvchisini Qo'shish

### 1. Firebase Console'ga kirish
1. [Firebase Console](https://console.firebase.google.com/)ga kiring
2. `smartsorovnoma-c7086` proyektini tanlang

### 2. Admin foydalanuvchi yaratish

#### Variant A: Firebase Console orqali
1. Firestore Database'ga o'ting
2. "Start collection" ni bosing
3. Collection ID: `admins`
4. Document ID: `<USER_UID>` (Firebase Authentication'dagi foydalanuvchi ID'si)
5. Field qo'shing:
   - Field: `role`
   - Type: `string`
   - Value: `admin`
   - Field: `email`
   - Type: `string`  
   - Value: `admin@smartsorovnoma.uz`
   - Field: `createdAt`
   - Type: `timestamp`
   - Value: `<hozirgi vaqt>`

#### Variant B: Firebase CLI orqali

```bash
# Firebase CLI o'rnatish
npm install -g firebase-tools

# Login qilish
firebase login

# Proyektni tanlash
firebase use smartsorovnoma-c7086

# Admin qo'shish (UID'ni o'zgartiring)
firebase firestore:set admins/YOUR_USER_UID '{"role":"admin","email":"admin@example.com","createdAt":{"_seconds":1674000000}}'
```

### 3. Authentication sozlash

Admin panel uchun Firebase Authentication'ni yoqish:

1. Firebase Console → Authentication
2. Sign-in method → Email/Password → Enable
3. Yangi foydalanuvchi qo'shish:
   - Email: `admin@smartsorovnoma.uz`
   - Password: `<xavfsiz parol>`
4. User UID'ni copy qiling
5. Admins kolleksiyasida document yaratish (yuqoridagi ko'rsatma)

### 4. Testing

Admin panel'ga kirish va test qilish:

```bash
cd admin-panel
npm run dev
```

Browser'da: `http://localhost:3000`

## Production Deployment

Production'da qo'shimcha xavfsizlik:

1. Firestore Rules'da `isAdmin()` funksiyasini faollashtirish
2. Firebase App Check yoqish
3. Rate limiting qo'shish
4. Audit logging yoqish

## Security Notes

⚠️ **MUHIM**: 
- Admin parollarini xavfsiz saqlang
- 2FA (Two-Factor Authentication) yoqing
- Admin kolleksiyasiga write access faqat manual
- Regular security audit o'tkazing
