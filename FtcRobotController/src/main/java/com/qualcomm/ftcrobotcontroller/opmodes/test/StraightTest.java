package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Admin on 2/29/2016.
 */
public class StraightTest extends ResQ_Library {
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() {
        telemetry.addData("Yaw", getYaw());
        telemetry.addData("Motor Left", motorLeftTread.getPower());
        telemetry.addData("Motor Right", motorLeftTread.getPower());
        
        driveStraight(180);
    }
}
