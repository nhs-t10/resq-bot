package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by Aman on 3/1/2016.
 *
 * The purpose of this class is to test things before they're implemented into actual code.
 * It will also function as an interim library
 */
public class TestingAuto extends AutonomousLibrary {

    public enum Team {
        RED, BLUE, UNKNOWN
    }

    protected Team teamWeAreOn; //enum that represent team

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        autonomous();
    }

    public void initialize() {
        initializeMapping();
        startIMU();
    }

    public void autonomous() throws InterruptedException {
        looper();

        driveTurnDegrees(180);
        driveStraight(180);
        sleep(2L);
    }

    public void looper() {

    }
}


