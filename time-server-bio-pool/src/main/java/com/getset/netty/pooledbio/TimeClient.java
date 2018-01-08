package com.getset.netty.pooledbio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("QUERY TIME ORDER");
            System.out.println("Send order to Timer Server successfully.");
            String repo = in.readLine();
            System.out.println("Now is " + repo);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
