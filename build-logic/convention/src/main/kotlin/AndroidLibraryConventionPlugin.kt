
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import me.danlowe.buildlogic.configureKotlinAndroid
import me.danlowe.buildlogic.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("org.jlleitschuh.gradle.ktlint")
      }
      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = 34
      }
      extensions.configure<LibraryAndroidComponentsExtension> {
        disableUnnecessaryAndroidTests(target)
      }
      dependencies {
        add("testImplementation", kotlin("test"))
      }
    }
  }
}