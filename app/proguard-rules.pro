# Privacy Monitor Proguard Obfuscation Rules

# Keep Kotlin Serialization annotations & serializable fields
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
    @kotlinx.serialization.Serializer <fields>;
}
-dontwarn kotlinx.serialization.**

# Keep Room Database schema & entities
-keepclassmembers class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# Keep Hilt DI modules
-keep class **_HiltModules** { *; }
-keep class **_Factory { *; }
-dontwarn dagger.hilt.**

# Obfuscate internal application package names, classes, methods, and fields
-repackageclasses 'a'
-allowaccessmodification
