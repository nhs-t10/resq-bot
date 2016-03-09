package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.supers_auto.AutonomousLibrary;

/**
 * Created by Aman on 3/8/2016.
 */
public class EncoderDriveTest extends AutonomousLibrary {
    public void runOpMode() throws InterruptedException {

        initializeMapping();
        startIMU();

        waitForStart();
        telemetry.addData("status", "going to move");
        encoderDriveStraight(1, 5600);
        telemetry.addData("status", "moved");
}
}