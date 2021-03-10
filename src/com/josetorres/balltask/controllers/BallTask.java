package com.josetorres.balltask.controllers;

import com.josetorres.balltask.connection.Channel;
import com.josetorres.balltask.connection.ClientConnection;
import com.josetorres.balltask.connection.ServerConnection;
import com.josetorres.balltask.models.BlackHole;
import com.josetorres.balltask.models.Hole;
import com.josetorres.balltask.views.Control;
import com.josetorres.balltask.views.View;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BallTask extends JFrame {
    private final static int NUM_HOLES = 5;
    private final static int NUM_BLACK_HOLES = 1;
    private final LinkedList<Hole> holeList = new LinkedList<>();
    private final LinkedList<BlackHole> blackHoleList = new LinkedList<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final View view = new View(holeList, blackHoleList);
    private final Control controlPanel = new Control();
    private final Channel channel = new Channel(this);
    ClientConnection clientConnection;
    ServerConnection serverConnection;
    private Statistics statistics;

    public BallTask(String IP) {

        setWindowLayout();
        setViewLayout();
        setControlLayout();

        setVisible(true);

        createBlackHoles();
        createHoles();

        serverConnection = new ServerConnection(channel, this);
        clientConnection = new ClientConnection(channel, IP);

        statistics = new Statistics(this);

        controlPanel.getPlayBtn().addActionListener(e -> {
            for (int i = 0; i < holeList.size(); i++) {
                analyzeStatus(holeList.get(i));
            }
        });

        controlPanel.getPauseBtn().addActionListener(e -> {
            for (int i = 0; i < holeList.size(); i++) {
                holeList.get(i).setStatus("PAUSE");
            }
        });

        controlPanel.getStopBtn().addActionListener(e -> {
            restart();
            new Statistics(this);
        });


        view.paint();
    }

    public void restart() {
        for (int i = 0; i < holeList.size(); i++) {
            holeList.get(i).setStatus("STOP");
        }
        holeList.clear();
        blackHoleList.clear();
        statistics.setStatus(false);
        createHoles();
        createBlackHoles();
    }

    public void analyzeStatus(Hole hole) {
        boolean intersect = false;

        for (BlackHole blackHole : blackHoleList) {
            if (hole.getRectangle().intersects(blackHole.getRectangle())) {
                intersect = true;
            }
        }

        if (intersect) {
            hole.setStatus("SLOW");
        } else if (hole.getRectangle().intersects(0, 0, 1, view.getHeight()) && channel.isStatus() && channel.getDirection().equals("LEFT")) {
            hole.setStatus("SEND");
        } else if (hole.getRectangle().intersects(view.getWidth(), 0, 1, view.getHeight()) && channel.isStatus() && channel.getDirection().equals("RIGHT")) {
            hole.setStatus("SEND");
        } else {
            hole.setStatus("NORMAL");
        }
    }

    public void createHoleLeft(String x, String y, String angleX, String angleY, String Color, String rectangleSize) {
        Hole hole = new Hole(this.blackHoleList, this, view.getWidth() - (Integer.parseInt(rectangleSize) * 2) + 1, Integer.parseInt(y));
        hole.setAngleX(Integer.parseInt(angleX));
        hole.setAngleY(Integer.parseInt(angleY));
        hole.setRectangleSize(Integer.parseInt(rectangleSize));
        holeList.add(hole);
        hole.getHoleThread().start();
    }

    public void createHoleRight(String x, String y, String angleX, String angleY, String Color, String rectangleSize) {
        Hole hole = new Hole(this.blackHoleList, this, Integer.parseInt(rectangleSize) + 1, Integer.parseInt(y));
        hole.setAngleX(Integer.parseInt(angleX));
        hole.setAngleY(Integer.parseInt(angleY));
        hole.setRectangleSize(Integer.parseInt(rectangleSize));
        holeList.add(hole);
        hole.getHoleThread().start();
    }

    public void createHoles() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        for (int i = 0; i < NUM_HOLES; i++) {
            int x = (int) (Math.random() * screenSize.width);
            int y = (int) (Math.random() * screenSize.height);
            Hole hole = new Hole(blackHoleList, this, x, y);
            holeList.add(hole);
            hole.getHoleThread().start();
        }
    }

    public void createBlackHoles() {
        for (int i = 0; i < NUM_BLACK_HOLES; i++) {
            int x = (int) (Math.random() * view.getWidth());
            int y = (int) (Math.random() * view.getHeight());
            BlackHole blackHole = new BlackHole(x, y);
            blackHoleList.add(blackHole);
        }
    }

    public void setViewLayout() {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(view, gbc);
        System.out.println();
    }

    public void setControlLayout() {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 1;
        add(controlPanel, gbc);
    }

    public void removeHole(Hole hole) {
        holeList.remove(hole);
    }

    public void setWindowLayout() {
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(new GridBagLayout());
        setBackground(Color.BLUE);
        setResizable(true);
    }

    public Control getControlPanel() {
        return controlPanel;
    }

    public View getView() {
        return view;
    }

    public Channel getChannel() {
        return channel;
    }

    public List<Hole> getHoleList() {
        return holeList;
    }

    public List<BlackHole> getBlackHoleList() {
        return blackHoleList;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
}
