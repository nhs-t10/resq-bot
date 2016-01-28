package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.test;

import com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.sensors.Imu;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Admin on 1/26/2016.
 */
public class ImuTest extends OpMode {
    Imu i;

    public void init() {
        i = new Imu("name", hardwareMap);
    }

    public void loop() {
        telemetry.addData("Imu", i.getYaw());
    }
}
