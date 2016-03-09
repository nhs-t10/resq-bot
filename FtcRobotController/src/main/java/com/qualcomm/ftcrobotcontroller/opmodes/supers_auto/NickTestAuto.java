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

        driveForward(.2f, 4000);
        while(!driveTurnDegrees(360)){
            waitOneFullHardwareCycle();
        }
        int timeC = getRuntime();

        while((getHue() != Color.WHITE) || (getRuntime() > (timeC + 2)))
        {
            drive(.2f, .2f);
        }

        if(getHue() == Color.WHITE){
            while(!driveTurnDegrees(270)){
                waitOneFullHardwareCycle();
            }
        }
        else{
            driveForward(-.2f, 4000);
        }

        while (getHue() != Color.WHITE) {
            drive(1f, 1f);
            waitOneFullHardwareCycle();
        }

    }

    private static double PID() {

    }


}
