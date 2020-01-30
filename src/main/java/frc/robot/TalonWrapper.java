package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public class TalonWrapper implements SpeedController {

    TalonSRX talon;
    boolean isInverted;
    boolean enabled = true;

    @Override
    public void pidWrite(double output) {
        set(output);
    }

    @Override
    public void set(double speed) {
        if (enabled) {
            if (isInverted)
                talon.set(ControlMode.PercentOutput, -speed);
            else
                talon.set(ControlMode.PercentOutput, speed);
        }
    }

    @Override
    public double get() {
        return talon.getMotorOutputPercent();
    }

    @Override
    public void setInverted(boolean isInverted) {
        this.isInverted = isInverted;
    }

    @Override
    public boolean getInverted() {
        return isInverted;
    }

    @Override
    public void disable() {
        set(0);
        enabled = false;
    }

    @Override
    public void stopMotor() {
        set(0);
    }
}