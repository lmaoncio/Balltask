package com.josetorres.balltask.connection;

import com.josetorres.balltask.controllers.BallTask;
import com.josetorres.balltask.data.Package;
import com.josetorres.balltask.models.Hole;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Channel implements Runnable {

    private Socket channelSocket;
    private boolean status;
    private Thread channelThread;
    private final BallTask ballTask;
    private HealthChecker healthChecker;
    private String direction;

    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
        this.status = false;
        this.channelThread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream;
            while (this.status) {
                dataInputStream = new DataInputStream(channelSocket.getInputStream());
                String data = dataInputStream.readUTF();

                if (data.split(",")[0].equals("HOLE")) {
                    if (data.split(",")[1].equals("LEFT")) {
                        ballTask.createHoleLeft(
                                data.split(",")[2],
                                data.split(",")[3],
                                data.split(",")[4],
                                data.split(",")[5],
                                data.split(",")[8],
                                data.split(",")[9]);
                    }
                    if (data.split(",")[1].equals("RIGHT")) {
                        ballTask.createHoleRight(
                                data.split(",")[2],
                                data.split(",")[3],
                                data.split(",")[4],
                                data.split(",")[5],
                                data.split(",")[8],
                                data.split(",")[9]);
                    }

                } else if (data.equals("ACK")) {
                    DataOutputStream dataOutputStream = new DataOutputStream(this.channelSocket.getOutputStream());
                    dataOutputStream.writeUTF("OK");
                } else if (data.equals("OK")) {
                    this.healthChecker.setHealth(true);
                }
                System.out.println(direction);
            }

        } catch (IOException e) {
            System.out.println("CHANNEL: ERROR READING");
            this.status = false;
        }
    }

    public void ACKCheck() {
        try {
            String data = "ACK";
            DataOutputStream dataOutputStream = new DataOutputStream(this.channelSocket.getOutputStream());
            dataOutputStream.writeUTF(data);
        } catch (IOException e) {
            this.status = false;
            System.out.println("CHANNEL: ERROR SENDING ACK");
        }
    }

    public synchronized void setChannelStatus(Socket socket) {
        if (!this.status) {
            this.channelSocket = socket;
            this.status = true;
            this.channelThread = new Thread(this);
            this.channelThread.start();
            this.healthChecker = new HealthChecker(this);
        }
    }

    public void send(Hole hole) {
        Package data = new Package(
                direction,
                hole.getX() + "",
                hole.getY() + "",
                hole.getAngleX() + "",
                hole.getAngleY() + "",
                hole.getColor() + "",
                hole.getRectangleSize() + "");
        try {
            OutputStream outputStream = channelSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(data.getDataToTransfer());
            ballTask.removeHole(hole);
            hole.setStatus("STOP");
        } catch (IOException e) {
            System.out.println("HOLE: ERROR SENDING");
        }
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Socket getChannelSocket() {
        return channelSocket;
    }

}

