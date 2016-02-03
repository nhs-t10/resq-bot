package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.sensors.Color;

/**
 * Created by Admin on 11/3/2015.
 */
public class ColorTest extends OpMode {

    Color c;

    @Override
    public void init() {
        c = new Color("color", hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("red", c.red());
    }
}
