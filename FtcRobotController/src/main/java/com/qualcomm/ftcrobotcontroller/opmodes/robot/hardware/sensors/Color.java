package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 1/15/2016.
 */
public class Color implements Sensor {
    private ColorSensor color;
    private String name;

    private int offsetRed;
    private int offsetGreen;
    private int offsetBlue;
    private int threshold = 50;

    public Color(String name) {
        initialize(name);
    }

    /**
     * Initialize Sensor
     * @param name name of the sensor
     */
    public void initialize(String name) {
        try {
            color = Config.getHardwareMap().colorSensor.get(name);
        } catch(ConfigException ce) {
            ce.printStackTrace();
            return;
        }
        this.name = name;
        calibrate();
    }

    public int red() {
        return Math.abs(color.red() - offsetRed);
    }
    public int green() {
        return Math.abs(color.green() - offsetGreen);
    }
    public int blue() {
        return Math.abs(color.blue() - offsetBlue);
    }

    /**
     * Callibrate the Color Sensor. Can be called multiple times
     * to recallibrate.
     */
    private void calibrate() {
        offsetRed = color.red();
        offsetGreen = color.green();
        offsetBlue = color.blue();
    }

    /**
     * Get name of sensor
     * @return name of sensor
     */
    public String getName() {
        return name;
    }
}
