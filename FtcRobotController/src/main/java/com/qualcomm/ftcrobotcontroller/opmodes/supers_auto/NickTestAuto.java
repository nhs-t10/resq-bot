package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;



/**
 * Created by nick on 3/9/16.
 */



public class NickTestAuto extends AutonomousLibrary {

    private ResQ_Library resQLibrary;

    public NickTestAuto(ResQ_Library resQLibrary) {
        this.resQLibrary = resQLibrary;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initializing Hardware", "");
        initializeMapping();
        telemetry.addData("Calibrating Colors", "");
        calibrateColors();
        telemetry.addData("Starting the IMU", "");
        startIMU();
        telemetry.addData("Resetting Encoders", "");
        ChangeEncoderMode("Reset");
        telemetry.addData("Ready to Start", "");
        waitForStart();

        /*
        logic is: turn to
         */

        while(!driveTurnDegrees(270)) {
            waitForNextHardwareCycle();
        }
        telemetry.addData("First turn complete", "");

        driveForward(.2f, 4000);
        while(!driveTurnDegrees(360)){
            waitOneFullHardwareCycle();
        }
        telemetry.addData("Second turn complete", "");

        while(getHue() != Color.WHITE)
        {
            drive(.2f, .2f);
            sleep(2000);
            waitOneFullHardwareCycle();
            drive(-.2f, -.2f);
            sleep(2000);
            waitOneFullHardwareCycle();
        }
        telemetry.addData("Line sensed", "");

        while(!driveTurnDegrees(270)){
            waitOneFullHardwareCycle();
        }
        telemetry.addData("Final turn complete", "");
    }




}
