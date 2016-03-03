package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.debug.Log;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by William Patsios on 1/15/2016.
 */
public class ResQ_Drive extends ResQ_Library {
    @Override
    public void init() {
        initializeMapping();
        Log l = new Log();
        l.writeLine("Hello World!");
        l.writeLine("Hello Nub!");
    }

    public void start() {

    }

    public void loop() {
        singleStickDrive();
    }

    public void singleStickDrive() {
        //0.5, 0.5
        float X = ProcessToMotorFromJoy(-gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y); //NOT inverted

        //(100 - 0.5) * 1/200 + 0.5
        float V = (100-Math.abs(X)) * (Y/100) + Y; // R+L
        float W = (100-Math.abs(Y)) * (X/100) + X; // R-L

        float right = (V+W)/2;
        float left = (V-W)/2;

        right = Range.clip(right, -0.9f, 0.9f);
        left = Range.clip(left, -0.9f, 0.9f);

        drive(left, right);
    }
}
