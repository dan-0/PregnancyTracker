plugins {
  alias(libs.plugins.app.android.application)
  alias(libs.plugins.app.android.application.compose)
  alias(libs.plugins.kotlin)
}

android {
  namespace = "me.danlowe.pregnancytracker"

  defaultConfig {
    applicationId = "me.danlowe.pregnancytracker"
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":database"))
  implementation(project(":models"))

  testImplementation(project(":testutils"))

  androidTestImplementation(project(":testutils"))

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

  // Lint
  ktlintRuleset(libs.composeKtlint)

  // images
  implementation(libs.coil.compose)
}
