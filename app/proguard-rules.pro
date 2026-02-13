# Add project specific ProGuard rules here.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# CameraX
-keep class androidx.camera.** { *; }
