package com.qualcomm.ftcrobotcontroller.opmodes.supers_auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by Aman on 3/1/2016.
 *
 * 20 points
 Circumstance: Alliance has to work immediately, will not move
 Wait 5-10 seconds for alliance to clear field
 Move towards and climb ramp
 They’ll likely score 25-45 more for a net 45-65 points autonomous
 */
public class AutoCircumLateUs extends LinearOpMode {

 private ResQ_Library resQLibrary;
 public AutoCircumLateUs(ResQ_Library resQLibrary){
  this.resQLibrary = resQLibrary;
 }
 @Override
 public void runOpMode() throws InterruptedException {
  resQLibrary.drive(1.0f, 1.0f);

 }
}