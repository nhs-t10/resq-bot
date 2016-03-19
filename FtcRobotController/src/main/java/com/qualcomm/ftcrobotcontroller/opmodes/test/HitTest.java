package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Admin on 3/18/2016.
 */
public class HitTest extends ResQ_Library {
    float position;

    public void init() {
        initializeMapping();
        position = 0.5f;
    }

    public void loop() {
        telemetry.addData("Servo Pos", srvoBlockHit.getPosition());
        telemetry.addData("Wanted Position", position);

        if(gamepad1.left_trigger > 0.5f) {
            position++;
        } else if (gamepad1.right_trigger > 0.5f) {
            position--;
        }

        srvoBlockHit.setPosition(position);
    }
}
