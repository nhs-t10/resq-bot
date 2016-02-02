package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.motors;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

/**
 * Created by Admin on 1/29/2016.
 */
public class Motor extends DcMotor {
    public Motor(String name) {
        //Super haxy way to extend DcMotor without giving the DcMotorController or the port number.
        //Also you cant place code before a "super" statement cause java. So this will have to look ugly.
        super(Config.getHardwareMapUnsafe().dcMotor.get(name).getController(), Config.getHardwareMapUnsafe().dcMotor.get(name).getPortNumber());
    }

    public Motor(String name, DcMotor.Direction direction) {
        //Super haxy way to extend DcMotor without giving the DcMotorController or the port number.
        //Also you cant place code before a "super" statement cause java. So this will have to look ugly.
        super(Config.getHardwareMapUnsafe().dcMotor.get(name).getController(), Config.getHardwareMapUnsafe().dcMotor.get(name).getPortNumber());
        this.setDirection(direction);
    }
}
