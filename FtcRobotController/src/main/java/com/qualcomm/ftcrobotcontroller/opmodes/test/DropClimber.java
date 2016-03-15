package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.supers_auto.AutonomousLibrary;

/**
 * Created by Aman on 3/8/2016.
 */
public class DropClimber extends AutonomousLibrary {
    public void runOpMode() throws InterruptedException {
        initializeMapping();
        startIMU();
        waitForStart();
        while(true) {
            loops();
        }
    }

    public void loops() throws InterruptedException {
        if(getDistance() > 20) {
            drive(1.0f, 1.0f);
        } else {
            stopDrive();
        }
    }
}
