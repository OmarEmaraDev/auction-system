package dev.omaremara.auctionsystem.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static final Gson gson = new Gson();

  public static void main(String[] args) {
    try {
      int port = Integer.getInteger("auctionSystem.port");
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      int processorsCount = Runtime.getRuntime().availableProcessors();
      ExecutorService executor = Executors.newFixedThreadPool(processorsCount);
      server.setExecutor(executor);

      // TODO: Create approperiate contexts.

      server.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
