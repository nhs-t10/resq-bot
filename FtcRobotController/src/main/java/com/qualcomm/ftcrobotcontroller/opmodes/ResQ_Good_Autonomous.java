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
    protected boolean readyToMove = false;

    final int RED_ANGLE = 220;
    final int BLUE_ANGLE = 140;
    final int PRECISION = 2;

    protected Team teamWeAreOn; //enum thats represent team

    public enum CurrentState{
        STARTING, GETINTOTURNPOSITION, FIRSTTURN, APPROACHBEACON, PARKEDFORDROP, SECONDTURN, FINALPARK, UNKNOWN
    }
    protected CurrentState currentState = CurrentState.STARTING;

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
    }

    @Override
    public void stop() {
        telemetry.addData("Init Yaw", getYaw());
    }

    @Override
    public void loop() { //Autonomous Logic
        if(currentState == CurrentState.STARTING){ //starting state
            telemetry.addData("Current State: ", "Beginning Match...");
            starting();
        }
        else if (currentState == CurrentState.GETINTOTURNPOSITION){
            telemetry.addData("Current State: ", "Moving forward...");
            getIntoTurnPosition(); //moves forward a little
        }
        else if (currentState == CurrentState.FIRSTTURN){
            telemetry.addData("Current State: ", "Turning towards beacon...");
            turnToBeacon(); //goes forward and turns towards beacon
        }
        else if (currentState == CurrentState.APPROACHBEACON){
            telemetry.addData("Current State: ", "Approaching beacon...");
            approachBeacon(); //rushes towards the beacon
        }
        else if (currentState == CurrentState.PARKEDFORDROP){
            telemetry.addData("Current State: ", "Dropping Climbers...");
            climberDrop();
        }
        else if (currentState == CurrentState.SECONDTURN){
            telemetry.addData("Current State: ", "Turning again...");
            secondTurn();
        }
        else if (currentState == CurrentState.FINALPARK){
            telemetry.addData("Current State: ", "Resting for teleop");
            finalParkingStage();
        }
    }


    ////////////////////////////////////////Autonomous Functions in Order////////////////////////////////////////

    public void starting () {
        srvoLeftDeflector.setPosition(0.2);
        srvoRightDeflector.setPosition(0.8);
        sleep(2000);
        currentState = CurrentState.FIRSTTURN;
    }

    public void getIntoTurnPosition() {
        drive(0.5f, 0.5f);
        sleep(2000);
        stopDrive();
        currentState = CurrentState.GETINTOTURNPOSITION;
    }

    public void turnToBeacon() { //turn to beacon
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        //we are aligned, so change state and drive forward.
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE - PRECISION && yaw <= RED_ANGLE + PRECISION)
                || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE - PRECISION && yaw <= BLUE_ANGLE + PRECISION)) { //make this compass later
            //robotFirstTurn = true; //deprecated logic
            currentState = CurrentState.APPROACHBEACON;
            drive(0.2f,0.2f);
        } else { //we are not aligned, so turn in direction we are supposed to
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
        //driveTurnDegrees(230); //Replacement function when it works
    }

    protected void approachBeacon(){
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > DISTANCE_FROM_WALL){
            approach(1, 0.2f);
        }
        else {
            stopDrive();
            currentState = CurrentState.PARKEDFORDROP;
        }
    }
    protected void approach(int direction, float speed){
        driveStraight(1.0f);
    }

    public void climberDrop () {
        DropClimber();
        currentState = CurrentState.SECONDTURN;
    }

    public void secondTurn () {
        currentState = CurrentState.FINALPARK;
    }

    public void finalParkingStage () {

    }
}
