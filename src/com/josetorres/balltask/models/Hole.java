package com.josetorres.balltask.models;

import com.josetorres.balltask.controllers.BallTask;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Hole implements Runnable {
    private static final String NAME = "holeThread";
    private String status = "NORMAL";
    private LinkedList<BlackHole> blackHoleList;
    private BallTask ballTask;
    private Thread holeThread;
    private int x, y;
    private int angleX = 1, angleY = 1;
    private Color color;
    private Rectangle rectangle;
    private int rectangleSize = (int) (Math.random() * (200 - 50) + 50);
    private String direction;

    public Hole(LinkedList<BlackHole> blackHoleList, BallTask ballTask, int x, int y) {
        this.blackHoleList = blackHoleList;
        this.ballTask = ballTask;
        this.holeThread = new Thread(this, NAME);
        this.x = x;
        this.y = y;
        this.color = Color.black;
        this.rectangle = new Rectangle(x, y, rectangleSize, rectangleSize);
    }

    @Override
    public void run() {
        while (!status.equals("STOP")) {
            try {
                go();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void go() throws InterruptedException {
        ballTask.analyzeStatus(this);
        if (status.equals("NORMAL")) {
            move(5);
        }
        if (status.equals("SLOW")) {
            BlackHole blackHoleIntersected = null;
            for (BlackHole blackHole : blackHoleList) {
                if (this.getRectangle().intersects(blackHole.getRectangle())) {
                    blackHoleIntersected = blackHole;
                }
            }

            if (blackHoleIntersected != null) {
                blackHoleIntersected.put();
                while (this.getRectangle().intersects(blackHoleIntersected.getRectangle())) {
                    while (status.equals("PAUSE")) {
                        Thread.sleep(50);
                    }
                    move(50);
                    this.setColor(Color.red);
                }
                if (blackHoleIntersected.getAmount() == 1) {
                    blackHoleIntersected.get();
                    this.setColor(Color.black);
                }
            }
        }
        if (status.equals("SEND")) {
            ballTask.getChannel().send(this);
        }
        if (status.equals("PAUSE")) {
            while (status.equals("PAUSE")) {
                Thread.sleep(50);
            }
        }
    }

    private void move(int speed) {
        if (x + angleX < 0) {
            angleX = 1;
        } else if (x + angleX > ballTask.getView().getWidth() - 50) {
            angleX = -1;
        } else if (y + angleY < 0) {
            angleY = 1;
        } else if (y + angleY > ballTask.getView().getHeight() - 50) {
            angleY = -1;
        }
        x = x + angleX;
        y = y + angleY;
        rectangle.setRect(x, y, rectangleSize, rectangleSize);

        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRectangleSize() {
        return rectangleSize;
    }

    public void setRectangleSize(int rectangleSize) {
        this.rectangleSize = rectangleSize;
    }

    public Thread getHoleThread() {
        return holeThread;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getNAME() {
        return NAME;
    }

    public List<BlackHole> getBlackHoleList() {
        return blackHoleList;
    }

    public void setBlackHoleList(LinkedList<BlackHole> blackHoleList) {
        this.blackHoleList = blackHoleList;
    }

    public BallTask getBallTask() {
        return ballTask;
    }

    public void setBallTask(BallTask ballTask) {
        this.ballTask = ballTask;
    }

    public void setHoleThread(Thread holeThread) {
        this.holeThread = holeThread;
    }

    public int getAngleX() {
        return angleX;
    }

    public void setAngleX(int angleX) {
        this.angleX = angleX;
    }

    public int getAngleY() {
        return angleY;
    }

    public void setAngleY(int angleY) {
        this.angleY = angleY;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

