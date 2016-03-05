package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Aman on 3/1/2016.
 *
 * The purpose of this class is to test things before they're implemented into actual code.
 * It will also function as an interim library
 */
public class TestingAuto extends LinearOpMode {

    public enum Team {
        RED, BLUE, UNKNOWN
    }
    protected Team teamWeAreOn; //enum that represent team

    private ResQ_Library resQLibrary;
    public TestingAuto(ResQ_Library resQLibrary){
        this.resQLibrary = resQLibrary;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        resQLibrary.drive(1.0f, 1.0f);

    }
}


