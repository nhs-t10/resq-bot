package com.qualcomm.ftcrobotcontroller.opmodes.test;

import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 11/3/2015.
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

        //change what servo we're using
        if(gamepad2.a){
            //change to climbers
            currentServoAssignment = ServoAssignment.Climbers;
        }
        if(gamepad2.b){
            //change to right deflector
            currentServoAssignment = ServoAssignment.RightDef;
        }
        if(gamepad2.x){
            //change to left deflector
            currentServoAssignment = ServoAssignment.LeftDef;
        }

        // change servo positions
        if (gamepad2.left_bumper){ //increase servo
            if (currentServoAssignment == ServoAssignment.Climbers){
                climberPos += servoDelta;
            } else if (currentServoAssignment == ServoAssignment.RightDef) {
                RDefPos += servoDelta;
            } else if (currentServoAssignment == ServoAssignment.LeftDef) {
                LDefPos += servoDelta;
            }
        } else if (gamepad2.right_bumper) { //decrease servo
            if (currentServoAssignment == ServoAssignment.Climbers){
                climberPos -= servoDelta;
            } else if (currentServoAssignment == ServoAssignment.RightDef) {
                RDefPos -= servoDelta;
            } else if (currentServoAssignment == ServoAssignment.LeftDef) {
                LDefPos -= servoDelta;
            }
        }

        //servo manipulation and settings
        climberPos = Range.clip(climberPos, 0.0, 1.0);
        RDefPos = Range.clip(RDefPos, 0.0, 1.0);
        LDefPos = Range.clip(LDefPos, 0.0, 1.0);

        srvoScoreClimbers.setPosition(climberPos);
        srvoLeftDeflector.setPosition(LDefPos);
        srvoRightDeflector.setPosition(RDefPos);

        if (currentServoAssignment == ServoAssignment.Climbers){
            telemetry.addData("Current:", "Climber Drop");
        } else if (currentServoAssignment == ServoAssignment.RightDef) {
            telemetry.addData("Current:", "Right Deflector");
        } else if (currentServoAssignment == ServoAssignment.LeftDef) {
            telemetry.addData("Current:", "Left Deflector");
        }
        telemetry.addData("Climbers:", "" +climberPos);
        telemetry.addData("Right Deflector:", ""+RDefPos);
        telemetry.addData("Left Deflector:", ""+LDefPos);
    }
}
