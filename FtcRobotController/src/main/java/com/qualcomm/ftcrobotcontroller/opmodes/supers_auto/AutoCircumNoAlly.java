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

    @Override
    public void runOpMode() throws InterruptedException {
        initializeMapping();
        calibrateColors();
        startIMU();
        ChangeEncoderMode("Reset");

        waitForStart();

        //Davis's code should update team here

        //////////////////////////////////////

        // Move forward a bit
        encoderDriveStraight(0.5f, 3360);
        // Turn to face the beacon
        turningTeamProcessor(225, 135);
        // Move forward until color sensor detects white line
        ChangeEncoderMode("Without");
        while((getHue() != Color.WHITE)){
            drive(0.5f, 0.5f);
        }
        // Turn towards beacon exactly
        turningTeamProcessor(270, 90);
        // Move closer to beacon. Be careful not to damage the field
        encoderDriveStraight(0.5f, 1120);
        // Score Climbers
        srvoScoreClimber.setPosition(1.0f);
        sleep(1000);
        //srvoScoreClimber.setPosition(0.0f);
        // Back up from beacon
        encoderDriveStraight(-0.5f, 2240);
        // Turn to be perpendicular to ramp

        // Move forward until you're a bit past the ramp, clear all blocks

        // Move back until robot fits in climbing area

        // Turn 90 degrees to face ramp

        // Climb for a set amount of time


        /* This is just so we can easily add "/*" comments wherever we need in the code to test
        */
    }

    public void turningTeamProcessor (int redTeamAngle, int blueTeamAngle){
        //Code allows us to choose between team colors super easily instead of a giant
        //if/else thing every single time we need to turn
        if(teamWeAreOn == Team.RED) {
            driveTurnDegrees(redTeamAngle);
        } else if(teamWeAreOn == Team.BLUE) {
            driveTurnDegrees(blueTeamAngle);
        }
    }
}