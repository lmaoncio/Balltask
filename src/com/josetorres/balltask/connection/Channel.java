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
    private Thread channelThread;
    private final BallTask ballTask;
    private HealthChecker healthChecker;

    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
        this.status = false;
        this.channelThread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = null;
            while (this.status) {
                System.out.println("CHANNEL: READING DATA");
                dataInputStream = new DataInputStream(channelSocket.getInputStream());
                String data = dataInputStream.readUTF();

                if (data == null) {
                    System.out.println("CHANNEL: NO DATA");
                    this.status = false;
                } else if (data.split(",")[0].equals("HOLE")) {
                    System.out.println("CHANNEL: GOT HOLE");
                    ballTask.createHole(
                            data.split(",")[1],
                            data.split(",")[2],
                            data.split(",")[3],
                            data.split(",")[4],
                            data.split(",")[7],
                            data.split(",")[8]);
                } else if (data.equals("ACK")) {
                    System.out.println("CHANNEL: GOT ACK");
                    DataOutputStream dataOutputStream = new DataOutputStream(this.channelSocket.getOutputStream());
                    dataOutputStream.writeUTF("OK");
                    System.out.println("CHANNEL: SENT OK");
                } else if (data.equals("OK")) {
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
        } catch (IOException e) {
            this.status = false;
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
            ballTask.removeHole(hole);
            hole.setStatus("STOP");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

