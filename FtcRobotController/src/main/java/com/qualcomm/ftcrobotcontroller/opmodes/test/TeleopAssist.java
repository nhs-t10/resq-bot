package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * This is the not official testing code to have sensors assist in teleop matches.
 */
public class TeleopAssist extends ResQ_Library {

    double srvoHang1Position;
    double srvoHang2Position;
    boolean flip90Left = false;
    int flip90LeftDest = 0;

    @Override
    public void init() {
        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //Do the map thing
        initializeMapping();

        //srvoHang1Position = srvoHang_1.getPosition();
        //srvoHang2Position = srvoHang_2.getPosition();
        //srvoHang_1.setPosition(1.0);
    }


    @Override
    public void loop() {

        float right = ProcessToMotorFromJoy(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessToMotorFromJoy(-gamepad1.left_stick_y);
        /*
         * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        drive(left, right); //Used with tracks

        //Drive modifications
        if (gamepad1.left_trigger > 0.8 && !flip90Left) {
            flip90Left = true;
            flip90LeftDest = (int) getYaw() + 90;
        }
        if(flip90Left && getYaw() < flip90LeftDest) {
            drive(0.5f, 0);
        }

        if(gamepad1.dpad_up) {
            driveTurnDegrees(0);
        } else if(gamepad1.dpad_right) {
            driveTurnDegrees(90);
        } else if(gamepad1.dpad_down) {
            driveTurnDegrees(180);
        } else if(gamepad1.dpad_left) {
            driveTurnDegrees(270);
        }
    }
}