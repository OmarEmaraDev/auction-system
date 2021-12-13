plugins {
  application
}

application {
  mainModule.set("dev.omaremara.auctionsystem.server")
  mainClass.set("dev.omaremara.auctionsystem.server.Main")
  applicationDefaultJvmArgs = System.getProperties().
    filter({(key, value) -> key.toString().startsWith("auctionSystem")}).
    map({(key, value) -> "-D$key=$value"})
}

dependencies {
  implementation(project(":model"))
  implementation("com.google.code.gson:gson:2.8.9")
  implementation("org.postgresql:postgresql:42.3.1")
}
