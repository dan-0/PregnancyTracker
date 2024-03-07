@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.app.android.library)
}

android {
  namespace = "me.danlowe.testutils"
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":database"))

  implementation(libs.sqlite.driver)

  implementation(libs.androidx.core.ktx)
}
