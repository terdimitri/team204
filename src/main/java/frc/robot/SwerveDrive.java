package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;

class EncoderTo extends Thread {
    int target;
    SpeedController motor;
    Encoder encoder;
    boolean inverted;

    EncoderTo(int target, SpeedController motor, Encoder encoder, boolean inverted) {
        this.target = target;
        this.motor = motor;
        this.encoder = encoder;
        this.inverted = inverted;
    }

    public void run() {
        int encoderVal;
        do {
            encoderVal = encoder.get();
            motor.set(towards(encoderVal, target, inverted));
        } while (Math.abs(encoderVal - target) > 10);
    }

    static double towards(int source, int target, boolean inverted) {
        if (source == target) return 0.0;
        boolean out;
        if (source < target) out = true;
        else out = false;
        if (inverted) out = !out;

        if (out) return 1.0;
        return -1.0;
    }
}

public class SwerveDrive extends RobotDriveBase {

    SwerveController frontLeft;
    SwerveController frontRight;
    SwerveController backLeft;
    SwerveController backRight;
    double wheelbase, trackwidth;

    /* The wheelbase is the distance between front and rear wheels.
     * The trackwidth is the distance between left side wheels and right side wheels.
     * Units do not matter as only the ratios are used.
     */
    SwerveDrive (SwerveController frontLeft, SwerveController frontRight,
                 SwerveController backLeft, SwerveController backRight,
                 double wheelbase, double trackwidth) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.wheelbase = wheelbase;
        this.trackwidth = trackwidth;
    }

    // All parameters from -1 to 1, as in joystick values
    // strafe is oriented rightwards and rotate is oriented clockwise
    void set(double forward, double strafe, double rotate) {

        assert -1.0 <= forward && forward <= 1 : "`forward` value is out of bounds";
        assert -1.0 <= strafe && strafe <= 1 : "`strafe` value is out of bounds";
        assert -1.0 <= forward && forward <= 1 : "`forward` value is out of bounds";

        double R = Math.sqrt(wheelbase*wheelbase + trackwidth*trackwidth);

        double a = strafe - rotate*wheelbase/R;
        double b = strafe + rotate*wheelbase/R;
        double c = forward - rotate*trackwidth/R;
        double d = forward + rotate*trackwidth/R;

        double speedfr = Math.sqrt(b*b + c*c);
        double anglefr = Math.atan2(b, c);
        double speedfl = Math.sqrt(b*b + d*d);
        double anglefl = Math.atan2(b, d);
        double speedbr = Math.sqrt(a*a + d*d);
        double anglebr = Math.atan2(a, d);
        double speedbl = Math.sqrt(a*a + c*c);
        double anglebl = Math.atan2(a, c);

        double max = Math.max(Math.max(speedfr, speedfl), Math.max(speedbr, speedbl));

        if (max > 1) {
            speedfl /= max;
            speedfr /= max;
            speedbl /= max;
            speedbr /= max;
        }

        frontLeft.setSpeed(speedfl);
        frontRight.setSpeed(speedfr);
        backRight.setSpeed(speedbl);
        backLeft.setSpeed(speedbr);



    }

    @Override
    public void stopMotor() {
        frontLeft.stopMotor();
        frontRight.stopMotor();
        backLeft.stopMotor();
        backRight.stopMotor();
    }

    @Override
    public String getDescription() {
        return "";
    }
}