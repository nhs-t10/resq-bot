package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.debug.Log;

/**
 * Created by Admin on 2/29/2016.
 */
public class StraightTest extends ResQ_Library {
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() {
        telemetry.addData("Yaw", getYaw());
        telemetry.addData("Motor Left", motorLeftTread.getPower());
        telemetry.addData("Motor Right", motorRightTread.getPower());

        driveStraight(180);
        sleep(2000);
    }

    public void stop() {
        telemetry.addData("Yaw", getYaw());
        telemetry.addData("Motor Left", motorLeftTread.getPower());
        telemetry.addData("Motor Right", motorRightTread.getPower());

        Log l = new Log();
        l.writeLine("Yaw: " + getYaw());
        l.writeLine("Motor Left: " + motorLeftTread.getPower());
        l.writeLine("Motor Right: " + motorRightTread.getPower());
    }
}
