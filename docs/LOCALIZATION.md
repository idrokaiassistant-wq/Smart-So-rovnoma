# Internationalization (i18n) Setup Guide

**Sana**: 2026-01-22  
**Versiya**: 1.0

---

## 📋 Overview

SmartSorovnoma supports multiple languages through Android's built-in string resources system.

Currently supported:
- 🇺🇿 **Uzbek** (default) - `values` folder
- 🇬🇧 **English** - `values-en` folder

---

## 📁 String Resources Structure

```
app/src/main/res/
├── values/
│   └── strings.xml          # Default (Uzbek)
├── values-en/
│   └── strings.xml          # English
└── values-uz/
    └── strings.xml          # Uzbek (alternative)
```

---

## 🔄 Current Strings

### Uzbek (values/strings.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">SmartSorovnoma</string>
    
    <!-- Common -->
    <string name="back">Orqaga</string>
    <string name="next">Keyingi</string>
    <string name="submit">Yuborish</string>
    <string name="retry">Qayta urinish</string>
    <string name="loading">Yuklanmoqda...</string>
    <string name="error">Xatolik</string>
    
    <!-- Screen Titles -->
    <string name="surveys">So'rovnomalar</string>
    <string name="survey_details">So'rovnoma tafsilotlari</string>
    <string name="questions">Savollar</string>
    <string name="review">Ko'rib chiqish</string>
    <string name="success">Muvaffaqiyat</string>
    
    <!-- Messages -->
    <string name="survey_not_found">So'rovnoma topilmadi</string>
    <string name="no_surveys">So'rovnoma yo'q</string>
    <string name="required_field">Bu savol majburiy</string>
    
    <!-- Error Messages -->
    <string name="network_error">Internet ulanishida xatolik</string>
    <string name="permission_denied">Ruxsat berilmadi</string>
    <string name="submission_failed">Yuborishda xatolik</string>
</resources>
```

### English (values-en/strings.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">SmartSorovnoma</string>
    
    <!-- Common -->
    <string name="back">Back</string>
    <string name="next">Next</string>
    <string name="submit">Submit</string>
    <string name="retry">Retry</string>
    <string name="loading">Loading...</string>
    <string name="error">Error</string>
    
    <!-- Screen Titles -->
    <string name="surveys">Surveys</string>
    <string name="survey_details">Survey Details</string>
    <string name="questions">Questions</string>
    <string name="review">Review</string>
    <string name="success">Success</string>
    
    <!-- Messages -->
    <string name="survey_not_found">Survey not found</string>
    <string name="no_surveys">No surveys available</string>
    <string name="required_field">This field is required</string>
    
    <!-- Error Messages -->
    <string name="network_error">Network connection error</string>
    <string name="permission_denied">Permission denied</string>
    <string name="submission_failed">Submission failed</string>
</resources>
```

---

## 📝 Using Strings in Code

### In Compose UI

```kotlin
import androidx.compose.ui.res.stringResource
import com.smartsorovnoma.R

@Composable
fun SurveyListScreen() {
    Text(
        text = stringResource(R.string.surveys),
        style = MaterialTheme.typography.headlineLarge
    )
    
    Button(
        onClick = { /* action */ },
        text = stringResource(R.string.submit)
    )
}
```

### In ViewModel/Logic

```kotlin
class SurveyViewModel(private val context: Context) : ViewModel() {
    private fun getErrorMessage(error: Throwable): String {
        return when {
            error is NetworkException -> 
                context.getString(R.string.network_error)
            error is PermissionException -> 
                context.getString(R.string.permission_denied)
            else -> 
                context.getString(R.string.submission_failed)
        }
    }
}
```

---

## ✏️ Adding New Strings

### Step 1: Add to Uzbek (Default)

Edit `app/src/main/res/values/strings.xml`:

```xml
<string name="new_feature_title">Yangi xususiyat</string>
<string name="new_feature_description">Bu yangi xususiyatning tavsifi</string>
```

### Step 2: Add to English

Edit `app/src/main/res/values-en/strings.xml`:

```xml
<string name="new_feature_title">New Feature</string>
<string name="new_feature_description">This is the description of the new feature</string>
```

### Step 3: Use in Code

```kotlin
Text(text = stringResource(R.string.new_feature_title))
```

### Step 4: Build

```bash
./gradlew clean build
```

Studio will automatically generate updated `R.string` constants.

---

## 🏗 Adding New Language

### Example: Adding Russian (Русский)

#### Step 1: Create Directory

```bash
mkdir -p app/src/main/res/values-ru
```

#### Step 2: Create strings.xml

