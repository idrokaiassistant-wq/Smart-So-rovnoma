# 05 — Antigravity workflow (VS Code Agent)

Quyidagi workflow Antigravity agentga topshiriqlarni *kichik-kichik* bo‘lib berish uchun.

## 1) Qoidalar
- Har task: 1–3 fayl atrofida bo‘lsin
- Har task natijasi: build o‘tishi kerak
- Har o‘zgarishdan keyin qisqa commit message (agar git bo‘lsa)

## 2) Prompt shablonlari

### A) Project skeleton yaratish
- Maqsad: Compose + Navigation + Hilt setup
- Kutilgan natija: App ishga tushadi, 1 ta ekran

Prompt:
- "Android Kotlin Compose loyihada MVVM struktura yarat. Hilt va Navigation Compose qo‘sh. Paketlar: presentation/domain/data. Main ekran SurveyListScreen bo‘lsin."

### B) Mock data + Survey list
Prompt:
- "MockSurveyRepository yarat (data layer). SurveyListScreen’da lazy list chiqsin. Item bosilganda SurveyDetailScreen’ga navigatsiya qilsin."

### C) Question renderer (MVP)
Prompt:
- "QuestionType bo‘yicha composable renderer yoz: TEXT, NUMBER, SINGLE_CHOICE, MULTI_CHOICE, RATING. Javoblarni ViewModel state’da saqla."

### D) Review & submit
Prompt:
- "Review screen’da barcha answers ko‘rinsin. Submit bosilganda status SUBMITTED bo‘lsin va Success screen ochilsin."

## 3) “Definition of Done” (har task uchun)
- Code compile
- Minimal UI ishlaydi
- State yo‘qolmasin (rotation bo‘lsa ham — ViewModel)
- Lint/format ok
