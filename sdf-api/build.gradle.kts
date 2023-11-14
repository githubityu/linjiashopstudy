import org.jetbrains.kotlin.ir.backend.jvm.jvmResolveLibraries

//dependencies {
//    implementation libraries['spring-boot-starter-web']
//    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
//    implementation project(':common-core')
//}
dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.alibaba:druid-spring-boot-3-starter:1.2.20")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation(project(":common-core"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(project(":common-security"))

}