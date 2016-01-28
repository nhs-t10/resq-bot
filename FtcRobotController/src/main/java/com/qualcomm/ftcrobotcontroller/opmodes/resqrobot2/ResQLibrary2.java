package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 */
public abstract class ResQLibrary2 extends OpMode {
    //****************HARDWARE MAPPING DEFINITIONS****************//

    //For Driving Only
    DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;
    DcMotor motorHangingMech;
    DcMotor motorTapeMech;
    DcMotor motorEnArm;

    //Other
    Servo srvoRightDeflector, srvoLeftDeflector;
    Servo srvoBlockDropper;

    public ResQLibrary2() {

    }

    //****************INITIALIZE METHOD****************//
    public void initializeMapping() {
        hardwareMap.logDevices();

        //Drive motors
        motorLeftTread = hardwareMap.dcMotor.get("m1");
        motorRightTread = hardwareMap.dcMotor.get("m2");
        motorLeftSecondTread = hardwareMap.dcMotor.get("m3");
        motorRightSecondTread = hardwareMap.dcMotor.get("m4");
        //Hang
        motorHangingMech = hardwareMap.dcMotor.get("m5");
        motorTapeMech = hardwareMap.dcMotor.get("m6");
        //Block Manipulation
        motorEnArm = hardwareMap.dcMotor.get("m7");

        //set modes of the motors
        motorEnArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRightTread.setDirection(DcMotor.Direction.REVERSE);
        motorRightSecondTread.setDirection(DcMotor.Direction.REVERSE);
    }
    //****************DRIVE METHODS****************//

    //Reading from compass sensor

    public void drive(float left, float right) {
        //Clips it just in case there's a problem
        right = (float)Range.clip(right, -1.0, 1.0);
        left = (float)Range.clip(left, -1.0, 1.0);


        //Sets the actual power
        motorRightTread.setPower(right);
        motorLeftTread.setPower(left);
        motorRightSecondTread.setPower(right);
        motorLeftSecondTread.setPower(left);
    }

    public void stopDrive() {
        drive(0.0f, 0.0f);
    }

    //****************NUMBER MANIPULATION METHODS****************//

    public float ProcessToMotorFromJoy(float input) { //This is used in any case where joystick input is to be converted to a motor
        float output = 0.0f;

        // clip the power values so that the values never exceed +/- 1
        output = Range.clip(input, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        output = (float) scaleInput(output);

        return output;
    }

    /**
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
	 */
    public double scaleInput(double dVal) {
     /*
      * This method scales the joystick input so for low joystick values, the
      * scaled value is less than linear.  This is to make it easier to drive
      * the robot more precisely at slower speeds.
      */
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        index = Math.abs(index);

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

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "");
        }
    }
}