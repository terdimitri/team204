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

    int toEncoder(double angle) {
        return (int) (angle/Math.PI/2*fullTurn);
    }

    double toAngle(int encoderVal) {
        return ((double) encoderVal) /fullTurn*Math.PI*2;
    }

    void stopMotor() {
        driveMotor.set(0.0);
        driveMotor.set(0.0);
    }

    void disable() {
        driveMotor.disable();
        turningMotor.disable();
    }

    
}