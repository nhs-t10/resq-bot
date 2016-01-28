package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.sensors;

import com.qualcomm.ftcrobotcontroller.opmodes.AdafruitIMU;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Admin on 1/15/2016.
 */
public class Imu {
    private AdafruitIMU imu;
    private String name;

    volatile double[] rollAngle = new double[2],
            pitchAngle = new double[2],
            yawAngle = new double[2];

    public Imu(String name, HardwareMap h) {
        try {
            imu = new AdafruitIMU(h, name, (byte) (AdafruitIMU.BNO055_ADDRESS_A * 2), (byte) AdafruitIMU.OPERATION_MODE_IMU);
            imu.startIMU();
        } catch(RobotCoreException rc) {
            rc.printStackTrace();
            Telemetry teleImu = new Telemetry();
            teleImu.addData("Imu","Cannot initialize imu! (probably Will's fault)");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void recallibrate() {
        imu.startIMU();
    }

    private void getAngles() {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
    }

    /**
     * Get gyro yaw.
     * @return gyro yaw degrees 0-360
     */
    public double getYaw() {
        getAngles();
        return 180 + yawAngle[0];
    }

    /**
     * Get gyro pitch. Untested, may not work as expected.
     * @return gyro pitch degrees 0-360
     */
    public double getPitch() {
        getAngles();
        return 180 + pitchAngle[0];
    }

    /**
     * Get gyro roll. Untested, may not work as expected.
     * @return gyro roll degrees 0-360
     */
    public double getRoll() {
        getAngles();
        return 180 + rollAngle[0];
    }
}
