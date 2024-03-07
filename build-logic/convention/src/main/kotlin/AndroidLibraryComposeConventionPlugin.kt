
import com.android.build.gradle.LibraryExtension
import me.danlowe.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jlleitschuh.gradle.ktlint")
      }

      val extension = extensions.getByType<LibraryExtension>()
      configureAndroidCompose(extension)
    }
  }

}