# 00 — Project Overview

## 1. Maqsad
SmartSo'rovnoma — foydalanuvchilar mobil ilova orqali so‘rovnomalarni to‘ldiradigan, admin esa so‘rovlarni boshqaradigan tizim.

## 2. Kimlar uchun
- Respondent: so‘rovni to‘ldiradi (mobil)
- Admin: so‘rov yaratadi, natijani ko‘radi (MVP’da admin panel shart emas, keyin qo‘shiladi)

## 3. MVP Scope (1-versiya)
### Funksiyalar
1) Surveylar ro‘yxati (mock data)
2) Survey detail (title/desc + Start)
3) Survey fill (step-by-step)
4) Review & Submit (mock submit)
5) Local state saqlash (in-memory) — keyin Room

### Savol turlari (MVP)
- TEXT
- NUMBER
- SINGLE_CHOICE
- MULTI_CHOICE
- RATING (1..5)

### Keyin (Smart)
- Skip-logic (conditional branching)
- Offline-first (Room + Sync)
- Auth (Google/Phone)
- Analytics/Export

## 4. Non-goals (MVP’da yo‘q)
- Murakkab admin panel
- Real-time chat
- Katta dashboard statistikalar

## 5. Acceptance Criteria (MVP)
- App ishga tushadi, surveylar ko‘rinadi
- Har bir question type UI ishlaydi
- Required validation ishlaydi
- Review ekranida barcha javoblar ko‘rinadi
- Submit bosilganda lokal “submitted” holat saqlanadi (mock)
