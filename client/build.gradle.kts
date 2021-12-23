plugins {
  application
  id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
  modules("javafx.base", "javafx.controls", "javafx.graphics")
}

application {
  mainModule.set("dev.omaremara.auctionsystem.client")
  mainClass.set("dev.omaremara.auctionsystem.client.Main")
  applicationDefaultJvmArgs = System.getProperties().
    filter({(key, value) -> key.toString().startsWith("auctionSystem")}).
    map({(key, value) -> "-D$key=$value"})
}

tasks.withType(JavaCompile::class) {
  options.compilerArgs.add("-Xlint:unchecked")
  options.compilerArgs.add("-Xlint:deprecation")
}

dependencies {
  implementation(project(":model"))
  implementation("com.google.code.gson:gson:2.8.9")
}
