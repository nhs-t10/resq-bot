package com.qualcomm.ftcrobotcontroller.opmodes.robot.test;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors.Imu;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 1/26/2016.
 */
public class ImuTest extends OpMode {
    Imu i;

    public void init() {
        i = new Imu("name");
    }

    public void loop() {
        telemetry.addData("Imu", i.getYaw());
    }
}
