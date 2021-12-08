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
}

dependencies {
  implementation(project(":model"))
  implementation("com.google.code.gson:gson:2.8.9")
}
