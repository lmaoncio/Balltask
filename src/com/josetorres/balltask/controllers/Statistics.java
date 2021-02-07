package com.josetorres.balltask.controllers;

import com.josetorres.balltask.views.Control;

public class Statistics implements Runnable {
    private final Control control;
    private final BallTask ballTask;
    private boolean status;

    public Statistics(BallTask ballTask) {
        this.ballTask = ballTask;
        this.control = ballTask.getControlPanel();
        this.status = true;
        Thread statisticsThread = new Thread(this);
        statisticsThread.start();
    }

    @Override
    public void run() {
        while (status) {
            int count = 0;
            control.getData()[0][1] = ballTask.getHoleList().size() + "";
            control.getData()[1][1] = ballTask.getBlackHoleList().size() + "";
            for (int i = 0; i < ballTask.getBlackHoleList().size() ; i++) {
                if (ballTask.getBlackHoleList().get(i).getAmount() == 1) {
                    count++;
                }
            }
            control.getData()[2][1] = count + "";
            control.getData()[3][1] = ballTask.getHoleList().size() - count + "";

            control.repaint();
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
