package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors;

/**
 * Created by Admin on 1/29/2016.
 */
public interface Sensor {
    /**
     * Initialize Sensor
     * @param name name of the sensor
     */
    public void initialize(String name);

    /**
     * Get name of sensor
     * @return name of sensor
     */
    public String getName();
}