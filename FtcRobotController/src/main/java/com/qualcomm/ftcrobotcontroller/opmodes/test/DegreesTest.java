package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Admin on 1/22/2016.
 */
public class DegreesTest extends ResQ_Library {
    private  boolean turn;
    private final int GOAL = 220;

    @Override
    public void init() {
        turn = true;
        initializeMapping();
        startIMU();
    }

    public void loop() {
        telemetry.addData("Current Yaw", getYaw());
        telemetry.addData("Goal", GOAL);
        if(turn) {
            turn = !driveTurnDegrees(GOAL);
        }
    }
}
