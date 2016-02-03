package com.qualcomm.ftcrobotcontroller.opmodes.StatesAuto;

/**
 * Created by Aman on 2/3/2016.
 */
public class MoveForwardAuto extends Autonomous_Library{

    public void init() {
        initializeMapping();

    }

    public void loop () {
        drive(1.0f, 1.0f);
    }
}
