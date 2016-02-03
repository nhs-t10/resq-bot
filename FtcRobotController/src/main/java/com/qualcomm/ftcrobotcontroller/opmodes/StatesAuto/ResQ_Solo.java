package com.qualcomm.ftcrobotcontroller.opmodes.StatesAuto;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

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
        STARTING, FIRSTTURN, APPROACHBEACON, GETPARKEDCORRECTLY, DOA360FAM, FINALAPPROACH, PARKEDFORDROP, SECONDTURN, FINALPARK, DONE
    }

    @Override
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() { //Autonomous Logic
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);
        switch(currentState) {
            //
            case STARTING:
                starting();
                currentState = CurrentState.FIRSTTURN;
                break;
            case FIRSTTURN:
                currentState = firstTurn()? CurrentState.APPROACHBEACON : CurrentState.FIRSTTURN;
                break;
            case APPROACHBEACON:
                approachBeacon();
                currentState = CurrentState.DONE;
                break;
            default:
                stopDrive();
        }
    }

    protected void starting () {
        srvoLeftDeflector.setPosition(0.2);
        srvoRightDeflector.setPosition(0.8);
    }

    protected boolean firstTurn() {
        return driveTurnDegrees(RED_ANGLE_FIRST, 5);
    }

    protected void approachBeacon(){ // approaches the beacon until the ultrasonic detects the wall
        drive(1.0f, 1.0f);
        sleep(5000);
        stopDrive();
    }

}
