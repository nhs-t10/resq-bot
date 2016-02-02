package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.motors.Motor;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.motors.Servo;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors.Imu;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors.UltraSonic;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 */
public abstract class T10Opmode extends OpMode {
    //****************HARDWARE MAPPING DEFINITIONS****************//
    protected DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;
    protected DcMotor motorHangingMech;
    protected DcMotor motorTapeMech;
    protected DcMotor motorEnArm;

    protected Servo srvoRightDeflector, srvoLeftDeflector;
    protected Servo srvoScoreClimbers;
    protected Servo srvoBlockDropper;

    protected Imu sensorImu;
    protected UltraSonic sensorUltra1;

    public T10Opmode() {
        hardwareMap.logDevices();
        Config.setHardwareMap(hardwareMap);
    }

    //****************INITIALIZE METHOD****************//
    public void initializeMapping() {
        //Drive
        motorLeftTread = new Motor("m1");
        motorRightTread = new Motor("m2", DcMotor.Direction.REVERSE);
        motorLeftSecondTread = new Motor("m3");
        motorRightSecondTread = new Motor("m4", DcMotor.Direction.REVERSE);
        //Hang
        motorHangingMech = new Motor("m5");
        motorTapeMech = new Motor("m6");
        //Block Manipulation
        motorEnArm = new Motor("m7");
        motorEnArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        srvoScoreClimbers = new Servo("s1");
        srvoRightDeflector = new Servo("s2");
        srvoLeftDeflector = new Servo("s3");
        srvoBlockDropper = new Servo("s4");

        sensorImu = new Imu("g1");
        sensorUltra1 = new UltraSonic("u1");
    }
    //****************DRIVE METHODS****************//

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

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "");
        }
    }
}