package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This is the not official testing code to have sensors assist in teleop matches.
 */
public class TeleopAssist extends ResQ_Library {

    double srvoHang1Position;
    double srvoHang2Position;
    boolean flip90Left = false;
    int flip90LeftDest = 0;

    @Override
    public void init() {
        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //Do the map thing
        initializeMapping();

        //srvoHang1Position = srvoHang_1.getPosition();
        //srvoHang2Position = srvoHang_2.getPosition();
        //srvoHang_1.setPosition(1.0);
    }


    @Override
    public void loop() {

        float right = ProcessToMotorFromJoy(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessToMotorFromJoy(-gamepad1.left_stick_y);
        /*
         * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        drive(left, right); //Used with tracks

        //Drive modifications
        if (gamepad1.left_trigger > 0.8 && !flip90Left) {
            flip90Left = true;
            flip90LeftDest = (int) getYaw() + 90;
        }
        if(flip90Left && getYaw() < flip90LeftDest) {
            drive(0.5f, 0);
        }

        double[] wallAngles = {0, 90, 180, 270};
        if(gamepad1.dpad_up) {
            driveTurnDegrees((int)wallAngles[0]);
        } else if(gamepad1.dpad_right) {
            driveTurnDegrees((int)wallAngles[1]);
        } else if(gamepad1.dpad_down) {
            driveTurnDegrees((int)wallAngles[2]);
        } else if(gamepad1.dpad_left) {
            driveTurnDegrees((int)wallAngles[3]);
        }
    }

    int getClosestAngle(double num, double[] angles) {
        double scaledAngle;
        double closestAngle = num;
        for(Double angle : angles) {
            scaledAngle = scaleToAngle(angle);
            if(closestAngle >= scaleToAngle(scaledAngle - num)) {
                closestAngle = scaledAngle;
            }
        }
        return (int)closestAngle;
    }

    /*
            Mox's UBER SECRET SPECIAL TEST OPMODE. FIND OUT WATS WRONG!!
        */
    public static class ChuckTesta extends OpMode {

        DcMotor m1, m2, m3, m4;
        int task = 0;

        @Override
        public void init() {
            telemetry.addData("Status", "Get ready, it's CHUCK TESTA time.");
            telemetry.addData("Task", "Now initializing motors. Reason for error? Probably not configged right.");
            m1 = hardwareMap.dcMotor.get("m1");
            m2 = hardwareMap.dcMotor.get("m2");
            m3 = hardwareMap.dcMotor.get("m3");
            m4 = hardwareMap.dcMotor.get("m4");
        }

        @Override
        public void loop() {
            Double t = this.time;
            int time = t.intValue();
            switch (time) {
                case 1:
                    telemetry.addData("Status", "Please stand clear. YEE HAW!");
                    break;
                case 3:
                    write("Turning Left", 0, 1, 0, 1);
                    break;
                case 6:
                    write("Turning Right", 1, 0, 1, 0);
                    break;
                case 10:
                    write("done", 0, 0, 0, 0);
                    break;
            }
        }
        public void write(String desc, float M1, float M2, float M3, float M4){
            m1.setPower(M1);
            m2.setPower(M2);
            m3.setPower(M3);
            m4.setPower(M4);
            telemetry.addData("Task", desc);
        }
        public void finish(){
            m1.setPower(0);
            m2.setPower(0);
            m3.setPower(0);
            m4.setPower(0);
        }
        public void say(String status, String task, String hardware) {
            if(status != "") telemetry.addData("Status", status);
            if(task != "") telemetry.addData("Task", task);
            if(hardware != "")telemetry.addData("Hardware", hardware);
        }
    }
}