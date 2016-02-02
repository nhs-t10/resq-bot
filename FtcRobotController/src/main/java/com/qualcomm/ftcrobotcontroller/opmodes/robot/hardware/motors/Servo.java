package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.motors;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;

/**
 * Created by Admin on 1/31/2016.
 */
public class Servo extends com.qualcomm.robotcore.hardware.Servo {
    public Servo(String name) {
        super(Config.getHardwareMapUnsafe().servo.get(name).getController(), Config.getHardwareMapUnsafe().servo.get(name).getPortNumber());
    }
}
