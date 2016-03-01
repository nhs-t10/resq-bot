package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Admin on 2/29/2016.
 */
public class StraightTest extends ResQ_Library {
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() {
        driveStraight(180);
    }
}
