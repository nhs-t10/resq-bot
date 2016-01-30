package com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.sensors;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.lang.exception.ConfigException;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.hardware.Config;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Admin on 1/15/2016.
 */
public class Imu implements Sensor {
    private HardwareMap h;
    private AdafruitIMU imu;
    private String name;

    volatile double[] rollAngle = new double[2],
            pitchAngle = new double[2],
            yawAngle = new double[2];

    public Imu(String name) {
        initialize(name);
    }

    /**
     * Initialize Sensor
     * @param name name of the sensor
     */
    public void initialize(String name) {
        try {
            h = Config.getHardwareMap();
        } catch(ConfigException ce) {
            ce.printStackTrace();
            return;
        }

        try {
            imu = new AdafruitIMU(h, name, (byte) (AdafruitIMU.BNO055_ADDRESS_A * 2), (byte) AdafruitIMU.OPERATION_MODE_IMU);
            imu.startIMU();
        } catch(RobotCoreException rc) {
            rc.printStackTrace();
            Config.getTelemetry().addData("Imu", "Cannot initialize imu! (probably Will's fault)");
        }
        this.name = name;
    }

    /**
     * Get name of sensor
     * @return name of sensor
     */
    public String getName() {
        return name;
    }

    /**
     * Callibrate the Imu. Can be called multiple times
     * to recallibrate.
     */
    public void callibrate() {
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
