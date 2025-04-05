buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    }

}
plugins{
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
