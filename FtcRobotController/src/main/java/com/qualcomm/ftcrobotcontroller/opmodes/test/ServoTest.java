package com.qualcomm.ftcrobotcontroller.opmodes.test;

import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 11/3/2015.
 */
public class ServoTest extends OpMode {

    Servo srvoScoreClimbers, srvoPushButton;
    Servo srvoRightDeflector, srvoLeftDeflector;

    double climberPos, RDefPos, LDefPos;

    double servoDelta = 0.1;

    public enum ServoAssignment {
        Climbers, RightDef, LeftDef
    }

    ServoAssignment currentServoAssignment = ServoAssignment.Climbers; //enum that represents the current servo testing

    @Override
    public void init() {
        srvoScoreClimbers = hardwareMap.servo.get("s1");
        srvoRightDeflector = hardwareMap.servo.get("s2");
        srvoLeftDeflector = hardwareMap.servo.get("s3");
    }

    @Override
    public void loop() {

        //int servoCurrent = currentServoAssignment;

        switch (currentServoAssignment) {
            case Climbers:

        }

        climberPos = Range.clip(climberPos, 0.0, 1.0);
        RDefPos = Range.clip(RDefPos, 0.0, 1.0);
        LDefPos = Range.clip(LDefPos, 0.0, 1.0);

        srvoScoreClimbers.setPosition(climberPos);
        srvoLeftDeflector.setPosition(LDefPos);
        srvoRightDeflector.setPosition(RDefPos);

        telemetry.addData("Climbers:", ""+climberPos);
        telemetry.addData("Right Deflector:", ""+RDefPos);
        telemetry.addData("Left Deflector:", ""+LDefPos);
    }
}
