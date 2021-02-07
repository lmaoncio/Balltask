package com.josetorres.balltask.data;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private List<String> dataToTransfer = new ArrayList<>();
    private String data = "";
    private final String header = "HOLE";

    public Package(String direction, String x, String y, String angleX, String angleY, String color, String rectangleSize) {
        dataToTransfer.add(header);
        dataToTransfer.add(direction);
        dataToTransfer.add(x);
        dataToTransfer.add(y);
        dataToTransfer.add(angleX);
        dataToTransfer.add(angleY);
        dataToTransfer.add(color);
        dataToTransfer.add(rectangleSize);

        for (int i = 0; i < dataToTransfer.size(); i++) {
            data +=  dataToTransfer.get(i) + ",";
        }
    }

    public String getDataToTransfer() {
        return data;
    }

}
