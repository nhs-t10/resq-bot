package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.util.Range;
import java.util.Observable;

/**
 * Created by Admin on 1/22/2016.
 */
public class DegreesTest extends ResQ_Library {
    private  boolean turn;
    private int goal;
    private double  curr;

    @Override
    public void init() {
        turn = true;
        goal = 180;
        curr = 180;

        initializeMapping();
        startIMU();
    }

    public void loop() {
        curr = getYaw();
        telemetry.addData("Goal", goal + "°");
        telemetry.addData("Current Yaw", curr + "°");
        if(turn) {
            float Y = ProcessToMotorFromJoy(-gamepad1.left_stick_y);

            if (Y >= 0.5f) {
                goal += 10;
            } else if (Y <= -0.5f) {
                goal -= 10;
            }

            if (gamepad1.left_stick_button) {
                //startIMU();
                sleep(500);
                turn = false;
            }

            goal = (int) Range.clip(goal, 0.0f, 359.9f);
            sleep(10);
        } else {
            turn = driveTurnDegrees(goal, 7);
        }
    }
}
