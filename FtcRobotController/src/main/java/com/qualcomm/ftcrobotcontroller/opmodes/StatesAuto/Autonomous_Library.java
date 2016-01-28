package com.qualcomm.ftcrobotcontroller.opmodes.StatesAuto;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Aman on 1/28/2016.
 */
public abstract class Autonomous_Library extends ResQ_Library {

    public enum Team {
        RED, BLUE, UNKNOWN
    }
    protected Team teamWeAreOn; //enum that represent team

    int InitWaitingTime = 0;

    public void InitAutoMapping() {
        initializeMapping();
    }
}
