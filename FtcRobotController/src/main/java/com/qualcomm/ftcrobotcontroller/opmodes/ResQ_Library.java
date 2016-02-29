package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

import java.sql.Time;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 */
public abstract class ResQ_Library extends OpMode {
    //****************HARDWARE MAPPING DEFINITIONS****************//

    //For Driving Only
    public DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;
    public DcMotor motorHangingMech;
    public DcMotor motorTapeMech;
    public DcMotor motorBlockArm;
    public DcMotor motorIntakeSpin;

    //Autonomous
    public Servo srvoScoreClimberRight, srvoScoreClimberLeft, srvoIntakeManager, srvoZiplineDrop;

    //Sensors
    public AnalogInput sensorUltra_1, sensorUltra_2;
    public ColorSensor sensorRGB_1, sensorRGB_2;
    public AdafruitIMU imu;

    public CompassSensor compassSensor;

    int offsetRed_1, offsetGreen_1, offsetBlue_1, offsetRed_2, offsetBlue_2, offsetGreen_2;


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
    int driveGear = 3; //3 is 100%, 2 is 50%, 1 is 25%

    public enum Color {
        RED, BLUE, GREEN, WHITE, NONE
    }

    Color colorDecided = Color.NONE; //enum thats represent team

    public ResQ_Library() {

    }

    //****************INITIALIZE METHOD****************//
    public void initializeMapping() {
        //Log statements to prevent color errors
        hardwareMap.logDevices();

        //Motors
        motorLeftTread = hardwareMap.dcMotor.get("m1");
        motorRightTread = hardwareMap.dcMotor.get("m2");
        motorLeftSecondTread = hardwareMap.dcMotor.get("m3");
        motorRightSecondTread = hardwareMap.dcMotor.get("m4");
        motorHangingMech = hardwareMap.dcMotor.get("m5");
        motorTapeMech = hardwareMap.dcMotor.get("m6");
        motorBlockArm = hardwareMap.dcMotor.get("m7");
        motorIntakeSpin = hardwareMap.dcMotor.get("m8");

        //Servos
        //srvoScoreClimberRight = hardwareMap.servo.get("s1");
        //srvoScoreClimberLeft = hardwareMap.servo.get("s2");
        srvoIntakeManager = hardwareMap.servo.get("s3");
        srvoZiplineDrop = hardwareMap.servo.get("s4");

        //Sensors
        //(color sensors are initialized w/ loadSensor(Team)
        sensorUltra_1 = hardwareMap.analogInput.get("u1");
        /*try {
            imu = new AdafruitIMU(hardwareMap, "g1", (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2), (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch(RobotCoreException rce) {
            telemetry.addData("RobotCoreException", rce.getMessage());
        }*/

        //set the direction of the motors
        motorRightTread.setDirection(DcMotor.Direction.REVERSE);
        motorRightSecondTread.setDirection(DcMotor.Direction.REVERSE);
    }

    //****************DRIVE METHODS****************//

    //Reading from compass sensor
    public double getCompassDirection() {
        return compassSensor.getDirection();
    }

    /*public void loadSensor(Team t) {
        String myteam = t == Team.RED ? "colorR" : "colorL";
        //sensorRGB_1 = hardwareMap.colorSensor.get(myteam);
        //sensorRGB_2 = hardwareMap.colorSensor.get(myteam);
    }*/

    public void drive(float left, float right) {
        // Drives

        //Drive modification code
        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            left = 1f * left;
            right = 1f * right;
        } else if (driveGear == 2) { //medium 50% setting
            left = 0.5f * left;
            right = 0.5f * right;
        } else if (driveGear == 1) { //lowest 25% setting
            left = 0.25f * left;
            right = 0.25f * right;
        } //if there's a bug and it's not 1, 2 or 3, default to max drive


        //right = Accelerate(right, "right");
        //left = Accelerate(left, "left");

        //Clips it just in case there's a problem
        right = (float)Range.clip(right, -1.0, 1.0);
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

        double speedLeft = 0.5 + ((yaw - initialYaw)/initialYaw);
        double speedRight = 0.5 - ((yaw - initialYaw)/initialYaw);

        speedLeft = Range.clip(speedLeft, -1.0, 1.0);
        speedRight = Range.clip(speedRight, -1.0, 1.0);

