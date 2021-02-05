package com.josetorres.balltask.connection;

import java.io.IOException;

public class HealthChecker implements Runnable {
    private boolean health;
    private final Channel channel;

    public HealthChecker(Channel channel) {
        this.channel = channel;
        this.health = true;
        Thread healthCheckerThread = new Thread(this);
        healthCheckerThread.start();
    }

    @Override
    public void run() {
        while (channel.isStatus()) {
            System.out.println("HEALTH: SENDING ACK");
            int count = 0;
            health = false;
            channel.ACKCheck();
            while (count < 5 && !this.health) {
                try {
                    Thread.sleep(500);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!health) {
                System.out.println("HEALTH: ACK OK NOT RECEIVED");
                channel.setStatus(false);
            }
        }
    }

    public boolean isHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }
}
