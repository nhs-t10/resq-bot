package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Admin on 12/22/2015.
 */
public abstract class ResQ_Good_Autonomous extends ResQ_Library {
    protected final double DISTANCE_FROM_WALL = 10.0;

    protected double currentTimeCatch;

    protected boolean foundLine;
    protected boolean robotFirstTurn; //when we get to the line, turn in the general direction of the beacon
    protected boolean turning;

    protected Team teamWeAreOn; //enum thats represent team

    public ResQ_Good_Autonomous() {
        foundLine = false;
        robotFirstTurn = false;
        turning = false;
        teamWeAreOn = Team.UNKNOWN;
        driveGear = 3;
    }


    @Override
    public void init() {
        initializeMapping();
        loadSensor(teamWeAreOn);
        //calibrateColors();
        startIMU();
    }

    @Override
    public void loop() {
        if (!robotFirstTurn){
            turnToBeacon(); //is
        } else {
            approachBeacon();
        }
    }

    protected void approach(int direction, float speed){
        drive(direction * speed, direction * speed);
    }

    protected void approachBeacon(){
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > 12){
            approach(1, 0.2f);
        }
        else {
            stopDrive();
        }
    }

    public void turnToBeacon() { //(turn to beacon)
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        if ((teamWeAreOn == Team.RED && yaw >= 220 && yaw <= 230) || (teamWeAreOn == Team.BLUE && yaw >= 300 && yaw <= 305)) { //make this compass later
            robotFirstTurn = true;
            drive(0.2f,0.2f);
        } else {
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
        //driveTurnDegrees(60); //thx will p
    }
}