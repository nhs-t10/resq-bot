package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.debug.Log;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.RobotMath;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors.*;

/**
 * Created by William Patsios on 1/15/2016.
 */
public class ResQ_Drive extends T10Opmode {
    Imu i;
    UltraSonic u;

    @Override
    public void init() {
        initializeMapping();
        Log l = new Log();
        l.writeLine("Hello World!");
        i = new Imu("gyro");
        u = new UltraSonic("ultra");
    }

    public void start() {
        motorEnArm.setTargetPosition(0);
    }

    public void loop() {
        singleStickDrive();

        //Winch
        /*if (gamepad2.right_bumper) {
            //release tension by letting go of string
            motorHangingMech.setPower(-1.0f);
            telemetry.addData("Winch:", "Releasing String");
        } else if (gamepad2.right_trigger >= 0.5f) {
            //pull string and add tension
            motorHangingMech.setPower(1.0f);
            telemetry.addData("Winch:", "Pulling String");
        } else {
            motorHangingMech.setPower(0);
        }

        //Tape
        if (gamepad2.left_bumper) {
            //bring the tape back;
            motorTapeMech.setPower(-1.0f);
            telemetry.addData("Tape:", "Pulling Tape");
        } else if (gamepad2.left_trigger >= 0.5f) {
            //send the tape out
            motorTapeMech.setPower(1.0f);
            telemetry.addData("Tape:", "Sending Tape");
        } else {
            motorTapeMech.setPower(0);
        }*/
    }

    public void singleStickDrive() {
        //0.5, 0.5
        float X = RobotMath.processToMotorFromJoy(-gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = RobotMath.processToMotorFromJoy(-gamepad1.left_stick_y); //NOT inverted

        //(100 - 0.5) * 1/200 + 0.5
        float V = (100-Math.abs(X)) * (Y/100) + Y; // R+L
        float W = (100-Math.abs(Y)) * (X/100) + X; // R-L

        float right = (V+W)/2;
        float left = (V-W)/2;

        telemetry.addData("Will Information", "" + V + ", " + W);

        right = Range.clip(right, -0.9f, 0.9f);
        left = Range.clip(left, -0.9f, 0.9f);

        drive(left, right);
    }
}
