package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.sensors;

import com.qualcomm.ftcrobotcontroller.opmodes.AdafruitIMU;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Admin on 1/15/2016.
 */
public class UltraSonic {
    private AnalogInput ult;
    private String name;

    public UltraSonic(String name, HardwareMap h) {
        ult = h.analogInput.get(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return ult.getValue();
    }
}
