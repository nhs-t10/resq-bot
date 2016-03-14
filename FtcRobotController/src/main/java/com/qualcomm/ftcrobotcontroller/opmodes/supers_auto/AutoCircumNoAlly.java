package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Aman on 3/1/2016.
 *
 * 40 points
 Circumstance: Alliance can’t or won’t do anything
 Rush to beacon, score climbers
 Back up and climb nearby ramp to midzone
 */
public class AutoCircumNoAlly extends AutonomousLibrary {

    public enum Team {
        RED, BLUE, UNKNOWN
    }
    protected Team teamWeAreOn; //enum that represent team

    public int currentRedAlpha;

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

        //Davis's code should update team here

        //////////////////////////////////////

        // Move forward a bit
        //encoderDriveStraight(0.5f, 3360);
        driveForward(1f, 3000);
        // Turn to face the beacon
        //turningTeamProcessor(225, 135);
        while(!driveTurnDegrees(225)) {
            waitForNextHardwareCycle();
        }
        // Move forward until color sensor detects white line
        //ChangeEncoderMode("Without");
        while(currentRedAlpha < 150){
            currentRedAlpha = getRed();
            telemetry.addData("Red Value", getRed()+"");
            driveStraight(225);
            waitForNextHardwareCycle();
        }
        stopDrive();
        // Turn towards beacon exactly
        while(!driveTurnDegrees(270)) {
            waitForNextHardwareCycle();
        }
        // Move closer to beacon. Be careful not to damage the field
        /*encoderDriveStraight(0.5f, 1120);
        // Score Climbers
        srvoScoreClimber.setPosition(1.0f);
        sleep(1000);
        srvoScoreClimber.setPosition(0.0f);
        // Back up from beacon
        encoderDriveStraight(-0.5f, 2240);
        // Turn to be perpendicular to ramp
        turningTeamProcessor(360, 0);
        // Move forward until you're a bit past the ramp, clear all blocks
        encoderDriveStraight(0.75f, 3360);
        // Move back until robot fits in climbing area
        encoderDriveStraight(0.5f, 1120);
        // Turn 90 degrees to face ramp
        turningTeamProcessor(270, 90);
        // Climb for a set amount of time
        encoderDriveStraight(1f, 5600);

        /* This is just so we can easily add "/*" comments wherever we need in the code to test
        */
    }

    public void turningTeamProcessor (int redTeamAngle, int blueTeamAngle) throws InterruptedException{
        //Code allows us to choose between team colors super easily instead of a giant
        //if/else thing every single time we need to turn
        if(teamWeAreOn == Team.RED) {
            while(!driveTurnDegrees(redTeamAngle)) {
                waitForNextHardwareCycle();
            }
        } else if(teamWeAreOn == Team.BLUE) {
            while(!driveTurnDegrees(blueTeamAngle)) {
                waitForNextHardwareCycle();
            }
        }
    }

}