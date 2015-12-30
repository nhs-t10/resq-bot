package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Autonomous program - for first meet use only
 * This autonomous program is for the first robotics competition on November 14/21 ONLY
 *
 * It assumes that other robots barely have an autonomvement if alliance robot is moving in front)
 * 		- We detect what color the line is to determine what team we're on, sepreate codes for each.
 * 		- turn X amount of degrees to face beacon in general direction
 * 		- some IMU sensor to keep straight path
 * 		- ultrasonic to detect wall/beacon and stop a short distance ahead
 * 		- sliding autonomous mechanisms deploy and start doing their thing
 * 		- we back up a bit and turn Y amount to face ramp, then choose which side of ramp to climb
 * 		- go to ramp and climb it as much as possible, stop motors once we've been turning for 5 seconds but no movement has been made (aka we're stuck)
 * 		- deploy hanging mech when gyroscope detects rotation change in order to stop us from falling off of ramp, touch sensor maybe to detect we've reached the end and we should stop.
 */
@SuppressWarnings("all")
public class ResQ_Autonomous extends ResQ_Library {

    float leftPower;
    float rightPower;
    final double DISTANCE_FROM_WALL = 10;
    double currentTimeCatch;

    boolean foundLine = false;
    boolean robotFirstTurn = false; //when we get to the line, turn in the general direction of the beacon
    int angleGoal;
    private boolean turning = false;

    /**
     * Blue Team Information:
     * 		- team is on the right side of the map, line is on robot's left and ramp on robot's right
     * 		- ramp to climb is far from starting, close to the beacon, turn right to access
     * 		- if going to ramp across the line, ramp is closer to starting position and not the beacon, turn left to access
     *
     * Red Team Information:
     * 		- team is on the left side of the map, line is on robot's right and ramp on robot's left
     * 		- ramp to climb is far from starting, close to the beacon, turn left to access
     * 		- if going to ramp across the line, ramp is closer to starting position and not the beacon, turn right to access
     */

    @Override
    public void init() {
        //Do the map thing
        initializeMapping();
        loadSensor(Team.RED);
        driveGear = 3;
        calibrateColors();
        startIMU();
    }
    public void loop() {
        if (!robotFirstTurn){
            //turnToBeacon();
            robotFirstTurn = true; //this
            driveTurnDegrees(230); //is
        } /*else { //for
            approachBeacon(); //patsios
        } */ //^
    }

    public void approachBeacon(){
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > 12){
            approach(1);
        }
        else {
            stopMoving();
            //srvoScoreClimbers.setPosition(0.0);
        }
    }

    public void turnToBeacon() { //(turn to beacon)
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        if ((teamWeAreOn == Color.RED && yaw >= 220 && yaw <= 230) || (teamWeAreOn == Color.BLUE && yaw >= 300 && yaw <= 305)) { //make this compass later
            robotFirstTurn = true;
            drive(0.2f,0.2f);
        } else { //markisagod
			/*if (!turning) turning = true;
			else {
				angleGoal = sensorGyro.rawX() + 70;
			}*/
			int m = teamWeAreOn == Color.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
            //driveTurnDegrees(60); //thx will p
        }
    }



    public void approach(int direction){
        leftPower = 0.2f * direction;
        rightPower = 0.2f * direction;
        drive(leftPower, rightPower);
    }
    public void stopMoving(){
        leftPower = 0.0f;
        rightPower = 0.0f;
        drive(leftPower, rightPower);
    }
}