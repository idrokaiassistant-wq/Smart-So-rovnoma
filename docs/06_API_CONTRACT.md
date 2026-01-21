# 06 — API Contract (kelajakda REST/Firebase)

MVP’da mock ishlatamiz. Keyin quyidagi endpointlar kerak bo‘ladi.

## REST (variant)
### GET /surveys
Return: list of active surveys

### GET /surveys/{id}
Return: survey detail + questions

### POST /responses
Body: response (surveyId, respondentId, answers[])
Return: created id + status

### GET /responses?surveyId=...
Admin uchun (keyin)

## Firebase (variant)
- surveys (collection)
- surveys/{id}/questions (subcollection)
- responses (collection, surveyId index)
