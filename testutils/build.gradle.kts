@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.com.android.library)
  alias(libs.plugins.kotlin)
}

android {
  namespace = "me.danlowe.testutils"
  compileSdk = 34

  defaultConfig {
    minSdk = 29

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.forClassVersion(libs.versions.javaVersion.get().toInt())
    targetCompatibility = JavaVersion.forClassVersion(libs.versions.javaVersion.get().toInt())
  }
  kotlinOptions {
    jvmTarget = JavaVersion.forClassVersion(libs.versions.javaVersion.get().toInt()).toString()
  }
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":database"))

  implementation(libs.sqlite.driver)

  implementation(libs.androidx.core.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}