package com.qualcomm.ftcrobotcontroller.opmodes;

//import android.database.CrossProcessCursor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 10/11/2015.
 */
public class ResQ_TeleOp extends ResQ_Library {

    double srvoHang1Position;
    double srvoHang2Position;

    boolean autoMoveArm;

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
    }


    @Override
    public void loop() {

        /*float right = ProcessToMotorFromJoy(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessToMotorFromJoy(-gamepad1.left_stick_y);*/
        /*
         * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        /* Procedure:
            Get X and Y from the Joystick, do whatever scaling and calibrating you need to do based on your hardware.
            Invert X
            Calculate R+L (Call it V): V =(100-ABS(X)) * (Y/100) + Y
            Calculate R-L (Call it W): W= (100-ABS(Y)) * (X/100) + X
            Calculate R: R = (V+W) /2
            Calculate L: L= (V-W)/2
            Re-calibrate to work with motor hardware
            Send those values to your Robot.
         */
        float X = -ProcessToMotorFromJoy(-gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y); //NOT inverted

        float V = (100-Math.abs(X)) * (Y/100) + Y; // R+L
        float W = (100-Math.abs(Y)) * (X/100) + X; // R-L

        float right = (V+W)/2;
        float left = (V-W)/2;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        //drive(left, right);

        telemetry.addData("Right:", ""+right);
        telemetry.addData("Left:", ""+left);

        //Drive modifications
        if (gamepad1.x) {
            //Track speed 100%
            setDriveGear(3);
        }
        if (gamepad1.y) {
            //Track speed 50%
            setDriveGear(2);
        }
        if (gamepad1.b) {
            //Track speed 25%
            setDriveGear(1);
        }
        if (gamepad1.a) {
            //reverse drive
            driveReverse = !driveReverse;
        }

        //****************BLOCK MANIPULATION****************//

        if (gamepad2.x) {
            //toggle block intake
        }
        if (gamepad2.a) {
            //Toggle conveyor movement
            if (isConveyorMoving) { //it's already moving, stop it

                isConveyorMoving = !isConveyorMoving;
            } else { //its not, so start it up

            }
        }
        if (gamepad2.b) {
            //have the servo switch which side the blocks fall into
            //create an enum code to know which side the servo is already facing by default so we can change
        }

        //****************OTHER****************//

        //Hanging Winch
        if (gamepad2.y) {
            //Hanging automation procedure
            //HangingAutomation();
        }

        //Hanging
        /**
         * Winch: Right trigger is +tension, right bumper is -tension
         * Tape: Left trigger is +length, left bumper is -length
         */
        //Winch
        if (gamepad2.right_bumper) {
            //release tension by letting go of string
            motorHangingMech.setPower(-1.0f);
        } else if (gamepad2.right_trigger >= 0.5f) {
            //pull string and add tension
            motorHangingMech.setPower(1.0f);
        } else {
            motorHangingMech.setPower(0);
        }

        //Tape
        if (gamepad2.left_bumper) {
            //release tension by letting go of string
            motorTapeMech.setPower(-1.0f);
        } else if (gamepad2.left_trigger >= 0.5f) {
            //pull string and add tension
            motorTapeMech.setPower(1.0f);
        } else {
            motorTapeMech.setPower(0);
        }




        //****************TELEMETRY****************/

        String tel_Bool_Reverse = (driveReverse) ? "REVERSED" : "normal";
        String tel_Bool_Speed = "error speed";
        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            tel_Bool_Speed = "at 100% speed";
        } else if (driveGear == 2) { //medium 50% setting
            tel_Bool_Speed = "at 50% speed";
        } else if (driveGear == 1) { //lowest 25% setting
            tel_Bool_Speed = "at 25% speed";
        }
        telemetry.addData("", "Driving is " + tel_Bool_Reverse + " and " + tel_Bool_Speed);

    }
}