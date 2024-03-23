pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "PregnancyTracker"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":testutils")
include(":database")
include(":utils")
include(":models")
