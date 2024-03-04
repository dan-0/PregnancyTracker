plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ksp)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.ktLint)
}

android {
  namespace = "me.danlowe.pregnancytracker"
  compileSdk = 34

  defaultConfig {
    applicationId = "me.danlowe.pregnancytracker"
    minSdk = 30
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlinCompiler.get()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

sqldelight {
  databases {
    create("PregnancyTracker") {
      packageName.set("me.danlowe.pregnancytracker")
      this.configurationName
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)

  // Compose
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.kotlinx.collections.immutable)

  // unit tests
  testImplementation(libs.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.kotlinx.coroutines.test)

  // ui tests
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.uiTestJunit4)

  // tooling
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.uiTestManifest)

  // voyager
  implementation(libs.voyager.navigator)
  implementation(libs.voyager.screenModel)
  implementation(libs.voyager.koin)
  implementation(libs.voyager.transitions)

  // DI
  implementation(libs.koin.android)
  implementation(libs.koin.android.compose)

  // DB
  implementation(libs.sqldelight.android.driver)
  implementation(libs.sqldelight.coroutines)
  testImplementation(libs.sqlite.driver)

  // Lint
  ktlintRuleset(libs.composeKtlint)

  // images
  implementation(libs.coil.compose)
}
