package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Admin on 12/22/2015.
 */
public abstract class ResQ_Good_Autonomous extends ResQ_Library {
    protected final double DISTANCE_FROM_WALL = 30.0;

    protected double currentTimeCatch;

    protected boolean foundLine;

    final int RED_ANGLE = 220;
    final int BLUE_ANGLE = 140;
    final int PRECISION = 2;

    String phase = "unset";
    int phasen = -1;

    protected Team teamWeAreOn; //enum thats represent team

    public ResQ_Good_Autonomous() {
        foundLine = false;
        teamWeAreOn = Team.UNKNOWN;
        driveGear = 3;
    }

    @Override
    public void init() {
        initializeMapping();
        //loadSensor(teamWeAreOn);
        //calibrateColors();
        updatePhase("Starting IMU", 0);
        startIMU();
        telemetry.addData("Init Yaw", getYaw());

        updatePhase("Lowering Deflectors", 1);
        srvoLeftDeflector.setPosition(0.0);
        srvoRightDeflector.setPosition(0.0);
        sleep(1000);
        updatePhase("Turning", 2);
    }

    @Override
    public void stop() {
        telemetry.addData("Init Yaw", getYaw());
    }

    @Override
    public void loop() {
        switch (phasen) {
            case 2: //turning
                turnToBeacon();
                break;
            case 3: //go to beacon
                approachBeacon();
                break;
            case 4: //drop climbers
                DropClimber();
                break;
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
            updatePhase("Reached Wall", 4);
        }
    }

    public void turnToBeacon() { //(turn to beacon)
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE - PRECISION && yaw <= RED_ANGLE + PRECISION) || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE - PRECISION && yaw <= BLUE_ANGLE + PRECISION)) { //make this compass later
            updatePhase("Turned toward Beacon", 3);
            drive(0.2f,0.2f);
        } else {
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
        //driveTurnDegrees(230); //thx will p
    }
    public void updatePhase(String p, int n) {
        phase = p;
        phasen = n;
        telemetry.addData("Phase", phase);
        telemetry.addData("#", phasen);
    }
}
