package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 1/15/2016.
 */
public class Color {
    private ColorSensor color;
    private String name;

    private int offsetRed;
    private int offsetGreen;
    private int offsetBlue;
    private int threshold = 50;

    public Color(String name, HardwareMap h) {
        color = h.colorSensor.get(name);
        this.name = name;
        sleep(3000);
        calibrate();
    }
    public int off() {
        return offsetRed;
    }
    public int red() {
        //return Math.abs(color.red() - offsetRed);
        return color.red();
    }
    public int green() {
        return Math.abs(color.green() - offsetGreen);
    }
    public int blue() {
        return Math.abs(color.blue() - offsetBlue);
    }

    private void calibrate() {
        offsetRed = color.red();
        offsetGreen = color.green();
        offsetBlue = color.blue();
    }

    public String getName() {
        return name;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
        }
    }

}
