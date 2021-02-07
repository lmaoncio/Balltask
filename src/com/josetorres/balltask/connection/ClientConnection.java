package com.josetorres.balltask.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private final Channel channel;
    private final String IP;

    public ClientConnection(Channel channel, String IP) {
        this.channel = channel;
        this.IP = IP;
        Thread clientConnectionThread = new Thread(this);
        clientConnectionThread.start();
    }

    public void run() {
        try {
            Socket clientSocket;

            while (true) {
                if (!this.channel.isStatus()) {
                    clientSocket = new Socket(IP, 8000);
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    String data = "BALLTASK";
                    dataOutputStream.writeUTF(data);

                    DataInputStream dataInputStream = new DataInputStream((clientSocket.getInputStream()));
                    String response = dataInputStream.readUTF();

                    if (!this.channel.isStatus() && response.equals("OK")) {
                        this.channel.setChannelStatus(clientSocket);
                        this.channel.setDirection("LEFT");
                    }
                }
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("CLIENT: ERROR CONNECTING");
        }
    }
}