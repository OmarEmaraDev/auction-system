package dev.omaremara.auctionsystem.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import dev.omaremara.auctionsystem.server.contexts.LoginContext;
import dev.omaremara.auctionsystem.server.contexts.NewBidContext;
import dev.omaremara.auctionsystem.server.contexts.NewLotContext;
import dev.omaremara.auctionsystem.server.contexts.RegisterContext;
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

      server.createContext("/api/login/", new LoginContext());
      server.createContext("/api/register/", new RegisterContext());
      server.createContext("/api/new/lot/", new NewLotContext());
      server.createContext("/api/new/bid/", new NewBidContext());

      server.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
