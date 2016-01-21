package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Admin on 12/22/2015.
 */
public abstract class ResQ_Good_Autonomous extends ResQ_Library {
    protected final double DISTANCE_FROM_WALL = 37.5;

    // First set is to turn to correct beacon
    final int RED_ANGLE_1 = 220;
    final int BLUE_ANGLE_1 = 140;
    // the second set positions to the beacon exactly. We should turn an additional 30-50 degrees
    final int RED_ANGLE_2 = 270;
    final int BLUE_ANGLE_2 = 170;
    // the third set positions to the ramp exactly. We should turn an additional 90 degrees
    final int RED_ANGLE_3 = 220;
    final int BLUE_ANGLE_3 = 140;
    final int PRECISION = 2;

    boolean turnedToBeaconCorrectly = false; //this is in the parking zone, checking that we're facing the beacon

    protected Team teamWeAreOn; //enum thats represent team

    public enum CurrentState{
        STARTING, GETINTOTURNPOSITION, FIRSTTURN, APPROACHBEACON, PARKEDFORDROP, SECONDTURN, FINALPARK, UNKNOWN
    }
    protected CurrentState currentState = CurrentState.STARTING;

    public ResQ_Good_Autonomous() {
        teamWeAreOn = Team.UNKNOWN;
        driveGear = 3;
    }

    @Override
    public void init() {
        initializeMapping();
        startIMU();
        telemetry.addData("Init Yaw", getYaw());
        srvoScoreClimbers.setPosition(0.0); //makes sure it doesn't drop it by accident
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
            parkingDrop();
        }
        else if (currentState == CurrentState.SECONDTURN){
            telemetry.addData("Current State: ", "Turning again...");
            secondTurn();
        }
        else if (currentState == CurrentState.FINALPARK) {
            telemetry.addData("Current State: ", "We done fam.");
            finalParkingStage();
        }
    }


    ////////////////////////////////////////Autonomous Functions in Order////////////////////////////////////////

    public void starting () {
        srvoLeftDeflector.setPosition(0.2);
        srvoRightDeflector.setPosition(0.8);
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
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE_1 - PRECISION && yaw <= RED_ANGLE_1 + PRECISION)
                || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE_1 - PRECISION && yaw <= BLUE_ANGLE_1 + PRECISION)) { //make this compass later
            //robotFirstTurn = true; //deprecated logic
            currentState = CurrentState.APPROACHBEACON;
            drive(0.2f, 0.2f);
        } else { //we are not aligned, so turn in direction we are supposed to
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
        //driveTurnDegrees(230); //Replacement function when it works
    }

    protected void approachBeacon(){ // approaches the beacon until the ultrasonic detects the wall
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > DISTANCE_FROM_WALL){
            driveStraight(1.0);
        }
        else {
            stopDrive();
            currentState = CurrentState.PARKEDFORDROP;
        }
    }

    public void parkingDrop () { //positions the robot to face beacon then drops climbers
        //later, we'll use ultrasonics to triangulate the positions correctly.

        //It's not possible
        //No - it's necessary.

        if (!turnedToBeaconCorrectly){ //Cooper, this is no time for caution.
            double yaw = getYaw();
            telemetry.addData("yaw", yaw);
            //we are aligned, move to next part
            if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE_2 - PRECISION && yaw <= RED_ANGLE_2 + PRECISION)
                    || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE_2 - PRECISION && yaw <= BLUE_ANGLE_2 + PRECISION)) { //make this compass later
                turnedToBeaconCorrectly = true;
            } else { //we are not aligned, so turn in direction we are supposed to
                int m = teamWeAreOn == Team.RED ? -1 : 1;
                drive(.1f * m, -.1f * m);
            }
        } else { //Cooper, we are.. lined up! INITIATE SPIN
            //bring up tread guards, they're useless now
            srvoLeftDeflector.setPosition(1.0);
            srvoRightDeflector.setPosition(0.0);
            sleep(1000); //waits for guards to raise
            drive(.2f, .2f);
            sleep(500); //moves forward at 1/5 speed for half a second
            stopDrive();
            DropClimber();
            currentState = CurrentState.SECONDTURN;
        }
    }

    public void secondTurn() { //turns 90 degrees to face ramp
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        //we are aligned, move to next part
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE_3 - PRECISION && yaw <= RED_ANGLE_3 + PRECISION)
                || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE_3 - PRECISION && yaw <= BLUE_ANGLE_3 + PRECISION)) { //make this compass later
            currentState = CurrentState.FINALPARK;
        } else { //we are not aligned, so turn in direction we are supposed to
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.1f * m, -.1f * m);
        }
    }

    public void finalParkingStage() { //moves forward and rests.
        drive(.2f, .2f);
        sleep(1000); //moves forward at 1/5 speed for a second
        stopDrive();
        currentState = CurrentState.FINALPARK;
    }
}
