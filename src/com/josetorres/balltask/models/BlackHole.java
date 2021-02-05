package com.josetorres.balltask.models;

import java.awt.*;

public class BlackHole {
    public static final int MAX_AMOUNT = 1;
    public static final int MIN_AMOUNT = 0;
    private int amount = 0;
    private int x, y;
    private Color color;
    private Rectangle rectangle;

    public BlackHole(int x , int y) {
        this.x = x;
        this.y = y;
        this.color = Color.yellow;
        this.rectangle = new Rectangle(x,y,125,125);
    }
    public synchronized void get()  {
        amount--;
        notifyAll();
    }

    public synchronized void put() throws InterruptedException {
        while (amount >= MAX_AMOUNT) {
            wait();
        }
        amount++;
        notifyAll();
    }
    public int getAmount() {
        return amount;
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
}
