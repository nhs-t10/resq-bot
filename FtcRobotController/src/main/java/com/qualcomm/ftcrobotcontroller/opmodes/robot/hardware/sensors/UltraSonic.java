package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by Admin on 1/15/2016.
 */
public class UltraSonic implements Sensor {
    private AnalogInput ult;
    private String name;

    public UltraSonic(String name) {
        initialize(name);
    }

    /**
     * Initialize Sensor
     * @param name name of the sensor
     */
    public void initialize(String name) {
        try {
            ult = Config.getHardwareMap().analogInput.get(name);
        } catch(ConfigException ce) {
            ce.printStackTrace();
            return;
        }
        this.name = name;
    }

    /**
     * Get name of sensor
     * @return name of sensor
     */
    public String getName() {
        return name;
    }

    /**
     * get the value of the UltraSonic sensor
     * @return UltraSonic value
     */
    public int getDistance() {
        return ult.getValue();
    }
}
