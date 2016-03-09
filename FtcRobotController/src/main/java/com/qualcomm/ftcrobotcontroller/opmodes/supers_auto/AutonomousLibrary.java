package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.ftcrobotcontroller.opmodes.AdafruitIMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 3/4/2016.
 */
public abstract class AutonomousLibrary extends LinearOpMode {

    /*private ResQ_Library resQLibrary;
    public void setLibraryBase (ResQ_Library resQLibrary){
        this.resQLibrary = resQLibrary;
    }*/
    // use something like:
    // resQLibrary.initializeMapping(); to declare a function

    //For Driving Only
    public DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;
    public DcMotor motorHangingMech;
    public DcMotor motorTapeMech;
    public DcMotor motorBlockArm;
    public DcMotor motorIntakeSpin;

    //Autonomous
    public Servo srvoScoreClimber, srvoZiplineDrop;

    //Sensors
    public AnalogInput sensorUltra_1;
    public ColorSensor sensorRGB;
    public AdafruitIMU imu;

    int offsetRed_1, offsetGreen_1, offsetBlue_1;


    //****************OTHER DEFINITIONS****************//
    //Ultrasonic algorithm Constants
    final static double RIGHT_TARGET_DISTANCE = 27.0, LEFT_TARGET_DISTANCE = 27.0, STOP_CONST = 6.0;

    //Color Sensor Calibrations
    final static int COLOR_THRESHOLD = 50;

