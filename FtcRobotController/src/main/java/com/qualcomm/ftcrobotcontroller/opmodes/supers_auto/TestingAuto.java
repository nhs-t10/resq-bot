package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Aman on 3/1/2016.
 *
 * The purpose of this class is to test things before they're implemented into actual code.
 * It will also function as an interim library
 */
public class TestingAuto extends ResQ_Library {

    public enum Team {
        RED, BLUE, UNKNOWN
    }
    protected Team teamWeAreOn; //enum that represent team

    public enum CurrentState{
        Starting, FirstTurn, DriveGeneralZone, //these will get our robot to the general zone
        TurnSide, Scan, Turn90, Align, //these will collectively get our robot completely aligned.
        ClimberDrop //ending functions
    }
    protected CurrentState currentState = CurrentState.Starting;

    @Override
    public void init() {
        initializeMapping();
        startIMU();
    }

    @Override
    public void stop() {
        telemetry.addData("Init Yaw", getYaw());
    }

    @Override
    public void loop() { //Autonomous Logic
        double yaw = getYaw();
        telemetry.addData("Yaw", getYaw());

        if(currentState == CurrentState.Starting){ //starting state
            telemetry.addData("Current State: ", "Beginning Match...");
            starting();
        } else if (currentState == CurrentState.Starting){
            telemetry.addData("Current State: ", "Should be turning...");
        }
    }


    ////////////////////////////////////////Autonomous Functions in Order////////////////////////////////////////

    double angle;
    boolean angleSet = false;

    public void starting () {
        if(!angleSet) {
            angle = getYaw();
            angleSet = true;
        }
        driveStraight(angle);
        sleep(2000);
        currentState = CurrentState.FirstTurn;
    }

    double startTime;

    public void init() {
        initializeMapping();
        startIMU();
        startTime = getRuntime();
    }

    public void loop() {
        telemetry.addData("Yaw", getYaw());
        telemetry.addData("Motor Left", motorLeftTread.getPower());
        telemetry.addData("Motor Right", motorLeftTread.getPower());

        if(getRuntime() - startTime < 2.0) {
            driveStraight(180);
        } else {
            stopDrive();
        }
        //sleep(2000);
    }

}
