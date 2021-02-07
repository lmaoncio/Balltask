package com.josetorres.balltask.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Control extends JPanel {
    private JButton playBtn;
    private JButton pauseBtn;
    private JButton stopBtn;
    String[][] data;

    public Control() {
        drawPanel();
    }

    public void drawPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500, screenSize.height);
        Color backgroundColor = new Color(255, 255, 255);
        setBackground(backgroundColor);

        setLayout(new GridBagLayout());

        setBorder(new EmptyBorder(15, 15, 15, 15));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.1;
        gbc.weightx = 0.1;

        data = new String[4][4];
        data[0][0] = "HOLES";
        data[1][0] = "BLACK HOLES";
        data[2][0] = "INSIDE";
        data[3][0] = "OUTSIDE";
        String[] column = {"Description", "Valor"};
        JTable dataTable = new JTable(data, column);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(dataTable, gbc);

        playBtn = new JButton("PLAY");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(playBtn, gbc);

        pauseBtn = new JButton("PAUSE");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(pauseBtn, gbc);

        stopBtn = new JButton("STOP");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(stopBtn, gbc);
    }

    public JButton getPlayBtn() {
        return playBtn;
    }

    public void setPlayBtn(JButton playBtn) {
        this.playBtn = playBtn;
    }

    public JButton getPauseBtn() {
        return pauseBtn;
    }

    public void setPauseBtn(JButton pauseBtn) {
        this.pauseBtn = pauseBtn;
    }

    public JButton getStopBtn() {
        return stopBtn;
    }

    public void setStopBtn(JButton stopBtn) {
        this.stopBtn = stopBtn;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
}

