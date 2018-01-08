package com.getset.netty.pooledbio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("The server is start on port " + port);
            ExecutorService threadPool = Executors.newFixedThreadPool(50);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                threadPool.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("The server stopped.");
        }
    }
}
