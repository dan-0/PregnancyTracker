@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.app.android.library)
  alias(libs.plugins.app.android.library.compose)
}

android {
  namespace = "me.danlowe.utils"

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)

  implementation(libs.androidx.compose.ui.ui)
}
