package com.qualcomm.ftcrobotcontroller.opmodes.robot.lang;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.motors.Motor;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

/**
 * Created by Admin on 1/31/2016.
 */
public class Drive {
    private static HardwareMap h;

    public Drive() {
        try {
            h = Config.getHardwareMap();
        } catch(ConfigException ce) {
            return;
        }
    }

    public static void drive(double left, double right) {
        for(String m: motors.keySet()) {

        }
    }
}
