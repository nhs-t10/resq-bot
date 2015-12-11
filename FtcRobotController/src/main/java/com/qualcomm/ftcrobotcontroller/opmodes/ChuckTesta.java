package com.qualcomm.ftcrobotcontroller.opmodes;

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
                telemetry.addData("Task", "Turning left.");
                drive(.5f, 0f);
                break;
            case 6:
                telemetry.addData("Task", "Turning Right.");
                drive(0f, .5f);
                break;
            case 9:
                telemetry.addData("Task", "M1 and m2!! yeahhh!!");
                m1.setPower(1f);
                m2.setPower(1f);
                break;
            case 11:
                telemetry.addData("Task", "now testing both");
                finish();
                break;
            case 12:
                telemetry.addData("Task", "M1");
                m1.setPower(1f);
                break;
            case 15:
                finish();
                break;
            case 16:
                telemetry.addData("Task", "M2");
                m2.setPower(1f);
                break;
            case 19:
                telemetry.addData("Task", "done.");
                drive(0f, 0f);
                break;
        }
    }

    public void drive(float left, float right) {
        telemetry.addData("Hardware", "Motors should be movin! yee haw!!");
        m1.setPower(right);
        m2.setPower(left);
        m3.setPower(right);
        m4.setPower(left);
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
