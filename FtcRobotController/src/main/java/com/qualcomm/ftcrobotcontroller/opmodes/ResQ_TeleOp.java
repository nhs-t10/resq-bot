package com.qualcomm.ftcrobotcontroller.opmodes;

//import android.database.CrossProcessCursor;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 10/11/2015.
 */
public class ResQ_TeleOp extends ResQ_Library {

    public enum Team {
        RED, BLUE, UNKNOWN
    }

    protected Team teamWeAreOn; //enum that represent team

    //position instance variables
    int bucketDownPos;
    int bucketMidPos;
    int bucketUpPos;

    float zipPos;
    double zipTime;

    double hitInitPos;

    public boolean SpeedToggle = false; //if set true, drive speed is capped at .6

    @Override
    public void init() {
        //Do the map thing
        initializeMapping();
        //startIMU();
        telemetry.addData("Version", "Sensorless. COLOR ERROR SHOULD NOT SHOW UP!");
        /*motorBlockArm.setPower(0.25);
        motorBlockArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorBlockArm.setTargetPosition(0);*/

        zipPos = 0.9f;
        zipTime = getRuntime();

        hitInitPos = srvoBlockHit.getPosition();

        if (teamWeAreOn == Team.RED) {
            bucketDownPos = 0;
            bucketMidPos = 30;
            bucketUpPos = 575;

        } else if (teamWeAreOn == Team.BLUE) {
            bucketDownPos = 0;
            bucketMidPos = -30;
            bucketUpPos = -575;
        }
    }


    @Override
    public void loop() {
        //telemetry.addData("yaw", getYaw());
        //telemetry.addData("distance", getDistance());
        /*
         * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        /* Procedure for Aman's tank single stick drive code:
            Get X and Y from the Joystick, do whatever scaling and calibrating you need to do based on your hardware.
            Invert X
            Calculate R+L (Call it V): V =(100-ABS(X)) * (Y/100) + Y
            Calculate R-L (Call it W): W= (100-ABS(Y)) * (X/100) + X
            Calculate R: R = (V+W) /2
            Calculate L: L= (V-W)/2
            Re-calibrate to work with motor hardware
            Send those values to your Robot.
         */

        float X = ProcessToMotorFromJoy(gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = ProcessToMotorFromJoy(gamepad1.left_stick_y); //NOT inverted

        float V = (100 - Math.abs(X)) * (Y / 100) + Y; // R+L
        float W = (100 - Math.abs(Y)) * (X / 100) + X; // R-L

        float right = (V + W) / 2;
        float left = (V - W) / 2;

        if (SpeedToggle) {
            right = Range.clip(right, -0.2f, 0.2f);
            left = Range.clip(left, -0.2f, 0.2f);
        } else {
            right = Range.clip(right, -1f, 1f);
            left = Range.clip(left, -1f, 1f);
        }

        // we're reversing it because we are redefining the front of the robot without actually changing anything hardware.
        drive(-left, -right);

        if (gamepad1.a) {
            SpeedToggle = false;
        } else if (gamepad1.b) {
            SpeedToggle = true;
        }


        //****************BLOCK SCORING****************//

        /**gamepad 1:
         * left trigger = while held, spin intake inwards priority DONE
         * right trigger = while held, spin blocks out DONE
         * button x: move bucket to 0 then unpower
         * button y: move bucket to 200
         * button b: move bucket to 550 (score)
         *
         * gamepad 2:
         * button x: set intake to bottom scoring pos 10 - .04
         * button y: set intake to lifted pos 40 - .16
         * button b: set intake to parking pos 200 - .78
         */

        //Gamepad 1 Functions

        if (gamepad1.x) { //moves arm to bottom then powers it off.
            motorBlockArm.setPower(0.85);
        } else if (gamepad1.y) { //moves arm to above ramp pos
            motorBlockArm.setPower(-0.85);
        } else {
            motorBlockArm.setPower(0);
        }


        if (gamepad1.left_trigger >= 0.5f) { //left trigger draws blocks in
            srvoIntake_1.setPosition(1);
            srvoIntake_2.setPosition(0);
        } else if (gamepad1.right_trigger >= 0.5f) { //right trigger shoots them out
            srvoIntake_1.setPosition(0);
            srvoIntake_2.setPosition(1);
        } else {
            srvoIntake_1.setPosition(0.50);
            srvoIntake_2.setPosition(0.50);
        }

        if (gamepad1.left_bumper) { //left trigger draws blocks in
            srvoBlockHit.setPosition(0.3);
        } else {
            srvoBlockHit.setPosition(0.9);
        }


        //****************OTHER****************//

        //Hanging urself
        /**
         * Winch: Right trigger is +tension, right bumper is -tension
         * Tape: Left trigger is +length, left bumper is -length
         */
        //Winch
        if (gamepad2.right_bumper) {
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
            motorTapeMech.setPower(-0.5f);
            telemetry.addData("Tape:", "Pulling Tape");
        } else if (gamepad2.left_trigger >= 0.5f) {
            //send the tape out
            motorTapeMech.setPower(0.5f);
            telemetry.addData("Tape:", "Sending Tape");
        } else {
            motorTapeMech.setPower(0);
        }

        ///Teleop Zipline drop
        if (gamepad2.x && getRuntime() - zipTime > 0.3d) {
            zipTime = getRuntime();
            zipPos = zipPos == 0.9f? 0.0f : 0.9f;
            srvoZiplineDrop.setPosition(zipPos);
        }

        if (gamepad2.a) {
            srvoScoreClimber.setPosition(0.0f);
        } else {
            srvoScoreClimber.setPosition(1.0f);
        }
    }
}
