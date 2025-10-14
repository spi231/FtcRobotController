package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This is code that will work with either a Mecanum Drive or an X-Drive with holonomic wheels.
*/
@TeleOp(name="OmniDrive", group="Linear OpMode")
//@Disabled
public class MecanumDrive extends LinearOpMode {

    // variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor armTilt2 = null;
    private DcMotor armScissor = null;
    private DcMotor armTilt = null;
    private CRServo armSpin = null;
    private boolean lockArmInClimb = false;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. 
        // Names are the same as on the Control Hub.
        armTilt2 = hardwareMap.get(DcMotor.class, "armTilt2"); //expansion 0
        armTilt = hardwareMap.get(DcMotor.class, "armTilt"); //expansion 2
        armScissor = hardwareMap.get(DcMotor.class, "armScissor"); //expansion 1
        armSpin = hardwareMap.get(CRServo.class, "armSpin"); //servo port 0
        //Reset scissor encoders for the code-stop at extension over 40 cm
        armScissor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armScissor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Throttle for fine movement, extension for the code-lock
            double armExtension= armScissor.getCurrentPosition();

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Arm extend", armScissor.getCurrentPosition());
            telemetry.update();
        //climber code
            // if (gamepad1.dpad_up) {
            //   climber.setPower(1);
            // } else if (gamepad1.dpad_down) {
            //   climber.setPower(-1);
            // } else {
            //   climber.setPower(0);
            // }
            
        //arm extend code - onGround
        
            if (gamepad1.a && (armExtension < 5000)) {
                armScissor.setPower(-1);
            }
            else if (gamepad1.y && (armExtension > -5000)) {
                armScissor.setPower(1);
            } else {
                armScissor.setPower(0);
            }
            
        //arm extend code - up
            if (gamepad1.dpad_up) {
                armScissor.setPower(-1);
            } else if (gamepad1.dpad_down) {
                armScissor.setPower(1);
            }
            
        //arm tilt code
            if (gamepad1.left_bumper) {
                armTilt.setPower(1);
                armTilt2.setPower(1);
            } else if (gamepad1.right_bumper){
                armTilt.setPower(-1);
                armTilt2.setPower(-1);
            } else {
                armTilt.setPower(0);
                armTilt2.setPower(0);
            }
            
        //arm spin code
            if (gamepad1.b) {
                armSpin.setPower(1);
            } else if (gamepad1.x) {
                armSpin.setPower(-1);
            } else {
                armSpin.setPower(0);
            }
            //final climb: locking code so the drivers don't need to
            //hold the button for the entire time
            if (gamepad1.right_stick_button) {
                 lockArmInClimb = true;
            }
            if (lockArmInClimb) {
             armTilt.setPower(-1);
             armTilt2.setPower(-1);
            }
        }
        //we put it outside just for redundancies, even though this never gets called
        if (lockArmInClimb) {
            armTilt.setPower(-1);
            armTilt2.setPower(-1);
        }
    }
}
