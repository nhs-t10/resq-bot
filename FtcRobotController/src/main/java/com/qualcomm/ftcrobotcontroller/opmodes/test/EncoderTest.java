package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Aman on 11/3/2015.
 */
public class EncoderTest extends ResQ_Library {

    DcMotor testingEncoderMotor;


    boolean isGrabberDown = false; //value is set to 0

    @Override
    public void init() {
        testingEncoderMotor = hardwareMap.dcMotor.get("m1");
        testingEncoderMotor.setPower(0.25);
    }

    @Override
    public void loop() {

        /*if (gamepad2.left_bumper){ //increase servo
            testingEncoderMotor.setPower(-1.0f);
        } else if (gamepad2.right_bumper) { //decrease servo
            testingEncoderMotor.setPower(1.0f);
        } else {
            testingEncoderMotor.setPower(0.0f);
        }*/
        /*if (gamepad1.a) { //Grabber Servo
            testingEncoderMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            if(isGrabberDown){ //grabber is down, move it back up
                //srvoBlockGrabber.setPosition(0.2);
                testingEncoderMotor.setTargetPosition(0);
            } else { //grabber is up, move it back down
                //srvoBlockGrabber.setPosition(0.7);
                testingEncoderMotor.setTargetPosition(-440);
            }
            testingEncoderMotor.setPower(1);
            isGrabberDown = !isGrabberDown;
        }*/

        float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y);
        if (Y > 0.25) { // joystick is up
            testingEncoderMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            testingEncoderMotor.setTargetPosition(0);
        }
        else if (Y < -0.25) { //joystick is down
            testingEncoderMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            testingEncoderMotor.setTargetPosition(-440);
        }


        telemetry.addData("joy output", "" + Y);
        telemetry.addData("Current Pos v", ""+testingEncoderMotor.getCurrentPosition());
    }
}