Create `app/src/main/res/values-ru/strings.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">SmartSorovnoma</string>
    <string name="back">Назад</string>
    <string name="next">Далее</string>
    <!-- Copy all strings from values/strings.xml and translate -->
</resources>
```

#### Step 3: Verify

Build and test on Russian locale device:

```bash
./gradlew :app:assembleDebug
# Test on device with Russian language setting
```

---

## 🌍 Language Selection

Currently, the app follows device language:

- Device language: **Uzbek** → App shows Uzbek strings
- Device language: **English** → App shows English strings
- Device language: **Russian** → App falls back to Uzbek (default)

### Future: Language Selection Menu

To add in-app language selector:

```kotlin
class LanguagePreferences(context: Context) {
    private val prefs = context.getSharedPreferences("lang", Context.MODE_PRIVATE)
    
    var selectedLanguage: String
        get() = prefs.getString("language", "uz") ?: "uz"
        set(value) = prefs.edit().putString("language", value).apply()
}

// Usage in settings screen
Button(onClick = { 
    preferences.selectedLanguage = "en"
    recreateActivity()
})
```

---

## 🎨 Formatting Strings

### Placeholder Strings

```xml
<!-- Uzbek -->
<string name="question_count">Jami savollar: %1$d ta</string>

<!-- English -->
<string name="question_count">Total questions: %1$d</string>
```

Usage:
```kotlin
stringResource(R.string.question_count, surveyQuestionCount)
// Output: "Jami savollar: 5 ta" (Uzbek) or "Total questions: 5" (English)
```

### Plurals

```xml
<!-- In plurals.xml -->
<plurals name="survey_responses">
    <item quantity="one">%d javob</item>
    <item quantity="other">%d javob</item>
</plurals>
```

Usage:
```kotlin
quantityStringResource(R.plurals.survey_responses, count, count)
```

---

## 🔍 Finding Untranslated Strings

### Android Studio Built-in Check

1. **Run → Analyze → Run Inspection by Name**
2. Search: "Untranslated Strings"
3. View results - shows missing translations

### Manual Check

Look for hardcoded strings (❌ wrong):

```kotlin
// ❌ BAD - Hardcoded
Text(text = "Bu savol majburiy")  

// ✅ GOOD - Uses string resource
Text(text = stringResource(R.string.required_field))
```

---

## 📊 Translation Coverage

| Language | Files | Status | Coverage |
|----------|-------|--------|----------|
| Uzbek (default) | ✅ values/strings.xml | Complete | 100% |
| English | ✅ values-en/strings.xml | Complete | 100% |
| Russian | ❌ Not started | Planned | 0% |
| Tajik | ❌ Not started | Future | 0% |

---

## 🚀 Adding Translator Support

For future translations, create spreadsheet:

| String Key | Uzbek | English | Russian | Comment |
|------------|-------|---------|---------|---------|
| app_name | SmartSorovnoma | SmartSorovnoma | SmartSorovnoma | App name |
| back | Orqaga | Back | Назад | Button label |
| required_field | Bu savol majburiy | This field is required | Это поле обязательно | Error message |

---

## ✅ Translation Checklist

Before deploying app with new language:

- [ ] All strings translated (no English fallback)
- [ ] Strings fit in UI (some languages are longer)
- [ ] Right-to-left languages handled (Dari, Arabic)
- [ ] Date/time formats localized
- [ ] Number formats correct (decimal separators)
- [ ] Tested on device with that language
- [ ] No hardcoded strings in code
- [ ] String resources organized by category

---

## 📚 Best Practices

1. **Never hardcode strings** - Always use resources
2. **Use descriptive keys** - `required_field` not `error1`
3. **Group by category** - UI, messages, errors
4. **Keep translations concise** - Mobile screens are small
5. **Test with different languages** - Check UI layout
6. **Review with native speakers** - Ensure quality
7. **Update all languages** - When adding/changing strings
8. **Use proper formatting** - %s for strings, %d for numbers

---

## 🔗 Resources

- [Android Localization Guide](https://developer.android.com/guide/topics/resources/localization)
- [String Resources](https://developer.android.com/guide/topics/resources/string-resource)
- [Plurals](https://developer.android.com/guide/topics/resources/string-resource#Plurals)
- [Android Studio Translations Editor](https://developer.android.com/studio/write/translations-editor)

---

## 📞 Support

For translation questions:
- Check existing strings in `values/strings.xml`
- Use Android Studio's Translations Editor
- Test on device with target language

---

**Last Updated**: 2026-01-22  
**Version**: 1.0
