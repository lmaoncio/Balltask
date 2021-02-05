package com.josetorres.balltask.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientIdentifier implements Runnable {
    private Channel channel;
    private Socket socket;
    private Thread clientIdentifierThread;

    public ClientIdentifier(Channel channel, Socket socket) {
        this.channel = channel;
        this.socket = socket;
        this.clientIdentifierThread = new Thread(this);
        this.clientIdentifierThread.start();
    }

    public void run() {
        boolean check = false;
        try {
            while (!check) {

                this.channel.setConnection(true);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                String data = dataInputStream.readUTF();

                if (data.equals("BALLTASK")) {
                    System.out.println("CI: GOT A BALLTASK REQUEST");
                    data = "OK";
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(data);
                    dataOutputStream.flush();

                    this.channel.setChannelStatus(socket);
                    this.channel.setConnection(false);
                    check = true;
                    System.out.println("CI: SETTING CHANNEL AND SENDING OK MESSAGE");
                } else {
                    check = true;
                    this.channel.setConnection(false);
                    System.out.println("CI: YOU ARE NOT IDENTIFIED AS BALLTASK");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
