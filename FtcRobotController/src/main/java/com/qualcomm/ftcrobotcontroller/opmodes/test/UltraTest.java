package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by Admin on 11/3/2015.
 */
public class UltraTest extends ResQ_Library {
    @Override
    public void init() {
        initializeMapping();
    }

    @Override
    public void loop() {
        telemetry.addData("Distance", getDistance());
    }
}
