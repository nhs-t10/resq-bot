package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Admin on 11/3/2015.
 */
public class ServoTest extends OpMode {

    Servo srvoScoreClimbers, srvoPushButton;
    Servo srvoRightDeflector, srvoLeftDeflector;

    @Override
    public void init() {
        srvoScoreClimbers = hardwareMap.servo.get("s1");
        srvoRightDeflector = hardwareMap.servo.get("s2");
        srvoLeftDeflector = hardwareMap.servo.get("s3");
    }

    @Override
    public void loop() {

        //Add increment based code for these three servos

        telemetry.addData("Climbers:", ""+srvoScoreClimbers.getPosition());
        telemetry.addData("Right Deflector:", ""+srvoRightDeflector.getPosition());
        telemetry.addData("Left Deflector:", ""+srvoLeftDeflector.getPosition());
    }
}
