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


    @Override
    public void init() {
        //Do the map thing
        initializeMapping();
        telemetry.addData("Version", "Sensorless. COLOR ERROR SHOULD NOT SHOW UP!");

        //Set Deflectors Up
        RDefPos = RDefUpPos;
        LDefPos = LDefUpPos;
        //srvoLeftDeflector.setPosition(LDefPos);
        //srvoRightDeflector.setPosition(RDefPos);
        isDeflectorDown = false;
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
        float X = -ProcessToMotorFromJoy(-gamepad1.left_stick_x); //X is inverted with the negative sign
        float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y); //NOT inverted

        float V = (100-Math.abs(X)) * (Y/100) + Y; // R+L
        float W = (100-Math.abs(Y)) * (X/100) + X; // R-L

        float right = (V+W)/2;
        float left = (V-W)/2;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        drive(left, right);

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

        //****************BLOCK SCORING****************//

        if (gamepad1.a) { //Grabber Servo
            if(isGrabberDown){ //grabber is down, move it back up
                srvoBlockGrabber.setPosition(0.2);
            } else { //grabber is up, move it back down
                srvoBlockGrabber.setPosition(0.7);
            }
            isGrabberDown = !isGrabberDown;
        }

        if (gamepad2.x) { //Dropper Left Pos
            DropperPosition dropperPos = DropperPosition.LEFT;
            srvoBlockDropper.setPosition(0.75);
        } else if (gamepad2.y) { //Dropper Center
            DropperPosition dropperPos = DropperPosition.CENTER;
            srvoBlockDropper.setPosition(0.6);
        } else if (gamepad2.b) { //Dropper Right Pos
            DropperPosition dropperPos = DropperPosition.RIGHT;
            srvoBlockDropper.setPosition(0.5);
        }



        //****************OTHER****************//

        //Hanging
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


        /*if (gamepad1.a) { //Deflector Toggle
            /*if(isDeflectorDown){ //Is down, bring back up
                //set position up
                RDefPos = RDefUpPos;
                LDefPos = LDefUpPos;
                RDefPos = Range.clip(RDefPos, 0.0, 1.0);
                LDefPos = Range.clip(LDefPos, 0.0, 1.0);
            }
            else { //Is up, deploy down
                //set position down
                RDefPos = RDefDownPos;
                LDefPos = LDefDownPos;
                RDefPos = Range.clip(RDefPos, 0.0, 1.0);
                LDefPos = Range.clip(LDefPos, 0.0, 1.0);
            }
            srvoRightDeflector.setPosition(RDefPos);
            srvoLeftDeflector.setPosition(LDefPos);
            if(isDeflectorDown){
                RDefPos += servoDelta;
                LDefPos += servoDelta;
            } else {
                RDefPos -= servoDelta;
                LDefPos -= servoDelta;
            }
            isDeflectorDown = !isDeflectorDown;
        }*/

        if (gamepad1.left_bumper){ //increase servo
            LDefPos -= servoDelta;
            RDefPos += servoDelta;
        } else if (gamepad1.right_bumper) { //decrease servo
            LDefPos += servoDelta;
            RDefPos -= servoDelta;
        }


        RDefPos = Range.clip(RDefPos, 0.0, 0.8);
        LDefPos = Range.clip(LDefPos, 0.2, 1.0);
        //srvoLeftDeflector.setPosition(LDefPos);
        //srvoRightDeflector.setPosition(RDefPos);

        if (gamepad1.a) {
            //lower the climber drop
            //srvoScoreClimbers.setPosition(0.0);
        } else {
            //srvoScoreClimbers.setPosition(1.0);
        }


        //****************TELEMETRY****************/
        /*String tel_Bool_Speed = "error speed";
        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            tel_Bool_Speed = "at 100% speed";
        } else if (driveGear == 2) { //medium 50% setting
            tel_Bool_Speed = "at 50% speed";
        } else if (driveGear == 1) { //lowest 25% setting
            tel_Bool_Speed = "at 25% speed";
        }
        telemetry.addData("", "Driving is " + " and " + tel_Bool_Speed);*/



        telemetry.addData("Current Power Left", "" + (float)motorLeftTread.getPower());
    }
}