package com.qualcomm.ftcrobotcontroller.opmodes.StatesAuto;


/**
 * Created by Aman on 1/28/2016.
 *
 * Game Plan:
 *      move forward a bit,
 *      turn towards beacon,
 *      move straight until left/right ultrasonic (only 1 of them is activated) detects wall
 *      turn towards beacon a certain amount of degrees
 *      move forward, drop climbers
 *      move back a bit
 */
public class ResQ_Solo extends Autonomous_Library {
    protected static final int RED_ANGLE_FIRST = 220;

    protected CurrentState currentState = CurrentState.STARTING;

    public enum CurrentState{
        STARTING, FIRSTTURN, APPROACHBEACON, DROPCLIMBERS, FINALPARK, DONE
    }

    @Override
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() { //Autonomous Logic
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        telemetry.addData("distance", getDistance());
        switch(currentState) {
            //
            case STARTING:
                telemetry.addData("Debug", "Starting");
                starting();
                break;
            case FIRSTTURN:
                telemetry.addData("Debug", "First Turn");
                firstTurn();
                break;
            case APPROACHBEACON:
                telemetry.addData("Debug", "Approach");
                approachBeacon();
                break;
            case DROPCLIMBERS:
                telemetry.addData("Debug", "Drop");
                dropClimbers();
                break;
            case FINALPARK:
                telemetry.addData("Debug", "Final");
                finalPark();
                break;
            default:
                telemetry.addData("Debug", "Done! :)");
                stopDrive();
        }
    }

    protected void starting () {
        srvoScoreClimberRight.setPosition(1.0); //makes sure it doesn't drop it by accident
        srvoScoreClimberLeft.setPosition(1.0); //makes sure it doesn't drop it by accident
        currentState = CurrentState.FIRSTTURN;
    }

    protected void firstTurn() {
        boolean result = driveTurnDegrees(RED_ANGLE_FIRST, 7);
        if(result) {
            currentState = CurrentState.APPROACHBEACON;
        }
    }

    protected void approachBeacon(){ // approaches the beacon until the ultrasonic detects the wall
        drive(1.0f, 1.0f);
        if(getDistance() < 45) {
            stopDrive();
            currentState = CurrentState.DROPCLIMBERS;
        }
    }

    protected void dropClimbers() {
        //current angle for testing: 10
        boolean result = driveTurnDegrees(180, 7);
        if(result) {
            stopDrive();
            DropClimber();
            currentState = CurrentState.FINALPARK;
        }
    }

    protected void finalPark() {
        drive(-1.0f, -1.0f);
        sleep(1000);
        stopDrive();
        currentState = CurrentState.DONE;
    }
}
