package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 1/24/2016.
 */
public class GaiaBot extends ResQLibrary2 {

    @Override
    public void init() {
        initializeMapping();
    }

    @Override
    public void loop() {
        float X = ProcessToMotorFromJoy(-gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y); //NOT inverted

        float V = (100-Math.abs(X)) * (Y/100) + Y; // R+L
        float W = (100-Math.abs(Y)) * (X/100) + X; // R-L

        float right = (V+W)/2;
        float left = (V-W)/2;

        right = Range.clip(right, -0.9f, 0.9f);
        left = Range.clip(left, -0.9f, 0.9f);

        drive(left, right);
    }

    @Override
    public void initializeMapping() {
        motorRightTread = hardwareMap.dcMotor.get("motor_1");
        motorLeftTread = hardwareMap.dcMotor.get("motor_2");
        motorRightSecondTread = hardwareMap.dcMotor.get("motor_3");
        motorLeftSecondTread = hardwareMap.dcMotor.get("motor_4");

        motorRightTread.setDirection(DcMotor.Direction.REVERSE);
        motorRightSecondTread.setDirection(DcMotor.Direction.REVERSE);
    }
}
