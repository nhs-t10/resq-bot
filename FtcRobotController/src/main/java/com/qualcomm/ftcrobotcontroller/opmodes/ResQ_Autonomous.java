package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Aman on 10/11/2015.
 */
public class ResQ_Autonomous extends ResQ_Library {
    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster
    final static double HOLD_IR_SIGNAL_STRENGTH = 0.20; // Higher values will cause the robot to follow closer
    final static double LIGHT_THRESHOLD = 0.5;

    double armPosition;
    double clawPosition;

    DcMotor motorRightTread;
    DcMotor motorLeftTread;




    @Override
    public void init() {
        public void ColorSensor(){


    }


    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}
