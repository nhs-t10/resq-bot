package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by Admin on 11/3/2015.
 */
public class UltraTest extends OpMode {
    AnalogInput u1, u2;
    DigitalChannel puls;
    DeviceInterfaceModule cdim;
    static final int PULSE = 0;


    @Override
    public void init() {
        u1 = hardwareMap.analogInput.get("u1");
        u2 = hardwareMap.analogInput.get("u2");
        puls = hardwareMap.digitalChannel.get("pulse");

        /*cdim = hardwareMap.deviceInterfaceModule.get("dim");
        cdim.setDigitalChannelMode(PULSE, DigitalChannelController.Mode.OUTPUT);
        cdim.setDigitalChannelState(PULSE, true); //pulse
        sleep(20);
        cdim.setDigitalChannelState(PULSE, false); //pulse*/
        puls.setState(true);
        sleep(100);
        puls.setState(false);
    }

    @Override
    public void loop() {
        telemetry.addData("Distance 1", u1.getValue());
        telemetry.addData("Distance 2", u2.getValue());
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "");
        }
    }
}
