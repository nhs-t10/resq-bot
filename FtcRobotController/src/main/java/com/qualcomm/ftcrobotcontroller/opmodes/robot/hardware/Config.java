package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Admin on 1/29/2016.
 */
public class Config {
    private static HardwareMap h;
    private static Telemetry t = new Telemetry();

    /**
     * Stores the hardware map for hardware use.
     * @param h the robot's hardware map
     */
    public static void setHardwareMap(HardwareMap h) {
        Config.h = h;
    }

    /**
     * Get the hardware map stored.
     * @return returns the stored hardware map
     */
    public static HardwareMap getHardwareMap() throws ConfigException {
        if(h == null) {
            throw new ConfigException();
        }
        return h;
    }

    /**
     * Get a basic telemetry object
     * @return telemetry object
     */
    public static Telemetry getTelemetry() {
        return t;
    }
}