    //gyro offsets
    final static double RIGHT_ROTATION_CONST = 0.0027;
    final static double LEFT_ROTATION_CONST = 0.0027;
    final static double ROTATION_OFFSET = 0.1;

    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];

    //Constants that determine how strong the robot's speed and turning should be
    final static double SPEED_CONST = 0.005, LEFT_STEERING_CONST = 0.85, RIGHT_STEERING_CONST = 0.8;


    //acceleration
    long delayUntil = 0;
    float accelerationTime = 100;
    double accelerationThreshold = 0.1;

    //Other
    public int driveGear = 3; //3 is 100%, 2 is 50%, 1 is 25%

    public enum Color {
        RED, BLUE, GREEN, WHITE, NONE
    }

    //Definitions complete





    public AutonomousLibrary(){}

    public void ChangeEncoderMode (String newMode){
        //Allows us to choose our encoder types super easily
        if (newMode == "Reset"){ //resets encoder values, no set mode
            motorLeftTread.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            motorRightTread.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        } else if (newMode == "Position") { //allows running to a specific position
            motorLeftTread.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            motorRightTread.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        } else if (newMode == "With") { //allows continous movement at a controlled speed
            motorLeftTread.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorRightTread.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        } else if (newMode == "Without") { //runs a motor as if it had no encoder, yet still keeps encoder tick values
            motorLeftTread.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            motorRightTread.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }

    public void encoderDriveStraight (float power, int distance) throws InterruptedException{
        //this allows us to drive straight using encoders
        /*
        Rotation based numbers:
        1: 1120
        2: 2240
        3: 3360
        4: 4480
        5: 5600
         */

        ChangeEncoderMode("Reset");
        motorRightTread.setTargetPosition(distance);
        motorLeftTread.setTargetPosition(distance);
        motorRightTread.setPower(1);
        motorLeftTread.setPower(1);
        ChangeEncoderMode("Position");
        while(motorRightTread.getCurrentPosition() < motorRightTread.getTargetPosition() ||
                motorLeftTread.getCurrentPosition() < motorLeftTread.getTargetPosition()){
            waitOneFullHardwareCycle();
            driveBackWheels(power);
        }
        stopDrive();
        ChangeEncoderMode("With");
    }

    public void driveBackWheels (float power) {
        motorRightSecondTread.setPower(power);
        motorLeftSecondTread.setPower(power);
    }

    public void driveForward(float power, int time) throws InterruptedException{
        drive(power, power);
        sleep(time);
    }

    //Non library code complete





    public void initializeMapping() {
        //Log statements to prevent color errors
        hardwareMap.logDevices();

        //Motors
        motorLeftTread = hardwareMap.dcMotor.get("m1"); //mapped
        motorRightTread = hardwareMap.dcMotor.get("m2");
        motorLeftSecondTread = hardwareMap.dcMotor.get("m3"); //mapped
        motorRightSecondTread = hardwareMap.dcMotor.get("m4");

        motorHangingMech = hardwareMap.dcMotor.get("m5"); //mapped
        motorTapeMech = hardwareMap.dcMotor.get("m6"); //mapped
        motorBlockArm = hardwareMap.dcMotor.get("m7"); //mapped

        //--------------------------

        //Servos
        srvoScoreClimber = hardwareMap.servo.get("s1");
        srvoZiplineDrop = hardwareMap.servo.get("s2");

        //Sensors
        sensorUltra_1 = hardwareMap.analogInput.get("u1");
        sensorRGB = hardwareMap.colorSensor.get("c1");
        try {
            imu = new AdafruitIMU(hardwareMap, "g1", (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2), (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch(RobotCoreException rce) {
            telemetry.addData("RobotCoreException", rce.getMessage());
        }

        //set the direction of the motors
        motorRightTread.setDirection(DcMotor.Direction.REVERSE);
        motorRightSecondTread.setDirection(DcMotor.Direction.REVERSE);
    }

    public void drive(float left, float right) {
        // Drives

        //Clips it just in case there's a problem
        right = (float) Range.clip(right, -1.0, 1.0);
        left = (float)Range.clip(left, -1.0, 1.0);

        //Sets the actual power
        motorRightTread.setPower(right);
        motorLeftTread.setPower(left);
        motorRightSecondTread.setPower(right);
        motorLeftSecondTread.setPower(left);
    }

    /**
     * Changes the speed of each side of robot to try to align it with the initialYaw.
     * The more it is called, the more close the robot will get to the intented angle.
     * Keep in mind that this function does NOT stop the robot.
     * @param initialYaw target yaw for the robot to face.
     */
    public void driveStraight(double initialYaw) {
        double yaw = getYaw();

        double offset = 180 - initialYaw;

        yaw += offset;
        initialYaw += offset;

        double speedLeft = 0.5 + ((yaw - initialYaw)/initialYaw);
        double speedRight = 0.5 - ((yaw - initialYaw)/initialYaw);

        speedLeft = Range.clip(speedLeft, -1.0, 1.0);
        speedRight = Range.clip(speedRight, -1.0, 1.0);

        drive((float) speedLeft, (float) speedRight);
    }

    private boolean foundQuickestRoute = false;
    float rightSpeed;
    float leftSpeed;

    double val = 180;

    /**
     * Turns the robot towards the given degree value via the quickest route. Should be called within a loop.
     * @param degrees degree value for the robot to turn towards.
     * @return Returns true if turn is complete
     */
    public boolean driveTurnDegrees(int degrees) {
        return driveTurnDegrees(degrees, 5);
    }

    /**
     * Turns the robot towards the given degree value via the quickest route. Should be called within a loop.
     * @param degrees degree value for the robot to turn towards.
     * @param precision how precise the turning is. (lower values increase the possibilty of error.)
     * @return Returns true if turn is complete
     */
    public boolean driveTurnDegrees(int degrees, int precision) {
        double initialAngle = getYaw();
        float driveSpeed = 0.5f;
        //the angle across from the initialAngle on a circle
        double oppositeAngle = scaleToAngle(initialAngle + 180);

        /*
         * Here is some pseudo code to try and help explain why the robot knows the quickest route to turn.
         * -----------------------------------------------------------------------------------------------------
         * if(we need to go passed the opposite angle, true || is the opposite angle below 180? If yes, true for
         * all degree values above the initial Angle. : If no, true for all degree values below the inital angle.
         */
        if (!foundQuickestRoute && (degrees > oppositeAngle || (oppositeAngle < 180)? (degrees > initialAngle): (degrees < initialAngle))) {
            //turn negative degrees
            rightSpeed = -driveSpeed;
            leftSpeed = driveSpeed;
        } else if(!foundQuickestRoute){
            //turn positive degrees
            rightSpeed = driveSpeed;
            leftSpeed = -driveSpeed;
        }

        foundQuickestRoute = true;

        //180 > 220 - 2 = 218 && 180 < 220 + 2 = 222
        //if(val > 80 && val < 100) {
        if(getYaw() > scaleToAngle(degrees - precision) && getYaw() < scaleToAngle(degrees + precision)) {
            stopDrive();
            foundQuickestRoute = false;
            return true;
        }

        drive(rightSpeed, leftSpeed);
        return false;
    }

    public void stopDrive() {
        drive(0.0f, 0.0f);
    }

    //****************SENSOR METHODS****************//
    public double getDistance() {
        return sensorUltra_1.getValue();
    }

    /*
     * Callibrates the IMU
     */
    public void startIMU() {
        imu.startIMU();
    }

    /**
     * @return gyro degrees 0-360
     */
    public double getYaw() {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        return 180 + yawAngle[0];
    }

    public void calibrateColors() {
        offsetRed_1 = sensorRGB.red();
        offsetGreen_1 = sensorRGB.green();
        offsetBlue_1 = sensorRGB.blue();
    }

    public Color getHue() {
        int r1 = Math.abs(sensorRGB.red() - offsetRed_1);
        int b1 = Math.abs(sensorRGB.blue() - offsetBlue_1);
        int a1 = Math.abs(sensorRGB.alpha());
        telemetry.addData("Red 1", r1);
        telemetry.addData("Blue 1", b1);
        telemetry.addData("White 1", a1);
        if (b1 > r1 && b1 > COLOR_THRESHOLD) {
            return Color.BLUE;
        } else if (r1 > b1 && r1 > COLOR_THRESHOLD) {
            return Color.RED;
        }
        else if(a1 > 1000) {
            return Color.WHITE;
        }
        else {
            return Color.NONE;
        }
    }

    /**
     * Converts any number to an angle value between 0 - 359.
     */
    public double scaleToAngle(double val) {
        double scaledVal = val;
        while(scaledVal >= 360) {
            scaledVal-=360;
        }
        while(scaledVal < 0) {
            scaledVal+=360;
        }

        return scaledVal;
    }

}
