package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/* Copyright (c) 2019 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "R.A.C.ShippingElement", group = "Concept")
// @Disabled
public class RedAutoCloseShippingElement extends LinearOpMode {
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

    /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
     * the following 4 detectable objects
     *  0: Ball,
     *  1: Cube,
     *  2: Duck,
     *  3: Marker (duck location tape marker)
     *
     *  Two additional model assets are available which only contain a subset of the objects:
     *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
     *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
     */
    private static final String TFOD_MODEL_ASSET = "model_20220206_120127.tflite";
    private static final String[] LABELS = {
            "Shipping Element"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AZEbmZ//////AAABmW2mj2n/EkjVvjU1K3PJ8F0W51aLwEga1Fcbme7XgF2kJzXZ4leybiWaW4obLbNbNSJukafGoT+Qs5ku5sjke1o5eDttLXzyNll1GULSxBCxJJE910VPVCL58nI6yt4IjaEOpsMVEOcbNfvwl8WN4pl67OGv13GYCRYS8ZT7Mtp+VFYySOMuDtrrsXcEnG+ZsSqjbicfhhQoTNIYQNx3Totuo93iNp5hf17MhVBXUzv/ToDZoNf17La62tIhcjhltWyi4S5rZK5Ace233bn+2fyOSGmvgxXFKVm+yGjYiPVNaFRf95Q2iWmd5D7VlMpg/CWyLoy8rEYiVN/LG3y1j36FudKtrHT295iglmIWgTbQ";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {

        int level = 3;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

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

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2.5, 16.0 / 9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        resetStartTime();
        while (opModeIsActive() && getRuntime() < 2) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        if (recognition.getLabel().contains("Duck") || recognition.getLabel().contains("Cube") || recognition.getLabel().contains("Ball") || recognition.getLabel().contains("Shipping Element")) {
                            carousel.setPower(1);
                            level = 2;
                        } else if (recognition.getLabel().isEmpty()) {
                            carousel.setPower(0);
                        }
                        i++;
                    }
                    telemetry.update();
                }
            }
        }
        carousel.setPower(0);
        if (level == 2) {
            cubbyservo1.setPosition(0.1);
            cubbyservo2.setPosition(0);

            // Moving forward from start to the team shipping hub
            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftDrive.setTargetPosition(3000);
            rightDrive.setTargetPosition(3000);
            leftMotor.setTargetPosition(3000);
            rightMotor.setTargetPosition(3000);

            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftDrive.setPower(0.6);
            rightDrive.setPower(0.6);
            leftMotor.setPower(0.6);
            rightMotor.setPower(0.6);

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

            leftDrive.setTargetPosition(700);
            rightDrive.setTargetPosition(-700);
            leftMotor.setTargetPosition(700);
            rightMotor.setTargetPosition(-700);

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

            leftDrive.setTargetPosition(800);
            rightDrive.setTargetPosition(800);
            leftMotor.setTargetPosition(800);
            rightMotor.setTargetPosition(800);

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
            while (opModeIsActive() && (getRuntime() < 3)) {
                lifting1.setPower(0.15);
                lifting2.setPower(0.15);
                cubbyservo1.setPosition(1);
                cubbyservo2.setPosition(1);
                sleep(850);
                cubbyservo1.setPosition(0.1);
                cubbyservo2.setPosition(0);
                sleep(1000);
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


            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftDrive.setTargetPosition(-5500);
            rightDrive.setTargetPosition(-5500);
            leftMotor.setTargetPosition(-5500);
            rightMotor.setTargetPosition(-5500);

            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftDrive.setPower(0.75);
            rightDrive.setPower(0.75);
            leftMotor.setPower(0.75);
            rightMotor.setPower(0.75);

            while (opModeIsActive() && rightMotor.isBusy()) {
                idle();
            }

            leftDrive.setPower(0);
            rightDrive.setPower(0);
            leftMotor.setPower(0);
            rightMotor.setPower(0);


        } else if (level == 3) {
            cubbyservo1.setPosition(0.1);
            cubbyservo2.setPosition(0);

            // Moving forward from start to the team shipping hub
            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftDrive.setTargetPosition(3000);
            rightDrive.setTargetPosition(3000);
            leftMotor.setTargetPosition(3000);
            rightMotor.setTargetPosition(3000);

            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftDrive.setPower(0.6);
            rightDrive.setPower(0.6);
            leftMotor.setPower(0.6);
            rightMotor.setPower(0.6);

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

            leftDrive.setTargetPosition(700);
            rightDrive.setTargetPosition(-700);
            leftMotor.setTargetPosition(700);
            rightMotor.setTargetPosition(-700);

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


            //Parking
            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftDrive.setTargetPosition(-5500);
            rightDrive.setTargetPosition(-5500);
            leftMotor.setTargetPosition(-5500);
            rightMotor.setTargetPosition(-5500);

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



/**
 * Initialize the Vuforia localization engine.
 */
    private void initVuforia () {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
