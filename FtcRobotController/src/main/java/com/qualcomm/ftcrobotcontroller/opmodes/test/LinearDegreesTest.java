package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.supers_auto.AutonomousLibrary;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Admin on 3/8/2016.
 */
public class LinearDegreesTest extends AutonomousLibrary {
    public void runOpMode() throws InterruptedException {
        initializeMapping();
        startIMU();

        waitForStart();
        telemetry.addData("status", "turning");
        while(!driveTurnDegrees(270)) {
            waitForNextHardwareCycle();
        }
        telemetry.addData("status", "turned");
    }
}