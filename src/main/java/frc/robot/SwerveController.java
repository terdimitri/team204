package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;


class SwerveController {
    private SpeedController driveMotor;
    private SpeedController turningMotor;
    private Encoder encoder;
    int fullTurn;
    String name;

    /* A nice little untilty class to control your favorite swerve drive wheel
     * fullTurn should be the value of the encoder if the wheel makes a full 360
     * degree turn.
     */
    public SwerveController(SpeedController driveMotor,
                            SpeedController turningMotor,
                            Encoder encoder,
                            int fullTurn,
                            String name) {


        this.driveMotor = driveMotor;
        this.turningMotor = turningMotor;
        this.encoder = encoder;
        this.fullTurn = fullTurn;
        this.name = name;
    }

    void setSpeed(double speed) {
        driveMotor.set(speed);
    }

    void setTurn(double speed) {
        turningMotor.set(speed);
    }

    int getEncoderVal() {
        return encoder.get();
    }
    // void setTurnTo(double speed, int target) {
        // 
    // }

    int toEncoder(double angle) {
        return (int) (angle/Math.PI/2*fullTurn);
    }

    double toAngle(int encoderVal) {
        return ((double) encoderVal) /fullTurn*Math.PI*2;
    }

    // void setTurnTo(double speed, int target) {
    //     speed = Math.abs(speed);
    //     setTurn(speed * towards(encoder.get(), target, false));
    // }

    // void setTurnTo(double speed, double angle) {
    //     setTurnTo(speed, nearestTurn(angle));
    // }

    // static double towards(int source, int target, boolean inverted) {
    //     if (source == target) return 0.0;
    //     boolean out;
    //     if (source < target) out = true;
    //     else out = false;
    //     if (inverted) out = !out;

    //     if (out) return 1.0;
    //     return -1.0;
    // }

    void stopMotor() {
        driveMotor.set(0.0);
        driveMotor.set(0.0);
    }

    void disable() {
        driveMotor.disable();
        turningMotor.disable();
    }

    // /* Utility function, return the would-be value of the encoder if it turned to
    //  * the target angle (in radians) minimizing the turning angle.
    //  */
    // int nearestTurn(double theta) {
    //     int current = encoder.get();
    //     int target = (int) (theta / 2 / Math.PI * fullTurn);
    //     int delta = (target - current) % fullTurn;
    //     if (fullTurn - delta < delta) delta -= fullTurn;
    //     return current + delta;
    // }
    
}