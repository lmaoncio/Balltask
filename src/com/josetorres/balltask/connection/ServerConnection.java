package com.josetorres.balltask.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private ServerSocket serverSocket;
    private final Channel channel;

    public ServerConnection(Channel channel) {
        Thread serverThread = new Thread(this);

        try {
            this.serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            System.out.println("SERVER: ERROR CREATING SERVER SOCKET");
        }

        this.channel = channel;
        serverThread.start();
    }

    public void run() {
        try {
            while(true) {
                Thread.sleep(200);
                while (!this.channel.isStatus()) {
                    Socket socket = serverSocket.accept();
                    new ClientIdentifier(channel, socket);
                    this.channel.setDirection("RIGHT");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("SERVER: ERROR CREATING CONNECTION SOCKET");
        }
    }
}

