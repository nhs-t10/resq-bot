package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Aman on 3/1/2016.
 *
 * 25 points
 Circumstance: Alliance will work on our beacon late
 Rush to beacon, score climbers
 Turn and park in floor goal out of the way
 They’ll likely score at least 25-45 more for a net 50-70 points autonomous
 */
public class AutoCircumLateAlly extends LinearOpMode {

    private ResQ_Library resQLibrary;
    public AutoCircumLateAlly(ResQ_Library resQLibrary){
        this.resQLibrary = resQLibrary;
    }
    @Override
    public void runOpMode() throws InterruptedException {
        resQLibrary.drive(1.0f, 1.0f);

    }
}