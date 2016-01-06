package com.qualcomm.ftcrobotcontroller.opmodes.test;

import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 11/3/2015.
 */
public class ServoTest extends OpMode {

    Servo srvoBlockGrabber;

    double grabberPos;

    double servoDelta = 0.1;

    public enum ServoAssignment {
        grabber
    }

    ServoAssignment currentServoAssignment = ServoAssignment.grabber; //enum that represents the current servo testing

    @Override
    public void init() {
        srvoBlockGrabber = hardwareMap.servo.get("s4");
    }

    @Override
    public void loop() {
        grabberPos = srvoBlockGrabber.getPosition();

        //change what servo we're using
        if(gamepad2.b){
            //change to grabber
            currentServoAssignment = ServoAssignment.grabber;
        }

        // change servo positions
        if (gamepad2.left_bumper){ //increase servo
            if (currentServoAssignment == ServoAssignment.grabber) {
                //srvoBlockGrabber.setPosition(Range.clip(srvoBlockGrabber.getPosition()+0.001, 0.0, 1.0));
                srvoBlockGrabber.setPosition(1.0);
            }
        } else if (gamepad2.right_bumper) { //decrease servo
            if (currentServoAssignment == ServoAssignment.grabber) {
                //srvoBlockGrabber.setPosition(Range.clip(srvoBlockGrabber.getPosition()-0.001, 0.0, 1.0));
                srvoBlockGrabber.setPosition(0.0);
            }
        }

        //servo manipulation and settings
        //grabberPos = Range.clip(grabberPos, 0.0, 1.0);

        //srvoBlockGrabber.setPosition(grabberPos);

        if (currentServoAssignment == ServoAssignment.grabber) {
            telemetry.addData("Current: ", "Grabber");
        }
        telemetry.addData("Grabber", ""+srvoBlockGrabber.getPosition());
    }
}
