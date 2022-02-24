
buildscript {

    val kotlinVersion = "1.3.40"

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("digital.wup:android-maven-publish:3.6.2")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean",type =  Delete::class) {
    delete(rootProject.buildDir)
}
