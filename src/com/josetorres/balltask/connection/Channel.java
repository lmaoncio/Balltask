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
    private Hole hole;
    private boolean status;
    private boolean connection;
    private Thread channelThread;
    private final BallTask ballTask;
    private HealthChecker healthChecker;

    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
        this.status = false;
        this.connection = false;
        this.channelThread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = null;
            while (status) {
                if (dataInputStream == null) {
                    dataInputStream = new DataInputStream(channelSocket.getInputStream());
                }
                System.out.println("CHANNEL: READING DATA");

                String data = dataInputStream.readUTF();
                String[] dataList = data.split("\\.");

                if (dataList[0].equals("HOLE")) {
                    System.out.println("CHANNEL: GOT HOLE");
                    ballTask.createHole(
                            dataList[1],
                            dataList[2],
                            dataList[3],
                            dataList[4],
                            dataList[7],
                            dataList[8]);
                } else if (dataList[0].equals("ACK")) {
                    System.out.println("CHANNEL: GOT ACK");
                    data = "ACK OK";
                    OutputStream outputStream = this.getChannelSocket().getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(data);
                    dataOutputStream.flush();
                    System.out.println("CHANNEL: SENT OK");
                } else if (dataList[0].equals("ACK OK")) {
                    System.out.println("CHANNEL: GOT ACK OK");
                    this.healthChecker.setHealth(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.status = false;
        }
    }

    public void ACKCheck() {
        try {
            String data = "ACK";
            DataOutputStream dataOutputStream = new DataOutputStream(this.channelSocket.getOutputStream());
            dataOutputStream.writeUTF(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            dataOutputStream.flush();
            ballTask.removeHole(hole);
            hole.setStatus("STOP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public Hole getHole() {
        return hole;
    }

    public void setHole(Hole hole) {
        this.hole = hole;
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

    public void setChannelSocket(Socket channelSocket) {
        this.channelSocket = channelSocket;
    }

}

