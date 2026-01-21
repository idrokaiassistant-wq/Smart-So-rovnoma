# SmartSorovnoma ProGuard Rules

# ==========================================
# General Android Rules
# ==========================================
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile

# ==========================================
# Firebase Rules
# ==========================================
# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firestore
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <methods>;
    @com.google.firebase.firestore.PropertyName <fields>;
}

# Keep model classes for Firestore
-keep class com.smartsorovnoma.domain.model.** { *; }
-keepclassmembers class com.smartsorovnoma.domain.model.** { *; }

# ==========================================
# Kotlin Rules
# ==========================================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ==========================================
# Jetpack Compose Rules
# ==========================================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class * {
    @androidx.compose.runtime.Composable *;
}

# ==========================================
# Data Classes & Sealed Classes
# ==========================================
# Keep data classes
-keepclassmembers class * {
    @kotlin.jvm.JvmField <fields>;
}

-keep class com.smartsorovnoma.presentation.viewmodel.AnswerValue { *; }
-keep class com.smartsorovnoma.presentation.viewmodel.AnswerValue$* { *; }

# ==========================================
# ViewModel Rules
# ==========================================
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# ==========================================
# Serialization (for Firestore)
# ==========================================
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ==========================================
# Debugging (keep for crash reports)
# ==========================================
-keepattributes SourceFile,LineNumberTable

# Keep custom exceptions
-keep public class * extends java.lang.Exception

# ==========================================
# Remove Logging (optional - uncomment for production)
# ==========================================
# -assumenosideeffects class android.util.Log {
#     public static *** d(...);
#     public static *** v(...);
#     public static *** i(...);
#     public static *** w(...);
# }
