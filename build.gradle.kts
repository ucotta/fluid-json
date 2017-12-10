import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


description = "JSON library written in pure Kotlin"


plugins {
	base
	`java-library`
	kotlin("jvm") version "1.2.0" apply false
	id("com.github.ben-manes.versions") version "0.17.0"
}


allprojects {
	group = "com.github.fluidsonic"
	version = "0.9.1"
}

subprojects {
	apply {
		plugin(JavaLibraryPlugin::class.java)
		plugin(KotlinPlatformJvmPlugin::class.java)
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8

		sourceSets {
			"main" {
				kotlin.setSrcDirs(listOf("Sources"))
				resources.setSrcDirs(listOf("Resources"))
			}

			"test" {
				kotlin.setSrcDirs(listOf("Tests/Sources"))
				resources.setSrcDirs(listOf("Tests/Resources"))
			}
		}
	}

	tasks {
		withType<KotlinCompile> {
			sourceCompatibility = "1.8"
			targetCompatibility = "1.8"

			kotlinOptions.jvmTarget = "1.8"
		}
	}

	dependencies {
		api(kotlin("stdlib-jre8"))
	}

	configurations.all {
		resolutionStrategy.apply {
			force("org.jetbrains.kotlin:kotlin-reflect:1.2.0")
			force("org.jetbrains.kotlin:kotlin-stdlib:1.2.0")
			force("org.jetbrains.kotlin:kotlin-stdlib-jre7:1.2.0")
			force("org.jetbrains.kotlin:kotlin-stdlib-jre8:1.2.0")

			failOnVersionConflict()
		}
	}

	repositories {
		jcenter()
		mavenCentral()
	}
}

val SourceSet.kotlin get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin
