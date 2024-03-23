@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.app.android.library)
  alias(libs.plugins.ktLint)
}

android {
  namespace = "me.danlowe.models"

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.kotlinx.collections.immutable)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
}
