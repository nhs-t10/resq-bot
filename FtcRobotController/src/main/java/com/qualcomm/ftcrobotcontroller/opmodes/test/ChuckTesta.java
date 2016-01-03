package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/*
    Mox's UBER SECRET SPECIAL TEST OPMODE. FIND OUT WATS WRONG!!
*/
public class ChuckTesta extends OpMode {

    DcMotor m1, m2, m3, m4;
    int task = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Get ready, it's CHUCK TESTA time.");
        telemetry.addData("Task", "Now initializing motors. Reason for error? Probably not configged right.");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        m4 = hardwareMap.dcMotor.get("m4");
    }

    @Override
    public void loop() {
        Double t = this.time;
        int time = t.intValue();
        switch (time) {
            case 1:
                telemetry.addData("Status", "Please stand clear. YEE HAW!");
                break;
            case 3:
                write("Turning Left", 0, 1, 0, 1);
                break;
            case 6:
                write("Turning Right", 1, 0, 1, 0);
                break;
            case 10:
                write("done", 0, 0, 0, 0);
                break;
        }
    }
    public void write(String desc, float M1, float M2, float M3, float M4){
        m1.setPower(M1);
        m2.setPower(M2);
        m3.setPower(M3);
        m4.setPower(M4);
        telemetry.addData("Task", desc);
    }
    public void finish(){
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }
    public void say(String status, String task, String hardware) {
        if(status != "") telemetry.addData("Status", status);
        if(task != "") telemetry.addData("Task", task);
        if(hardware != "")telemetry.addData("Hardware", hardware);
    }
}
