module dev.omaremara.auctionsystem.client {
  requires java.base;
  requires javafx.base;
  requires javafx.controls;
  requires javafx.graphics;
  requires dev.omaremara.auctionsystem.model;
  exports dev.omaremara.auctionsystem.client to javafx.graphics;
}
