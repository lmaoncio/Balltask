package com.josetorres.balltask.controllers;

import com.josetorres.balltask.views.Control;

public class Statistics extends Thread {
    private final Control control;
    private final BallTask ballTask;

    public Statistics(BallTask ballTask) {
        this.ballTask = ballTask;
        this.control = ballTask.getControlPanel();
        start();
    }

    @Override
    public void run() {
        while (true) {
            control.getData()[0][1] = ballTask.getHoleList().size() + "";
            control.getData()[1][1] = ballTask.getBlackHoleList().size() + "";
            control.repaint();
        }
    }
}
