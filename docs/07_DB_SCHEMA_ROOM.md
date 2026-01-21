# 07 — Room DB Schema (offline uchun)

## Tables (taklif)
- survey
- question
- choice
- response
- answer

## Offline flow
1) surveys/questions cached
2) respondent draft response saqlanadi
3) internet bo‘lsa submit → serverga yuboriladi
4) yuborilgach status SUBMITTED va syncedAt
