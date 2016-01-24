package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Admin on 12/22/2015.
 */
public abstract class ResQ_Good_Autonomous extends ResQ_Library {
    protected final double RED_WALL = 37.5;
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
    boolean IMURecalibratedAgain = false;

    protected Team teamWeAreOn; //enum that represent team
    protected boolean wait5 = false;
    double yaw360;

    public int testVar = 0;

    public enum CurrentState{
        STARTING, GETINTOTURNPOSITION, FIRSTTURN, APPROACHBEACON, GETPARKEDCORRECTLY, DOA360FAM, FINALAPPROACH, PARKEDFORDROP, SECONDTURN, FINALPARK, UNKNOWN
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
        if(wait5) waitFive();
        srvoScoreClimbers.setPosition(1.0); //makes sure it doesn't drop it by accident
        srvoBlockDropper.setPosition(0.6);
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
        else if (currentState == CurrentState.GETPARKEDCORRECTLY){
            telemetry.addData("Current State: ", "Turning to beacon");
            getParkedCorrectly(); //turns to beacon
        }
        else if (currentState == CurrentState.DOA360FAM){
            telemetry.addData("Current State: ", "do a 360 fam");
            doA360Fam();
        }
        else if (currentState == CurrentState.FINALAPPROACH){
            telemetry.addData("Current State: ", "Moving forward");
            finalApproach();
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
        drive(-0.5f, -0.5f);
        sleep(2000);
        stopDrive();
        currentState = CurrentState.FIRSTTURN;
    }

    public void turnToBeacon() { //turn to beacon
        double yaw = getYaw();
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
        //currentState = driveTurnDegrees(RED_ANGLE_1)?  CurrentState.APPROACHBEACON : CurrentState.FIRSTTURN;
    }

    protected void approachBeacon(){ // approaches the beacon until the ultrasonic detects the wall
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        double distance = (teamWeAreOn == Team.RED ? RED_WALL : BLUE_WALL);
        if(ultraValue > distance) {
            float fltUltValue = (float) ultraValue;
            //float speed = ((ultraValue < 50.0) ? fltUltValue / 50.0f : 1.0f);
            float speed = 1.0f;
            //driveStraight(1.0);
            drive(speed, speed);
        }
        else {
            stopDrive();
            //bring up tread guards, they're useless now
            srvoLeftDeflector.setPosition(1.0);
            srvoRightDeflector.setPosition(0.0);
            currentState = CurrentState.GETPARKEDCORRECTLY;
        }
    }

    public void getParkedCorrectly () {
        double yaw = getYaw();
        //we are aligned, move to next part
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE_2 - PRECISION && yaw <= RED_ANGLE_2 + PRECISION)
                || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE_2 - PRECISION && yaw <= BLUE_ANGLE_2 + PRECISION)) { //make this compass later
            turnedToBeaconCorrectly = true;
            stopDrive();
            telemetry.addData("end angle", yaw);
            yaw360 = getYaw();
            currentState = CurrentState.DOA360FAM;
        } else { //we are not aligned, so turn in direction we are supposed to
            telemetry.addData("yawInBlock", yaw);
            int m = teamWeAreOn == Team.RED ? -1 : 1;
            drive(.3f * m, -.3f * m);
        }
    }

    public void doA360Fam () {
        double yaw = getYaw();
        if (yaw >= yaw360 - PRECISION*3 && yaw <= yaw360 - PRECISION){ //weirder calculation to ensure no overlaps from current yaw
            stopDrive();
            telemetry.addData("end angle", yaw);
            currentState = CurrentState.FINALAPPROACH;
        } else { //we are not aligned, so turn in direction we are supposed to
            telemetry.addData("yawInBlock", yaw);
            drive(.3f, -.3f);
        }
    }

    protected void finalApproach(){ // approaches the beacon until the ultrasonic detects the wall
        double ultraValue = getDistance();
        telemetry.addData("ultra", ultraValue);
        if(ultraValue > 12) {
            float fltUltValue = (float) ultraValue;
            //float speed = ((ultraValue < 50.0) ? fltUltValue / 50.0f : 1.0f);
            float speed = 0.5f;
            //driveStraight(1.0);
            drive(speed, speed);
        }
        else {
            stopDrive();
            currentState = CurrentState.PARKEDFORDROP;
        }
    }


    public void parkingDrop () { //positions the robot to face beacon then drops climbers
        //later, we'll use ultrasonics to triangulate the positions correctly.
        drive(.5f, .5f);
        sleep(500); //moves forward at 1/5 speed for half a second
        stopDrive();

        DropClimber();

        sleep(1000); //waits for climbers to dropbp
        currentState = CurrentState.UNKNOWN;
    }

    public void secondTurn() { //turns 90 degrees to face ramp
        double yaw = getYaw();
        //we are aligned, move to next part
        if ((teamWeAreOn == Team.RED && yaw >= RED_ANGLE_3 - PRECISION && yaw <= RED_ANGLE_3 + PRECISION)
                || (teamWeAreOn == Team.BLUE && yaw >= BLUE_ANGLE_3 - PRECISION && yaw <= BLUE_ANGLE_3 + PRECISION)) { //make this compass later
            currentState = CurrentState.FINALPARK;
        } else { //we are not aligned, so turn in direction we are supposed to
            int m = teamWeAreOn == Team.RED ? 1 : -1;
            drive(.3f * m, -.3f * m);
        }
    }

    public void finalParkingStage() { //moves forward and rests.
        drive(.3f, .3f);
        sleep(1000); //moves forward at 1/5 speed for a second
        stopDrive();
        currentState = CurrentState.FINALPARK;
    }

    public void waitFive() {
        sleep(5000);
    }
}
