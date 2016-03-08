package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.supers_auto.AutonomousLibrary;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Admin on 3/8/2016.
 */
public class LinearDegreesTest extends AutonomousLibrary {
    public void runOpMode() {
        initializeMapping();
        startIMU();
        telemetry.addData("status", "turning");
        while(!driveTurnDegrees(270)) {
            try {
                waitForNextHardwareCycle();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        telemetry.addData("status", "turned");
    }
}