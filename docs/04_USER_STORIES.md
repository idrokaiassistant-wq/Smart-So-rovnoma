# 04 — User Stories (MVP)

## Respondent (Foydalanuvchi)

### So'rovnomalarni Ko'rish
1. Men so'rovnomalar ro'yxatini ko'ra olaman
2. Har bir so'rovnomada sarlavha va qisqa tavsif ko'rsatiladi
3. Faqat faol so'rovnomalar ro'yxatda ko'rinadi

### So'rovnoma Tafsilotlari
4. Men so'rovnomani bosib tafsilotlarini ko'raman
5. Savollar soni va taxminiy vaqt ko'rsatiladi
6. "Boshlash" tugmasi orqali to'ldirishni boshlayman

### Savollarni To'ldirish
7. Men savollarga navbat bilan javob beraman
8. Progress bar joriy holatni ko'rsatadi
9. "Orqaga" tugmasi orqali oldingi savolga qaytaman
10. Majburiy savollar tekshiriladi (xatolik ko'rsatiladi)

### Ko'rib Chiqish
11. Review ekranida barcha javoblarni ko'raman
12. Javob berilmagan savollar belgilanadi
13. "Yuborish" tugmasi bilan yakunlayman

### Muvaffaqiyat
14. Submit qilinganda tasdiqlash ekrani ko'rsatiladi
15. "Bosh sahifaga" tugmasi orqali ro'yxatga qaytaman

## Savol Turlari
- **TEXT**: Erkin matn kiritish (1-3 qator)
- **NUMBER**: Faqat raqam kiritish
- **SINGLE_CHOICE**: Radio buttonlar orqali bitta tanlov
- **MULTI_CHOICE**: Checkbox orqali bir nechta tanlov
- **RATING**: 1-5 yulduz bilan baholash

## System
- Submit qilinganda response status `SUBMITTED` bo'ladi (MVP'da lokal)
- Barcha javoblar `ViewModel` da saqlanadi
