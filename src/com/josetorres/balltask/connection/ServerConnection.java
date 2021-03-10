package com.josetorres.balltask.connection;

import com.josetorres.balltask.controllers.BallTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private ServerSocket serverSocket;
    private final Channel channel;
    private BallTask ballTask;

    public ServerConnection(Channel channel, BallTask ballTask) {
        Thread serverThread = new Thread(this);
        this.ballTask = ballTask;
        try {
            this.serverSocket = new ServerSocket(5000);
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

                    String IP = socket.getInetAddress().getHostAddress();
                    System.out.println(IP);
                    ballTask.getClientConnection().setIP(IP);

                    new ClientIdentifier(channel, socket);
                    this.channel.setDirection("RIGHT");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("SERVER: ERROR CREATING CONNECTION SOCKET");
        }
    }
}

