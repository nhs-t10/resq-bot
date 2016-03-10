package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Aman on 3/9/2016.
 */
public class EncoderPositionConfig extends ResQ_Library {

    int currentPosition;

    public void init() {
        //Do the map thing
        initializeMapping();
        //startIMU();
        telemetry.addData("Version", "Sensorless. COLOR ERROR SHOULD NOT SHOW UP!");
    }

    public void loop() {
        currentPosition = motorBlockArm.getCurrentPosition();
        telemetry.addData("Current Encoder Position", currentPosition+"");
        motorBlockArm.setPower(0.10);
        motorBlockArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        if(gamepad1.left_trigger >= 0.5f) { //left trigger draws blocks in
            motorBlockArm.setTargetPosition(currentPosition++);
        } else if(gamepad1.right_trigger >= 0.5f) { //right trigger shoots them out
            motorBlockArm.setTargetPosition(currentPosition--);
        }
    }
}
