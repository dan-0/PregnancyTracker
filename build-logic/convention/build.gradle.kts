import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "me.danlowe.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "app.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationCompose") {
      id = "app.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "app.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "app.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidTest") {
      id = "app.android.test"
      implementationClass = "AndroidTestConventionPlugin"
    }
  }
}