package com.josetorres.balltask;

import com.josetorres.balltask.controllers.BallTask;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String remote = null;
        while (remote == null) {
            remote = JOptionPane.showInputDialog("IP: ");
        }

        new BallTask(remote);
    }
}