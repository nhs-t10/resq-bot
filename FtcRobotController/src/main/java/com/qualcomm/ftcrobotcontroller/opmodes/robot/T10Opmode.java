package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 */
public abstract class T10Opmode extends OpMode {
    //****************HARDWARE MAPPING DEFINITIONS****************//

    //For Driving Only
    DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;
    DcMotor motorHangingMech;
    DcMotor motorTapeMech;
    DcMotor motorEnArm;

    //Other
    Servo srvoRightDeflector, srvoLeftDeflector;
    Servo srvoBlockDropper;

    public T10Opmode() {
        Config.setHardwareMap(hardwareMap);
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

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "");
        }
    }
}