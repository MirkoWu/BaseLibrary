# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile





#混淆规则参考 https://www.jianshu.com/p/f3455ecaa56e

# 每个Moudle 配置自己的混淆规则，混淆不会继承

#libraryjars  声明lib jar文件
#dontwarn 不提示警告 dontwarn是一个和keep可以说是形影不离,尤其是处理引入的library时.
#引入的library可能存在一些无法找到的引用和其他问题,在build时可能会发出警告,
#如果我们不进行处理,通常会导致build中止.
#因此为了保证build继续,我们需要使用dontwarn处理这些我们无法解决的library的警告.

#keep 保留类和类中的成员，防止被混淆或移除
#keepnames 保留类和类中的成员，防止被混淆，成员没有被引用会被移除
#keepclassmembers 只保留类中的成员，防止被混淆或移除
#keepclassmembernames 只保留类中的成员，防止被混淆，成员没有引用会被移除
#keepclasseswithmembers 保留类和类中的成员，防止被混淆或移除，保留指明的成员
#keepclasseswithmembernames 保留类和类中的成员，防止被混淆，保留指明的成员，成员没有引用会被移除



# >>>>>>>>>>>>>>>----------基本不用动区域---------->>>>>>>>>>>>>>>

#----------------------基本指令区-----------------
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
# 混淆后类名都为小写   Aa aA
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#不做预校验的操作
-dontpreverify
# 混淆时不记录日志
-verbose
# 混淆采用的算法.
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保留代码行号，方便异常信息的追踪
-keepattributes SourceFile,LineNumberTable

#dump文件列出apk包内所有class的内部结构
-dump class_files.txt
#seeds.txt文件列出未混淆的类和成员
-printseeds seeds.txt
#usage.txt文件列出从apk中删除的代码
-printusage unused.txt
#mapping文件列出混淆前后的映射
-printmapping mapping.txt

#避免混淆Android基本组件，下面是兼容性比较高的规则
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#不提示V4包下错误警告
-dontwarn android.support.v4.**
#保持下面的V4兼容包的类不被混淆
-keep class android.support.v4.**{*;}
#保持Activity中与View相关方法不被混淆
-keepclassmembers class * extends android.app.Activity{
        public void *(android.view.View);
}

#避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
        native <methods>;
}

#避免混淆自定义控件类的get/set方法和构造函数
-keep public class * extends android.view.View{
        *** get*();
        void set*(***);
        public <init>(android.content.Context);
        public <init>(android.content.Context,android.util.AttributeSet);
        public <init>(android.content.Context,android.util.AttributeSet,int);
}
-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
}

#避免混淆枚举类
-keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
}

#避免混淆序列化类
#不混淆Parcelable和它的实现子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
}

#不混淆Serializable和它的实现子类、其成员变量
-keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
}

-keep class **.R$* {*;}
-keepclassmembers class * {
        void *(**On*Event);
}

#Webview 相关不混淆
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
        public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
        public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
        public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
        public void *(android.webkit.WebView, jav.lang.String);
 }

 #使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
 -keepclassmembers class * {
         public <init>(org.json.JSONObject);
 }

#不混淆泛型
-keepattributes Signature

#避免混淆注解类
-dontwarn android.annotation
-keepattributes *Annotation*

#避免混淆内部类
-keepattributes InnerClasses

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
        public static *** v(...);
        public static *** d(...);
        public static *** i(...);
        public static *** w(...);
}
# <<<<<<<<<<<<<<<----------基本不用动区域----------<<<<<<<<<<<<<<<


# >>>>>>>>>>>>>>>----------1.实体类---------->>>>>>>>>>>>>>>
#BaseBean
-keep class com.softgarden.baselibrary.network.**{*;}

# <<<<<<<<<<<<<<<----------1.实体类----------<<<<<<<<<<<<<<<


# >>>>>>>>>>>>>>>----------2.第三方包---------->>>>>>>>>>>>>>>
# ==================okhttp start===================
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# ==================okhttp end=====================

# ==================retrofit2 start===================
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# ==================retrofit2 end=====================

# ==================logger start=====================
-dontwarn com.orhanobut.logger.**
-keep class com.orhanobut.logger.**{*;}
-keep interface com.orhanobut.logger.**{*;}
# ==================logger end=====================

# ==================gson start=====================
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep interface com.google.gson.**{*;}
# ==================gson end=====================

# ==================BaseQuickAdapter start===================
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
-keepclassmembers class * extends com.chad.library.adapter.base.BaseViewHolder { <init>(...); }
# ==================BaseQuickAdapter end=====================

# ==================butterknife start===================
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# ==================butterknife end=====================

# ==================glide start===================
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#glide如果你的API级别<=Android API 27 则需要添加
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
# ==================glide end=====================

# ==================rxpermissions2 start===================
-dontwarn com.tbruyelle.**
# ==================rxpermissions2 start===================

# <<<<<<<<<<<<<<<----------2.第三方包----------<<<<<<<<<<<<<<<


# >>>>>>>>>>>>>>>----------3.与js互相调用的类---------->>>>>>>>>>>>>>>

# <<<<<<<<<<<<<<<----------3.与js互相调用的类----------<<<<<<<<<<<<<<<


# >>>>>>>>>>>>>>>----------4.反射相关的类和方法---------->>>>>>>>>>>>>>>

# <<<<<<<<<<<<<<<----------4.反射相关的类和方法----------<<<<<<<<<<<<<<<
