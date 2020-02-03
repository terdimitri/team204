package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.drive.Vector2d;


public class SwerveDrive extends RobotDriveBase {

    final double angleTolerance = Math.PI/8;

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

        // assert good inputs
        assert -1.0 <= forward && forward <= 1 : "`forward` value is out of bounds";
        assert -1.0 <= strafe && strafe <= 1 : "`strafe` value is out of bounds";
        assert -1.0 <= forward && forward <= 1 : "`forward` value is out of bounds";

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
        deltafl.iadd(Vector2d(-L, W));
        deltafr.iadd(Vector2d(L, W));
        deltabr.iadd(Vector2d(L, -W));
        deltabl.iadd(Vector2d(-L, -W));

        // set speeds and angles
        speedfl = deltafl.getMagnitude();
        speedfr = deltafr.getMagnitude();
        speedbr = deltabr.getMagnitude();
        speedbl = deltabl.getMagnitude();
        anglefl = deltafl.getAngle();
        anglefr = deltafr.getAngle();
        anglebr = deltabr.getAngle();
        anglebl = deltabl.getAngle();

        double maxSpeed = Math.max(Math.max(speedfr, speedfl), Math.max(speedbr, speedbl));

        if (maxSpeed > 1) {
            speedfl /= maxSpeed;
            speedfr /= maxSpeed;
            speedbl /= maxSpeed;
            speedbr /= maxSpeed;
        }


        frontLeft.setSpeed(speedfl);
        frontRight.setSpeed(speedfr);
        backRight.setSpeed(speedbl);
        backLeft.setSpeed(speedbr);

        frontLeft.setTurnTo(1.0, anglefl);
        frontRight.setTurnTo(1.0, anglefr);
        backRight.setTurnTo(1.0, anglebl);
        backLeft.setTurnTo(1.0, anglebr);
    }


    static int nearestEncoderVal(SwerveController controller, double targetAngle) {
        int current = controller.getEncoderVal();
        target = controller.toEncoder(targetAngle);

        int delta = (target - current) % (fullTurn/2)
        if (Math.abs(delta - fullTurn/2) < delta)
            delta -= fullTurn/2;

        return current + delta;
    }

    static boolean isReversed(SwerveController controller, int encoderVal, double angle) {
        int target = controller.toEncoder(double angle);
        int hereDist = modDist(encoderVal, target, fullTurn);
        int thereDist = modDist(encoderVal, target + fullTurn/2, fullTurn);

        return thereDist < hereDist;
    }

    static int modDist(int source, int target, int modulo) {
        return Math.min((target-source) % modulo, (source-target) % modulo);
    }

    double angleDelta() {
        return 0.0;
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
