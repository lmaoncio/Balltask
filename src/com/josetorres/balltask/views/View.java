package com.josetorres.balltask.views;

import com.josetorres.balltask.models.BlackHole;
import com.josetorres.balltask.models.Hole;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.LinkedList;

public class View extends Canvas {
    private final LinkedList<Hole> holeList;
    private final LinkedList<BlackHole> blackHoleList;

    public View(LinkedList<Hole> holeList, LinkedList<BlackHole> blackHoleList) {
        this.holeList = holeList;
        this.blackHoleList = blackHoleList;

    }

    public void paint() {
        createBufferStrategy(2);
        BufferStrategy bs;

        do {
            bs = getBufferStrategy();
            Graphics g = bs.getDrawGraphics();
            g.clearRect(0, 0, getWidth(), getHeight());

            for (BlackHole blackHole : blackHoleList) {
                g.setColor(blackHole.getColor());
                g.fillRect(blackHole.getX(), blackHole.getY(), (int) blackHole.getRectangle().getWidth(), (int) blackHole.getRectangle().getHeight());
            }

            for (int i = 0; i < holeList.size(); i++) {
                g.setColor(holeList.get(i).getColor());
                g.fillRect(holeList.get(i).getX(), holeList.get(i).getY(), (int) holeList.get(i).getRectangle().getWidth(), (int) holeList.get(i).getRectangle().getHeight());
            }

            g.dispose();
            bs.show();
        } while (true);
    }
}
