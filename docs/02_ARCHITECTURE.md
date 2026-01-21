# 02 — Architecture (Jetpack Compose + MVVM)

## 1. Stack (tavsiya)
- Kotlin + Jetpack Compose
- MVVM (ViewModel)
- Navigation Compose
- DI: Hilt
- Data: Repository pattern
- Local: Room (keyin), DataStore (settings)
- Network: Retrofit (agar REST) yoki Firebase

## 2. Modul tuzilma (single-module MVP)
```
app/
  presentation/
    navigation/
    screen/
    components/
    viewmodel/
  domain/
    model/
    usecase/
    repository/
  data/
    datasource/
      local/
      remote/
    repository/
    mapper/
```

## 3. UI oqim
SurveyList → SurveyDetail → QuestionFlow → Review → SubmitSuccess

## 4. State management (Compose)
- UI state: immutable data class (UiState)
- Events: sealed class UiEvent
- One-off effects: Channel/SharedFlow

## 5. Error handling
- Result wrapper: Success/Failure
- UI’da snackbars / inline error