        drive((float)speedLeft, (float)speedRight);
    }

    /*This method should not be working...*/
    @Deprecated
    public void driveStraight() {
        /*
        * This algorithm assumes yawAngle[0] returns
        * values between 0—359 or -180—179.
        */

        double millis = 1000;

        double startDir = getYaw();
        double startTime = System.currentTimeMillis();
        double currentTime = 0.0;

        double rSpeed = 1.0f;
        double lSpeed = 1.0f;

        while(currentTime - startTime < millis) {
            rSpeed = (startDir + yawAngle[0]) * RIGHT_ROTATION_CONST + ROTATION_OFFSET;
            lSpeed = (startDir - yawAngle[0]) * LEFT_ROTATION_CONST + ROTATION_OFFSET;
            //360 + 100 = 40
            //360 - 100 = 260

            //round any values <0 or >1 to 0 or 1.
            rSpeed = Math.max(0, Math.min(1.0, rSpeed));
            lSpeed = Math.max(0, Math.min(1.0, lSpeed));

            drive((float) lSpeed, (float) rSpeed);
            currentTime = System.currentTimeMillis();
            sleep(10);
        }
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

    public void approach(int direction){
        float leftPower = 0.2f * direction;
        float rightPower = 0.2f * direction;
        drive(leftPower, rightPower);
    }

    public void setDriveGear(int gear) {
        driveGear = normalizeForGear(gear);
    }

    /*
     * Function to automatically move the hanging arm.
     * Should be called in a loop.
     * @return returns true when the arm is in the process of moving.
     */
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

    public boolean moveToClosestObject() {
        double ultraRight, ultraLeft;
        double rightSpeed, leftSpeed;

        ultraRight = sensorUltra_1.getValue(); //set these values to sensor readout
        ultraLeft = sensorUltra_2.getValue();

        rightSpeed = SPEED_CONST * (ultraRight - RIGHT_TARGET_DISTANCE) + -RIGHT_STEERING_CONST * (ultraLeft - ultraRight); //speedl = kpd * (dl - tl) + kps * (dl - dr)
        leftSpeed = SPEED_CONST * (ultraLeft - LEFT_TARGET_DISTANCE) + -LEFT_STEERING_CONST * (ultraRight - ultraLeft);

        drive((float) rightSpeed, (float) leftSpeed);

        if (Math.abs(ultraRight - RIGHT_TARGET_DISTANCE) + Math.abs(ultraLeft - LEFT_TARGET_DISTANCE) < STOP_CONST) {
            return true;
        }

        return false;
    }

    public void calibrateColors() {
        offsetRed_1 = sensorRGB_1.red();
        offsetGreen_1 = sensorRGB_1.green();
        offsetBlue_1 = sensorRGB_1.blue();
        offsetRed_2 = sensorRGB_2.red();
        offsetGreen_2 = sensorRGB_2.green();
        offsetBlue_2 = sensorRGB_2.blue();
    }

    /*public Team getColor() {
        Color color = getHue();
        if (color == Color.BLUE) {
            return Team.BLUE;
        } else if (color == Color.RED) {
            return Team.RED;
        } else {
            return Team.UNKNOWN;
        }
    }*/

    public Color getHue() {
        int r1 = Math.abs(sensorRGB_1.red() - offsetRed_1), r2 = Math.abs(sensorRGB_2.red() - offsetRed_2);
        int b1 = Math.abs(sensorRGB_1.blue() - offsetBlue_1), b2 = Math.abs(sensorRGB_2.blue() - offsetBlue_2);
        int a1 = Math.abs(sensorRGB_1.alpha()), a2 = Math.abs(sensorRGB_2.alpha());
        telemetry.addData("Red 1", r1);
        telemetry.addData("Blue 1", b1);
        telemetry.addData("White 1", a1);
        if ((b1 > r1 && b1 > COLOR_THRESHOLD) || (b2 > r2 && b2 > COLOR_THRESHOLD)) {
            return Color.BLUE;
        } else if ((r1 > b1 && r1 > COLOR_THRESHOLD) || (r2 > b2 && r2 > COLOR_THRESHOLD)) {
            return Color.RED;
        }
        else if((a1 > 1000) || (a2 > 1000)) {
            return Color.WHITE;
        }
        else {
            return Color.NONE;
        }
    }

    //****************NUMBER MANIPULATION METHODS****************//
    public void accelerateForward() {
        //get average powers for left and right motors (prevents any motor bias)
        double right = (motorLeftTread.getPower() + motorLeftSecondTread.getPower())/2;
        double left = (motorRightTread.getPower() + motorRightSecondTread.getPower())/2;
    }

    public float Accelerate (float value, String side) {
        //Acceleration Code
        /**
         * How it works:
         *  We collect the current power for both sides of motors and deal with them separately
         *  We find the delta between the desired motor value and the current power
         *  We use the system's time to time a change amount. If the system's time is more than the delay, it changes
         *  If the time threshold has not been crossed, the target power is the current power (doesn't change)
         *
         *  We compare the change amount to the threshold (currently .1); if the change is bigger, we restrict it
         *  if the change is lower, we'll let it pass exactly as it is instead of changing by .1
         *
         *  the actual power variable changes, so we'll pass that below the if statement whether or not the time works
         */
        float motorCurrentPower = 0;

        if(side.equals("right")) {
            motorCurrentPower = (float)motorRightTread.getPower();
        } else if(side.equals("left")) {
            motorCurrentPower = (float)motorLeftTread.getPower();
        }  else {
            telemetry.addData("Error", "Motor values unable to accelerate");
            return value;
        }

        float powerChange = value - motorCurrentPower;

        if (System.currentTimeMillis() > delayUntil) { // We can change it
            //We're changing it by too much, restrict it
            if (Math.abs(powerChange) > accelerationThreshold) {
                if(value != 0) {
                    motorCurrentPower += accelerationThreshold * value / Math.abs(value);
                }else{
                    motorCurrentPower = 0;
                }
                delayUntil = System.currentTimeMillis() + (long) accelerationTime;
            } else {
                // Small enough change that we can allow it right away
                delayUntil = 0;
                motorCurrentPower = value;
            }
        }
        return motorCurrentPower;
    }

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
     * Converts any number to an angle value between 0 - 359.
     */
    public double scaleToAngle(double val) {
        double scaledVal = Math.abs(val) + 360;
        while(scaledVal >= 360) {
            scaledVal-=360;
        }
        return scaledVal;
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

    private int normalizeForGear(int gear) {
        if (gear > 3) gear = 3;
        if (gear < 1) gear = 1;
        return gear;
    }

    public void DropClimber () {
        srvoScoreClimberRight.setPosition(1.0f);
        srvoScoreClimberLeft.setPosition(1.0f);
        sleep(2000);
        srvoScoreClimberRight.setPosition(0.0f);
        srvoScoreClimberLeft.setPosition(0.0f);
    }

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "It's TIME to stop.");
        }
    }
}