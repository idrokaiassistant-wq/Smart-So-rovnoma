/**
 * Muhit sozlamalarini tekshirish
 * Barcha kerakli Firebase ma'lumotlari mavjudligini tekshiradi
 */

interface FirebaseConfig {
  apiKey: string;
  authDomain: string;
  projectId: string;
  storageBucket: string;
  messagingSenderId: string;
  appId: string;
}

/**
 * Ishga tushishda muhit o'zgaruvchilarini tekshirish
 */
export function validateEnvironment(): FirebaseConfig {
  const requiredVars = [
    'NEXT_PUBLIC_FIREBASE_API_KEY',
    'NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN',
    'NEXT_PUBLIC_FIREBASE_PROJECT_ID',
    'NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET',
    'NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID',
    'NEXT_PUBLIC_FIREBASE_APP_ID',
  ];

  const missing: string[] = [];

  requiredVars.forEach((varName) => {
    const value = process.env[varName];
    if (!value || value.trim() === '') {
      missing.push(varName);
    }
  });

  if (missing.length > 0) {
    const errorMsg = `
❌ Quyidagi muhit o'zgaruvchilari yetishmayapti:
${missing.map((v) => `  - ${v}`).join('\n')}

Iltimos, ushbu o'zgaruvchilar bilan .env.local faylini yarating.
Andoza uchun .env.example fayliga qarang.
`;
    console.error(errorMsg);
    throw new Error(errorMsg);
  }

  return {
    apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY!,
    authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN!,
    projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID!,
    storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET!,
    messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID!,
    appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID!,
  };
}

/**
 * Tekshirilgan Firebase konfiguratsiyasini olish
 */
export function getFirebaseConfig(): FirebaseConfig {
  if (typeof window === 'undefined') {
  // Server tomoni: faqat bir marta tekshirish
    if (!global.__firebaseConfig) {
      global.__firebaseConfig = validateEnvironment();
    }
    return global.__firebaseConfig;
  }

  // Mijoz tomoni: muhitni tekshirish
  return validateEnvironment();
}

/**
 * Global turlarni kengaytirish
 */
declare global {
  var __firebaseConfig: FirebaseConfig | undefined;
}
