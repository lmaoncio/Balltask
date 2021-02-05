package com.josetorres.balltask.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ClientConnection implements Runnable {
    private final Channel channel;
    private String IP;
    private Thread clientConnectionThread;

    public ClientConnection(Channel channel, String IP) {
        this.channel = channel;
        this.IP = IP;
        this.clientConnectionThread = new Thread(this);
        this.clientConnectionThread.start();
    }

    public void run() {
        try {
            Socket clientSocket = null;

            while (true) {
                if (!this.channel.isStatus()) {
                    clientSocket = new Socket(IP, 8000);

                    System.out.println("CLIENT: SENDING BALLTASK REQUEST");
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    String data = "BALLTASK";
                    dataOutputStream.writeUTF(data);

                    DataInputStream dataInputStream = new DataInputStream((clientSocket.getInputStream()));
                    String response = dataInputStream.readUTF();

                    if (!this.channel.isStatus() && response.equals("OK")) {
                        this.channel.setChannelStatus(clientSocket);
                        System.out.println("CLIENT: CI OK SETTING CHANNEL");
                    }
                }
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("CLIENT: ERROR CONNECTING");
        }
    }
}