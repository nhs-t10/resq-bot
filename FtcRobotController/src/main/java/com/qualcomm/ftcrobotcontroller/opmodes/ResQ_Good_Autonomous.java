package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Admin on 12/22/2015.
 */
public abstract class ResQ_Good_Autonomous extends ResQ_Library {
    protected final double DISTANCE_FROM_WALL = 30.0;

    protected double currentTimeCatch;

    protected boolean foundLine;
    protected boolean robotFirstTurn; //when we get to the line, turn in the general direction of the beacon
    protected boolean turning;
    protected boolean parked;

    final int RED_ANGLE = 220;
    final int BLUE_ANGLE = 140;
    final int PRECISION = 2;

    protected Team teamWeAreOn; //enum thats represent team

    public ResQ_Good_Autonomous() {
        foundLine = false;
        robotFirstTurn = false;
        turning = false;
        parked = false;
        teamWeAreOn = Team.UNKNOWN;
        driveGear = 3;
    }


    @Override
    public void init() {
        initializeMapping();
        //loadSensor(teamWeAreOn);
        //calibrateColors();
        startIMU();
        telemetry.addData("Init Yaw", getYaw());

        srvoLeftDeflector.setPosition(0.0);
        srvoRightDeflector.setPosition(0.0);
        sleep(1000);
    }

    @Override
    public void stop() {
        telemetry.addData("Init Yaw", getYaw());
    }

    @Override
    public void loop() {
        if (!robotFirstTurn){
            turnToBeacon(); //is
        } else if (!parked){
            approachBeacon();
        } else {
            DropClimber();
        }
    }

    protected void approach(int direction, float speed){
        driveStraight(1.0f);
    }

    protected void approachBeacon(){
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > DISTANCE_FROM_WALL){
            approach(1, 0.2f);
        }
        else {
            stopDrive();
            telemetry.addData("Status", "parked");
            parked = true;
        }
    }

    public void turnToBeacon() { //(turn to beacon)
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE - PRECISION && yaw <= RED_ANGLE + PRECISION) || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE - PRECISION && yaw <= BLUE_ANGLE + PRECISION)) { //make this compass later
            robotFirstTurn = true;
            drive(0.2f,0.2f);
        } else {
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
        //robotFirstTurn = driveTurnDegrees(230); //thx will p
    }
}
