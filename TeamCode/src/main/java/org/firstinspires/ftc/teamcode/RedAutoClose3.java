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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name="RedAutoClose3 ", group="Linear Opmode")
//Disabled
public class RedAutoClose3 extends LinearOpMode {

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
        leftDrive = hardwareMap.get(DcMotor.class, "Back_Left");
        rightDrive = hardwareMap.get(DcMotor.class, "Back_Right");
        lifting1 = hardwareMap.get(DcMotor.class, "LiftingL");
        lifting2 = hardwareMap.get(DcMotor.class, "LiftingR");
        lifting1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        cubbyservo1 = hardwareMap.servo.get("CS1");
        cubbyservo2 = hardwareMap.servo.get("CS2");
        cubbyservo2.setDirection(Servo.Direction.REVERSE);
        carousel = hardwareMap.get(DcMotor.class, "CaroWinds");
        liftTouch = hardwareMap.get(TouchSensor.class, "LiftTouch");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Initialize servos
        cubbyservo1.setPosition(0.1);
        cubbyservo2.setPosition(0);

        // Moving forward from start to the team shipping hub
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(2885);
        rightDrive.setTargetPosition(2885);
        leftMotor.setTargetPosition(2885);
        rightMotor.setTargetPosition(2885);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        telemetry.addData("Started :", "Yes!");
        telemetry.update();

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Turning right to score in the team shipping hub
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(780);
        rightDrive.setTargetPosition(-780);
        leftMotor.setTargetPosition(780);
        rightMotor.setTargetPosition(-780);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.35);
        rightDrive.setPower(0.35);
        leftMotor.setPower(0.35);
        rightMotor.setPower(0.35);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);


        // Lifting the lift to score
        resetStartTime();
        while (opModeIsActive() && getRuntime() < 2) {
            lifting1.setPower(0.5);
            lifting2.setPower(0.5);
        }


        telemetry.addData("done1", "done :" + "Yes!");
        telemetry.update();

        // backing up for placement
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(900);
        rightDrive.setTargetPosition(900);
        leftMotor.setTargetPosition(900);
        rightMotor.setTargetPosition(900);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.25);
        rightDrive.setPower(0.25);
        leftMotor.setPower(0.25);
        rightMotor.setPower(0.25);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);



        // Scoring with the cubby servos
        resetStartTime();
        while (opModeIsActive() && (getRuntime() < 4)) {
            lifting1.setPower(0.15);
            lifting2.setPower(0.15);
            cubbyservo1.setPosition(1);
            cubbyservo2.setPosition(1);
            sleep(620);
            cubbyservo1.setPosition(0.25);
            cubbyservo2.setPosition(0.25);
            sleep(1500);
            cubbyservo1.setPosition(0.1);
            cubbyservo2.setPosition(0);
            sleep(500);
        }

        sleep(500);

        telemetry.addData("done2", "done :" + "Yes!");
        telemetry.update();

        // bringing everything down


        telemetry.addData("servo returned?", "done" + "Yes!");
        telemetry.update();

        resetStartTime();
        while (opModeIsActive() && (getRuntime() < 4)) {
            lifting1.setPower(0.01);
            lifting2.setPower(0.01);
        }

        telemetry.addData("done3", "done :" + "Yes!");
        telemetry.update();

        // 360 for parking
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(-750);
        rightDrive.setTargetPosition(-750);
        leftMotor.setTargetPosition(-750);
        rightMotor.setTargetPosition(-750);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(-2300);
        rightDrive.setTargetPosition(2300);
        leftMotor.setTargetPosition(-2300);
        rightMotor.setTargetPosition(2300);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        //Parking
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(4500);
        rightDrive.setTargetPosition(4500);
        leftMotor.setTargetPosition(4500);
        rightMotor.setTargetPosition(4500);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);


        //Setting up for tele-op
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(2700);
        rightDrive.setTargetPosition(-2700);
        leftMotor.setTargetPosition(2700);
        rightMotor.setTargetPosition(-2700);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        while (opModeIsActive() && rightMotor.isBusy()) {
            idle();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);

    }
}

