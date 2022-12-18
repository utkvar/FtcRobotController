/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, thne corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="FourWDrive", group="Linear Opmode")
//Disabled
public class FourWDriveNew extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor lifting1;
    DcMotor lifting2;
    DcMotor intake;
    DcMotor carousel;
    Servo cubbyservo1;
    Servo cubbyservo2;
    TouchSensor liftTouch;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("Front_Left");
        rightMotor = hardwareMap.dcMotor.get("Front_Right");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "Back_Left");
        rightDrive = hardwareMap.get(DcMotor.class, "Back_Right");
        lifting1 = hardwareMap.get(DcMotor.class,"LiftingL");
        lifting2 = hardwareMap.get(DcMotor.class,"LiftingR");
        lifting1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake = hardwareMap.get(DcMotor.class,"intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        cubbyservo1 = hardwareMap.servo.get("CS1");
        cubbyservo2 = hardwareMap.servo.get("CS2");
        cubbyservo2.setDirection(Servo.Direction.REVERSE);
        carousel = hardwareMap.get(DcMotor.class, "CaroWinds");
        liftTouch = hardwareMap.get(TouchSensor.class,"LiftTouch");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        boolean height_moved = false;
        double Current_Pos = 0;
        int height_old = 0;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            double intakePower;
            double liftingPower;
            double intakeRotation;
            double CarouselPower;
            int height1;
            int height2;
            int height3;
            int height;
            double TouchSensorPressed;
            boolean intakeR1;
            boolean intakeR2;
            boolean intakeP1;
            boolean intakeP2;
            boolean height1G1;
            boolean height1G2;
            boolean height2G1;
            boolean height2G2;
            boolean height3G1;
            boolean height3G2;
            boolean caro1;
            boolean caro2;
            boolean liftm1;
            boolean liftm2;
            boolean liftmode;
            boolean servoP2;
            boolean servoP1;
            float servoP;
            float servoPos;
            float servoPos2;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = gamepad1.left_stick_y + gamepad2.left_stick_y;
            double turn  =  gamepad1.right_stick_x + gamepad2.right_stick_x;
            leftPower    = Range.clip(drive - (turn * 1.25), -1.0, 1.0) ;
            rightPower   = Range.clip(drive + (turn * 1.25), -1.0, 1.0) ;
            intakePower = gamepad1.right_trigger + gamepad2.right_trigger;
            intakeR1 = gamepad1.dpad_up ? false : true;
            intakeR2 = gamepad2.dpad_up ? false : true;
            intakeRotation = (intakeR1 == false || intakeR2 == false) ? -1 : 1;
            liftingPower = Range.clip(gamepad1.left_trigger + gamepad2.left_trigger,0.01,0.5);
            servoP1 = gamepad1.right_bumper ? false : true;
            servoP2 = gamepad2.right_bumper ? false : true;
            servoP = (servoP1 == false || servoP2 == false) ? 1 : 0;
            servoPos = (float) (servoP * 0.21 + 0.08);
            servoPos2 = (float) (-0.21 * servoP + 0.92);
            height1G1 = gamepad1.a ? false : true;
            height1G2 = gamepad2.a ? false : true;
            height1 = (height1G1 == false || height1G2 == false) ? 1 : 0;
            height2G1 = gamepad1.b ? false : true;
            height2G2 = gamepad2.b ? false : true;
            height2 = (height2G1 == false || height2G2 == false) ? 2 : 0;
            height3G1 = gamepad1.y ? false : true;
            height3G2 = gamepad2.y ? false : true;
            height3 = (height3G1 == false || height3G2 == false) ? 3 : 0;
            height = Range.clip(height1 + height2 + height3,0,3);
            caro1 = gamepad1.x ? false : true;
            caro2 = gamepad2.x ? false : true;
            CarouselPower = (caro1 == false || caro2 == false) ? 1 : -0.001;
            TouchSensorPressed = (liftTouch.isPressed()) ? 0.01 : 1;
            liftm1 = gamepad1.dpad_down ? false : true;
            liftm2 = gamepad2.dpad_down ? false : true;
            liftmode = (liftm1 == false || liftm2 == false) ? true: false;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
            intake.setPower(intakeRotation * intakePower);
            lifting1.setPower(liftingPower);
            lifting2.setPower(liftingPower);
            if (height_old != height) {
                height_moved = false;
            }
            if (liftmode == true) {
                if (height == 1) {
                    if (height_moved == false) {
                        if (Current_Pos == 0) {
                            resetStartTime();
                            while (getRuntime() < 0.5 && !liftTouch.isPressed()) {
                                lifting1.setPower(0.5);
                                lifting2.setPower(0.5);
                            }
                        }
//                        else if (Current_Pos == 1000) {
//                            resetStartTime();
//                            while (getRuntime() < 1 && !liftTouch.isPressed()) {
//                                lifting1.setPower(0.001);
//                                lifting2.setPower(0.001);
//                            }
//                        }
                        Current_Pos = 500;
                        height_moved = true;
                        height_old = height;
                    }
                    else if (height_moved == true) {
                        lifting1.setPower(0.15);
                        lifting2.setPower(0.15);
                    }
                }
                else if (height == 2 || height == 3) {
                    if (height_moved == false) {
                        if (Current_Pos == 0) {
                            resetStartTime();
                            while (getRuntime() < 1.3 && !liftTouch.isPressed()) {
                                lifting1.setPower(0.5);
                                lifting2.setPower(0.5);
                            }
                        }
                        else if (Current_Pos == 500) {
                            resetStartTime();
                            while (getRuntime() < 0.5 && !liftTouch.isPressed()) {
                                lifting1.setPower(0.5);
                                lifting2.setPower(0.5);
                            }
                        }
                        Current_Pos = 1000;
                        height_moved = true;
                        height_old = height;
                    }
                    else {
                        lifting1.setPower(0.15);
                        lifting2.setPower(0.15);
                    }
                }
            }
            else if (liftmode == false) {
                Current_Pos = 0;
                lifting1.setPower(liftingPower * TouchSensorPressed);
                lifting2.setPower(liftingPower * TouchSensorPressed);
            }
            cubbyservo1.setPosition((Range.clip(servoP,0.15,0.75)));
            cubbyservo2.setPosition((Range.clip(servoP, 0.15, 0.75)));
            carousel.setPower(intakeRotation * CarouselPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Servos", "left (%.2f), right (%.2f)", servoPos, servoPos2);
            telemetry.addData("height", "A_B_or_Y height: " + height);
            telemetry.addData("height_moved", "A.B.Y height_moved:" + height_moved);
            telemetry.addData("height_old", "ABY: " + height_old);
            telemetry.addData("Current_Pos","CPOS :" + Current_Pos);
            telemetry.update();
        }
    }
}

