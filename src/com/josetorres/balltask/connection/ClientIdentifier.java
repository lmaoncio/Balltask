package com.josetorres.balltask.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientIdentifier implements Runnable {
    private final Channel channel;
    private final Socket socket;

    public ClientIdentifier(Channel channel, Socket socket) {
        this.channel = channel;
        this.socket = socket;
        Thread clientIdentifierThread = new Thread(this);
        clientIdentifierThread.start();
    }

    public void run() {
        boolean check = false;
        try {
            while (!check) {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String data = dataInputStream.readUTF();
                if (data.equals("BALLTASK")) {
                    this.channel.setChannelStatus(socket);
                    DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
                    dataOutputStream.writeUTF("OK");
                }
                check = true;
            }
        } catch (IOException e) {
            System.out.println("CLIENT IDENTIFIER: ERROR IDENTIFYING");
        }
    }
}
