package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware;

import android.bluetooth.BluetoothClass;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 1/29/2016.
 */
public class Config {
    private static HardwareMap h = null;
    private static Telemetry t = new Telemetry();

    /**
     * Stores the hardware map for hardware use.
     * @param h the robot's hardware map
     */
    public static void setHardwareMap(HardwareMap h) {
        if(h == null) {
            return;
        }

        Config.h = h;
    }

    /**
     * Get the hardware map stored.
     * @return the stored hardware map
     */
    public static HardwareMap getHardwareMap() throws ConfigException {
        if(h == null) {
            throw new ConfigException();
        }
        return h;
    }

    /**
     * Get the hardware map without checking if HardwareMap is null.
     * It is recommended that you use getHardwareMap instead.
     * @return the stored hardware map
     */
    public static HardwareMap getHardwareMapUnsafe() {
        return h;
    }

    /**
     * Get a basic telemetry object
     * @return telemetry object
     */
    public static Telemetry getTelemetry() {
        return t;
    }
}
