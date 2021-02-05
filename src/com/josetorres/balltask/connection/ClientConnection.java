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
                if (!this.channel.isStatus() && !this.channel.isConnection()) {
                    DataInputStream dataInputStream = null;
                    clientSocket = new Socket(IP, 8000);
                    System.out.println("CLIENT: TRYING TO CONNECT");

                    if (clientSocket != null) {

                        this.channel.setConnection(true);
                        System.out.println("CLIENT: SENDING BALLTASK REQUEST");

                        String data = "BALLTASK";
                        OutputStream outputStream = clientSocket.getOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                        dataOutputStream.writeUTF(data);
                        dataOutputStream.flush();

                        boolean check = false;

                        while (!check) {
                            if (dataInputStream == null) {
                                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                            }

                            data = dataInputStream.readUTF();

                            if (data.equals("OK")) {
                                this.channel.setConnection(false);
                                this.channel.setChannelStatus(clientSocket);
                                check = true;
                                System.out.println("CLIENT: CI OK SETTING CHANNEL");
                            } else {
                                this.channel.setConnection(false);
                                check = true;

                                System.out.println("CLIENT: ERROR IDENTIFYING");
                            }
                        }
                    }
                    Thread.sleep(200);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("CLIENT: ERROR CONNECTING" + e);
        }
    }
}