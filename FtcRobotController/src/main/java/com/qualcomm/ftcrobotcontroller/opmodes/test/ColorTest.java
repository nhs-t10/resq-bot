package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.debug.Log;

/**
 * Created by Admin on 2/29/2016.
 */
public class ColorTest extends ResQ_Library {
    public void init() {
        initializeMapping();
        startIMU();
    }

    public void loop() {
        int alpha = getAlpha();
        telemetry.addData("Alpha: ", alpha);
        if(alpha > 1500) {
            stopDrive();
        } else {
            drive(0.2f, 0.2f);
        }
    }

    public void stop() {
    }
}
