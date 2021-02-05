package com.josetorres.balltask.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private ServerSocket serverSocket;
    private Channel channel;
    private Thread serverThread;

    public ServerConnection(Channel channel) {
        this.serverThread = new Thread(this);

        try {
            this.serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.channel = channel;
        this.serverThread.start();
    }

    public void run() {
        try {
            while(true) {
                while (!channel.isStatus()) {
                    Socket socket = serverSocket.accept();
                    System.out.println("SERVER: GOT CONNECTION REQUEST");
                    new ClientIdentifier(channel, socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread getServerThread() {
        return serverThread;
    }

    public void setServerThread(Thread serverThread) {
        this.serverThread = serverThread;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

