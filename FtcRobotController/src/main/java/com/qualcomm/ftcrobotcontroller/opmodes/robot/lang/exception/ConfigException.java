package com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception;

import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Admin on 1/29/2016.
 */
public class ConfigException extends Exception {
    public ConfigException() {
        Telemetry errMsg = new Telemetry();
        errMsg.addData("Error", "HardwareMap not set!");
    }
}
