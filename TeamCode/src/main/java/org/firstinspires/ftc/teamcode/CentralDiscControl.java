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
public class CentralDiscControl extends LinearOpMode {

    // variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor discControl = null;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. 
        // Names are the same as on the Control Hub. (and are semi-arbetrary)
        discControl = hardwareMap.get(DcMotor.class, "discControl"); //expansion 0

        // reset the position of the disc, and set the proper motor function
        // this needs an encoder
        discControl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        discControl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        discControl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // ^ defines at 0 power what the motor should do
        discControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // 1440 ticks per revolution
        int motorSpeed = 1440;
        int rotations = 0;
        // to show which position of the three states the system is actually in
        int absPosition = 0;

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Run until the end of the match (Driver presses STOP)
        while (opModeIsActive()) {

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

           // to figure out the absolute position of the disk
           absPosition = rotations;
            if absPosition > 2 { // to get it between 0-2 if it is > 2
                while absPosition > 2 {
                    absPosition -= 3;
                }
            } elif absPosition < 0 { // to get it between 0-2 if it is < 0
                whlie absPosition < 0 {
                    absPosition += 3;
                }
            }

            /*
                THIS IS FOR THE MANUAL MODE ON THE CENTRAL DISK
            */

            // rotate counterclockwise
            if (gamepad1.x || gamepad1.y){
                rotations -= 1;
                discControl.setTargetPosition(motorSpeed * (rotations/3));
            }

            // rotate clockwise
            if (gamepad1.b || gamepad1.a){
                rotations += 1;
                discControl.setTargetPosition(motorSpeed * (rotations/3));
            }



        }
    }
}
