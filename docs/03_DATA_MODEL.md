# 03 — Data Model (MVP)

## Entities
### Survey
- id: String
- title: String
- description: String?
- isActive: Boolean
- version: Int (keyin)

### Question
- id: String
- surveyId: String
- order: Int
- type: QuestionType
- text: String
- required: Boolean
- choices: List<Choice>? (faqat choice turlarida)

### Choice
- id: String
- questionId: String
- text: String
- value: String

### Response (User submission)
- id: String
- surveyId: String
- respondentId: String (deviceId yoki userId)
- startedAt: Long
- submittedAt: Long?
- status: DRAFT | SUBMITTED

### Answer
- questionId: String
- value: String (JSON yoki plain)

## QuestionType (MVP)
- TEXT
- NUMBER
- SINGLE_CHOICE
- MULTI_CHOICE
- RATING

## Validation (MVP)
- required: true bo‘lsa bo‘sh bo‘lishi mumkin emas
- NUMBER: min/max (keyin)
- RATING: 1..5
