allprojects {
    repositories {
        google()
        mavenCentral()
        // 添加百度地图SDK的Maven仓库
        // maven { url = uri("https://maven.aliyun.com/repository/google") }
        // maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        // maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        // 替换为百度官方仓库解决401错误
        // maven { url = uri("https://maven.baidu.com/repository/releases") }
    }
}

val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
