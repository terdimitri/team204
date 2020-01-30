package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;


class SwerveController {
    private SpeedController driveMotor;
    private SpeedController turningMotor;
    private Encoder encoder;
    private int fullTurn;

    /* A nice little untilty class to control your favorite swerve drive wheel
     * fullTurn should be the value of the encoder if the wheel makes a full 360
     * degree turn.
     */
    public SwerveController(SpeedController driveMotor,
                            SpeedController turningMotor,
                            Encoder encoder,
                            int fullTurn) {

        this.driveMotor = driveMotor;
        this.turningMotor = turningMotor;
        this.encoder = encoder;
        this.fullTurn = fullTurn;
    }

    void setSpeed(double speed) {
        driveMotor.set(speed);
    }

    void setTurn(double speed) {
        turningMotor.set(speed);
    }

    void stopMotor() {
        driveMotor.set(0.0);
        driveMotor.set(0.0);
    }

    void disable() {
        driveMotor.disable();
        turningMotor.disable();
    }

    /* Utility function, return the would-be value of the encoder if it turned to
     * the target angle (in radians) minimizing the turning angle.
     */
    int nearestTurn(double theta) {
        int current = encoder.get();
        int target = (int) (theta / 2 / Math.PI * fullTurn);
        int delta = (target - current) % fullTurn;
        if (fullTurn - delta < delta) delta -= fullTurn;
        return current + delta;
    }
    
}