import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.JavaExec
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

description = "JSON library written in pure Kotlin"


plugins {
	//maven
	//`maven-publish`
	//signing

	// testing
	jacoco
	id("org.junit.platform.gradle.plugin") version "1.0.2"
}

dependencies {
	api(kotlin("reflect"))

	api(project(":basic"))
}


// testing

junitPlatform {
	platformVersion = "1.0.2"

	filters {
		engines {
			include("spek")
		}
	}
}

afterEvaluate {
	val junitPlatformTest: JavaExec by tasks

	jacoco {
		applyTo(junitPlatformTest)
	}

	task<JacocoReport>("${junitPlatformTest.name}Report") {
		executionData(junitPlatformTest)

		val sourceSet = java.sourceSets["main"]
		sourceDirectories = files(sourceSet.allJava.srcDirs)
		classDirectories = files(sourceSet.output)

		reports {
			xml.destination = file("$buildDir/reports/jacoco.xml")
			xml.isEnabled = true
			html.isEnabled = false
		}

		tasks["check"].dependsOn(this)
	}
}

dependencies {
	testImplementation("com.winterbe:expekt:0.5.0")
	testImplementation("org.jetbrains.spek:spek-subject-extension:1.1.5")
	testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
	testRuntime("org.junit.platform:junit-platform-runner:${junitPlatform.platformVersion}")
}


fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}


/*
// publishing

val javadoc = tasks["javadoc"] as Javadoc
val javadocJar by tasks.creating(Jar::class) {
	classifier = "javadoc"
	from(javadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
	classifier = "sources"
	from(java.sourceSets["main"].allSource)
}

publishing {
	(publications) {
		"default".invoke(MavenPublication::class) {
			from(components["java"])
			artifact(sourcesJar)
		}
	}
}

val ossrhUserName = findProperty("ossrhUserName") as String?
val ossrhPassword = findProperty("ossrhPassword") as String?
if (ossrhUserName != null && ossrhPassword != null) {
	artifacts {
		add("archives", javadocJar)
		add("archives", sourcesJar)
	}

	signing {
		sign(configurations.archives)
	}

	tasks {
		"uploadArchives"(Upload::class) {
			repositories {
				withConvention(MavenRepositoryHandlerConvention::class) {
					mavenDeployer {
						withGroovyBuilder {
							"beforeDeployment" { signing.signPom(delegate as MavenDeployment) }

							"repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
								"authentication"("userName" to ossrhUserName, "password" to ossrhPassword)
							}

							"snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots/") {
								"authentication"("userName" to ossrhUserName, "password" to ossrhPassword)
							}
						}

						pom.project {
							withGroovyBuilder {
								"name"(project.name)
								"description"(project.description)
								"packaging"("jar")
								"url"("https://github.com/fluidsonic/fluid-json")
								"developers" {
									"developer" {
										"id"("fluidsonic")
										"name"("Marc Knaup")
										"email"("marc@knaup.koeln")
									}
								}
								"licenses" {
									"license" {
										"name"("MIT License")
										"url"("https://github.com/fluidsonic/fluid-json/blob/master/LICENSE")
									}
								}
								"scm" {
									"connection"("scm:git:https://github.com/fluidsonic/fluid-json.git")
									"developerConnection"("scm:git:git@github.com:fluidsonic/fluid-json.git")
									"url"("https://github.com/fluidsonic/fluid-json")
								}
							}
						}
					}
				}
			}
		}
	}
}
*/
