# This is a configuration file for R8

-verbose
-allowaccessmodification
-repackageclasses

# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# We only need to keep ComposeView + FragmentContainerView
-keep public class androidx.compose.ui.platform.ComposeView {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# For enumeration classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# AndroidX + support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn androidx.**

-keepattributes SourceFile,
                LineNumberTable,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                AnnotationDefault

-renamesourcefileattribute SourceFile

-dontwarn org.conscrypt.**

# Dagger
-dontwarn com.google.errorprone.annotations.*

-keepclassmembers class com.kafka.data.entities.** { *; }
-keepclassmembers class org.kafka.navigation.graph.** { *; }

-keep class com.kafka.data.entities.** {
    <fields>;
    <init>(...);
}
-keep class com.kafka.*.model.** {
    <fields>;
    <init>(...);
}
-keep class com.kafka.*.model.** {
    <fields>;
    <init>(...);
}
-keep class com.kafka.*.enums.** {
    <fields>;
    <init>(...);
}
-keep class org.kafka.navigation.graph.** {
    <fields>;
    <init>(...);
}

-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

# Retain the generic signature of retrofit2.Call until added to Retrofit.
# Issue: https://github.com/square/retrofit/issues/3580.
# Pull request: https://github.com/square/retrofit/pull/3579.
-keep,allowobfuscation,allowshrinking class retrofit2.Call

# Retain annotation default values for all annotations.
# Required until R8 version >= 3.1.12+ (in AGP 7.1.0+).
-keep,allowobfuscation,allowshrinking @interface *

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** {
*;
}

-keep class androidx.startup.AppInitializer
-keep class * extends androidx.startup.Initializer

# Kotlin serialization looks up the generated serializer classes through a function on companion
# objects. The companions are looked up reflectively so we need to explicitly keep these functions.
-keepclasseswithmembers class **.*$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
# If a companion has the serializer function, keep the companion field on the original type so that
# the reflective lookup succeeds.
-if class **.*$Companion {
  kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class <1>.<2> {
  <1>.<2>$Companion Companion;
}

-keepattributes SourceFile,LineNumberTable

-keep class app.rive.runtime.** { *; }

-keep class com.shockwave.**

-if public class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt {
    public static *** getLocalLifecycleOwner();
}
-keep public class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt {
    public static *** getLocalLifecycleOwner();
}
-dontwarn org.slf4j.impl.StaticLoggerBinder