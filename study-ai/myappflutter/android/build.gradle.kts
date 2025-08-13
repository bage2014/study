
// 添加必要的导入
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.tasks.Delete
import java.io.File

allprojects {
    repositories {
        google()
        mavenCentral()
        // 添加百度地图SDK的Maven仓库
        maven { url = uri("https://maven.baidu.com/repository/releases") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
    subprojects {
        afterEvaluate {
            if (hasProperty("android")) {
                val androidExtension = extensions.findByName("android")
                if (androidExtension != null && androidExtension is com.android.build.gradle.BaseExtension) {
                    if (androidExtension.namespace == null) {
                        androidExtension.namespace = group.toString() // Set namespace as fallback
                    }
                    
                    tasks.whenTaskAdded {
                        if (name.contains("processDebugManifest") || name.contains("processReleaseManifest")) {
                            doFirst {
                                val manifestFile = file("${projectDir}/src/main/AndroidManifest.xml")
                                if (manifestFile.exists()) {
                                    var manifestContent = manifestFile.readText()
                                    if (manifestContent.contains("package=")) {
                                        manifestContent = manifestContent.replace(Regex("package=\"[^\"]*\""), "")
                                        manifestFile.writeText(manifestContent)
                                        println("Removed 'package' attribute from ${manifestFile}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
}


val newBuildDir: File = rootProject.layout.buildDirectory.dir("../../build").get().asFile
rootProject.layout.buildDirectory.set(newBuildDir)

subprojects {
    val newSubprojectBuildDir: File = newBuildDir.resolve(project.name)
    project.layout.buildDirectory.set(newSubprojectBuildDir)
}

subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
