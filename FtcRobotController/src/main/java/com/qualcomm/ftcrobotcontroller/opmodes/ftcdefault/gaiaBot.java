/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.ftcdefault;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class gaiaBot extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorRight1;
    DcMotor motorLeft1;

    /**
     * Constructor
     */
    public gaiaBot() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are four motors: "motor_1", "motor_2", "motor_3", and "motor_4".
		 *   "motor_1" is on the front left side of the bot.
		 *   "motor_2" is on the front right side of the bot.
		 *   "motor_3" is on the back left side of the bot.
		 *   "motor_4" is on the back right side of the bot.
		 *   
		 * We don't assume we have any servos on the test bot, but it could happen.
		 *    "servo_1" would control the arm joint of the manipulator.
		 *    "servo_6" would control the claw joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("motor_1");
        motorLeft = hardwareMap.dcMotor.get("motor_2");
        motorRight1 = hardwareMap.dcMotor.get("motor_3");
        motorLeft1 = hardwareMap.dcMotor.get("motor_4");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);


    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1, in this instance, is the only controller. It drives the test robot. If servos and others are added, we can easily map it
		 * out and program it.
		 */

        // here we are mimicking a tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);
        motorRight1.setPower(right);
        motorLeft1.setPower(left);


        //Motor Right = motor 1
        //Motor Left= motor2
        //Motor Right1= motor 3
        //Motor Left1= motor 4

        // stop robot
        if (gamepad1.a) {
            motorRight.setPower(0f);
            motorLeft.setPower(0f);
            motorRight1.setPower(0f);
            motorLeft1.setPower(0f);
        }

        // turning
        if (gamepad1.left_bumper) {
            motorRight.setPower(.7f);
            motorLeft.setPower(-.7f);
            motorRight1.setPower(.7f);
            motorLeft1.setPower(-.7f);
        }

        if (gamepad1.right_bumper) {
            motorLeft.setPower(.7f);
            motorRight.setPower(-.7f);
            motorLeft1.setPower(.7f);
            motorRight1.setPower(-.7f);
        }

        if (gamepad1.right_bumper && gamepad1.left_bumper)
            motorLeft.setPower(1f);
            motorRight.setPower(1f);
            motorLeft1.setPower(1f);
            motorRight1.setPower(1f);

        //driving can be done using both bumpers or triggers, users choice. #triggered
        //Driving

        if (gamepad1.left_trigger > 0.25 && gamepad1.right_trigger > 0.25) {
            motorRight.setPower(-.7f);
            motorLeft.setPower(-.7f);
            motorRight1.setPower(-.7f);
            motorLeft1.setPower(-.7f);
        }

        if (gamepad1.right_trigger > .25)
            motorLeft.setPower(.7f);
            motorRight.setPower(.7f);
            motorLeft1.setPower(.7f);
            motorRight1.setPower(.7f);

        // intricate steering for accuracy
        if (gamepad1.b) {
            motorLeft.setPower(-.1f);
            motorRight.setPower(-.1f);
            motorLeft1.setPower(-.1f);
            motorRight1.setPower(-.1f);
        }

        if (gamepad1.x) {
            motorLeft.setPower(.1f);
            motorRight.setPower(.1f);
            motorLeft1.setPower(.1f);
            motorRight1.setPower(.1f);

        }

        if (gamepad1.y) {
            motorRight.setPower(1f);
            motorLeft.setPower(1f);
            motorRight1.setPower(1f);
            motorLeft1.setPower(1f);

        }


		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
    }


    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
