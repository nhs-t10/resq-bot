package com.qualcomm.ftcrobotcontroller.opmodes.StatesAuto;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Aman on 1/28/2016.
 */
public class ResQ_Crossing extends Autonomous_Library {

    public enum CurrentState{
        STARTING
    }
    protected CurrentState currentState = CurrentState.STARTING;

    @Override
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() { //Autonomous Logic
        double yaw = getYaw();
        telemetry.addData("yaw", yaw);



    }

}