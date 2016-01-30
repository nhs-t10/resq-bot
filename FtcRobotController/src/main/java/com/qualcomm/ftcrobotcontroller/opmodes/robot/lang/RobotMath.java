package com.qualcomm.ftcrobotcontroller.opmodes.robot.lang;

import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 1/29/2016.
 */
public class RobotMath {
    /**
     * This is used in any case where joystick input is to be converted to a motor value.
     * @param input input from the joysticks
     * @return motor power that corresponds to the joy input
     */
    public static float processToMotorFromJoy(float input) {
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
    private static double scaleInput(double dVal) {
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

    /**
     * Converts any number to an angle value between 0 - 359.
     * @param val number to be converted
     * @return scaled value
     */
    public static double scaleToAngle(double val) {
        double scaledVal = Math.abs(val) + 360;
        while(scaledVal >= 360) {
            scaledVal-=360;
        }
        return scaledVal;
    }
}
