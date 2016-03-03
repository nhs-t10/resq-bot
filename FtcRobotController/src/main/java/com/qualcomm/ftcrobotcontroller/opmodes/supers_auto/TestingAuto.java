package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Aman on 3/1/2016.
 *
 * The purpose of this class is to test things before they're implemented into actual code.
 * It will also function as an interim library
 */
public abstract class TestingAuto extends ResQ_Library {
    protected final double RED_WALL = 50;
    protected final double BLUE_WALL = 37.5;
    // First set is to turn to correct  beacon
    final int RED_ANGLE_1 = 225;
    final int BLUE_ANGLE_1 = 135;
    // the second set positions to the beacon exactly. We should turn an additional 30-50 degrees
    final int RED_ANGLE_2 = 270;
    final int BLUE_ANGLE_2 = 90 ;
    // the third set positions to the ramp exactly. We should turn an additional 90 degrees
    final int RED_ANGLE_3 = 360;
    final int BLUE_ANGLE_3 = 360;
    final int PRECISION = 2;

    boolean turnedToBeaconCorrectly = false; //this is in the parking zone, checking that we're facing the beacon
    boolean IMURecalibrating = false;
    boolean IMURecalibrated = false;

    public enum Team {
        RED, BLUE, UNKNOWN
    }
    protected Team teamWeAreOn; //enum that represent team

    //used for timer
    double runTime;
    double startTime;
    boolean firstCall = true;

    public int testVar = 0;

    public boolean AreWeNearRamp;

    public enum CurrentState{
        Starting, FirstTurn, DriveGeneralZone, //these will get our robot to the general zone
        TurnSide, Scan, Turn90, Align, //these will collectively get our robot completely aligned.
        ClimberDrop //ending functions
    }
    protected CurrentState currentState = CurrentState.Starting;

    public TestingAuto() {
        teamWeAreOn = Team.UNKNOWN;
        driveGear = 3;
    }

    @Override
    public void init() {
        initializeMapping();
        startIMU();
        telemetry.addData("Init Yaw", getYaw());
        /*if(wait5) waitFive();
        srvoScoreClimbers.setPosition(1.0); //makes sure it doesn't drop it by accident
        srvoBlockDropper.setPosition(0.6);*/
    }

    @Override
    public void stop() {
        telemetry.addData("Init Yaw", getYaw());
    }

    @Override
    public void loop() { //Autonomous Logic
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        if(!IMURecalibrating || !IMURecalibrated) {
            telemetry.addData("Calibration", "False");
        }

        if(currentState == CurrentState.Starting){ //starting state
            telemetry.addData("Current State: ", "Beginning Match...");
            starting();
        } else if (currentState == CurrentState.Starting){
            telemetry.addData("Current State: ", "Should be turning...");
        }
    }


    ////////////////////////////////////////Autonomous Functions in Order////////////////////////////////////////

    public void starting () {
        driveStraight(0);
        sleep(2000);
        currentState = CurrentState.FirstTurn;
    }


}
