package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
            flip90LeftDest = add90(getYaw());
        }
        if(flip90Left && getYaw() < flip90LeftDest) {
            drive(0.5f, 0);
        }

        double[] wallAngles = {0, 90, 180, 270};
        if(gamepad1.dpad_up) {
            driveTurnDegrees((int)wallAngles[0]);
        } else if(gamepad1.dpad_right) {
            driveTurnDegrees((int)wallAngles[1]);
        } else if(gamepad1.dpad_down) {
            driveTurnDegrees((int)wallAngles[2]);
        } else if(gamepad1.dpad_left) {
            driveTurnDegrees((int)wallAngles[3]);
        }
    }

    int getClosestAngle(double num, double[] angles) {
        double scaledAngle;
        double closestAngle = num;
        for(Double angle : angles) {
            scaledAngle = scaleToAngle(angle);
            if(closestAngle >= scaleToAngle(scaledAngle - num)) {
                closestAngle = scaledAngle;
            }
        }
        return (int)closestAngle;
    }

    int add90(double yaw) {
        int ret = (int) yaw + 90;
        if(ret >= 360) return 360 - ret;
        else return ret;
    }
}