package frc.robot;

import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.robot.util.Vector2d;

public class SwerveDrive extends RobotDriveBase {

    final double angleTolerance = Math.PI/18;

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
    // strafe is oriented leftwards and rotate is oriented counter-clockwise
    void set(double forward, double strafe, double rotate) {

        // assert good inputs
        assert -1.0 <= forward && forward <= 1 : "`forward` value is out of bounds";
        assert -1.0 <= strafe && strafe <= 1 : "`strafe` value is out of bounds";
        assert -1.0 <= rotate && rotate <= 1 : "`rotate` value is out of bounds";

        // do nothing case
        if (forward == 0 && strafe == 0 && rotate == 0) {
            frontLeft.setSpeed(0);
            frontRight.setSpeed(0);
            backLeft.setSpeed(0);
            backRight.setSpeed(0);

            frontLeft.setTurn(0);
            frontRight.setTurn(0);
            backLeft.setTurn(0);
            backRight.setTurn(0);

            return;
        }

        // constants
        final double R = Math.sqrt(trackwidth*trackwidth + wheelbase*wheelbase);
        final double W = trackwidth/R;
        final double L = wheelbase/R;
        
        // strafe vector
        Vector2d deltafl = new Vector2d(forward, strafe);
        Vector2d deltafr = new Vector2d(forward, strafe);
        Vector2d deltabr = new Vector2d(forward, strafe);
        Vector2d deltabl = new Vector2d(forward, strafe);
        
        // add rotation, iadd means inplace add
        deltafl.iadd(new Vector2d(-W, L));
        deltafr.iadd(new Vector2d(W, L));
        deltabr.iadd(new Vector2d(W, -L));
        deltabl.iadd(new Vector2d(-W, -L));

        // set speeds and angles
        double speedfl = deltafl.getMagnitude();
        double speedfr = deltafr.getMagnitude();
        double speedbr = deltabr.getMagnitude();
        double speedbl = deltabl.getMagnitude();
        double anglefl = deltafl.getAngle();
        double anglefr = deltafr.getAngle();
        double anglebr = deltabr.getAngle();
        double anglebl = deltabl.getAngle();

        double maxSpeed = Math.max(Math.max(speedfr, speedfl), Math.max(speedbr, speedbl));

        if (maxSpeed >= 1) {
            speedfl /= maxSpeed;
            speedfr /= maxSpeed;
            speedbl /= maxSpeed;
            speedbr /= maxSpeed;
        }

        turnTo(frontLeft, anglefl, 0.02);
        turnTo(frontRight, anglefr, 0.02);
        turnTo(backRight, anglebr, 0.02);
        turnTo(backLeft, anglebl, 0.02);

        double maxAngleError = angleError(frontLeft, anglefl);
        maxAngleError = Math.max(angleError(frontRight, anglefr), maxAngleError);
        maxAngleError = Math.max(angleError(backRight, anglebr), maxAngleError);
        maxAngleError = Math.max(angleError(backLeft, anglebl), maxAngleError);

        if (maxAngleError < angleTolerance) {
            frontLeft.setSpeed(speedfl);
            frontRight.setSpeed(speedfr);
            backRight.setSpeed(speedbr);
            backLeft.setSpeed(speedbl);
        } else {
            frontLeft.setSpeed(0);
            frontRight.setSpeed(0);
            backRight.setSpeed(0);
            backLeft.setSpeed(0);
        }
    }

    static void turnTo(SwerveController controller, double targetAngle, double coeff) {
        int current = controller.getEncoderVal();
        int targetVal = nearestEncoderVal(controller, current, targetAngle);
        int delta = targetVal - current;
        double speed = coeff * (double) delta;
        speed = Math.copySign(Math.min(Math.abs(speed), 1), speed);
        controller.setTurn(speed);
    }

    static int nearestEncoderVal(SwerveController controller, int current, double targetAngle) {
        // stuff
        int target = controller.toEncoder(targetAngle);

        int delta = (target - current) % (controller.fullTurn/2);
        if (Math.abs(delta - controller.fullTurn/2) < delta)
            delta -= controller.fullTurn/2;

        return current + delta;
    }

    static boolean isReversed(SwerveController controller, int encoderVal, double angle) {
        int target = controller.toEncoder(angle);
        int hereDist = modDist(encoderVal, target, controller.fullTurn);
        int thereDist = modDist(encoderVal, target + controller.fullTurn/2, controller.fullTurn);

        return thereDist < hereDist;
    }

    static double angleError(SwerveController controller, double targetAngle) {
        return modDist(controller.toEncoder(targetAngle), controller.getEncoderVal(), controller.fullTurn/2);
    }

    static int modDist(int source, int target, int modulo) {
        return Math.min((target-source) % modulo, (source-target) % modulo);
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
