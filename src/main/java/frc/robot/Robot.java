package frc.robot;

// Team 204 robot 2020 is 20 * 25.5

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Swerve drive objects
  private static final Encoder e_front_left  = new Encoder(6, 7);
  private static final Encoder e_front_right = new Encoder(4, 5);
  private static final Encoder e_back_left   = new Encoder(2, 3);
  private static final Encoder e_back_right  = new Encoder(0, 1);

  private static final WPI_TalonSRX m_front_left  = new WPI_TalonSRX(24);
  private static final WPI_TalonSRX m_front_right = new WPI_TalonSRX(14);
  private static final WPI_TalonSRX m_back_left   = new WPI_TalonSRX(22);
  private static final WPI_TalonSRX m_back_right  = new WPI_TalonSRX(12);

  private static final WPI_TalonSRX t_front_left  = new WPI_TalonSRX(23);
  private static final WPI_TalonSRX t_front_right = new WPI_TalonSRX(13);
  private static final WPI_TalonSRX t_back_left   = new WPI_TalonSRX(21);
  private static final WPI_TalonSRX t_back_right  = new WPI_TalonSRX(11);


  // Control
  private static final XboxController controller = new XboxController(0);

  private static final SwerveDrive drive = new SwerveDrive(
      new SwerveController(m_front_left, t_front_left, e_front_left, 420, "frontLeft"),
      new SwerveController(m_front_right, t_front_right, e_front_right, 420, "frontRight"),
      new SwerveController(m_back_left, t_back_left, e_back_left, 420,  "backLeft"),
      new SwerveController(m_back_right, t_back_right, e_back_right, 420, "backRight"),
      20, 25.5);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double forward = -controller.getY(Hand.kLeft);
    double strafe = controller.getX(Hand.kLeft);
    double rotate = controller.getX(Hand.kRight);

    if (Math.abs(forward) < 0.15) forward = 0.0;
    if (Math.abs(strafe) < 0.15) strafe = 0.0;
    if (Math.abs(rotate) < 0.15) rotate = 0.0;

    SmartDashboard.putString("forward", "" + forward);
    SmartDashboard.putString("strafe", "" + strafe);
    SmartDashboard.putString("rotate", "" + rotate);

    
    drive.set(-forward, strafe, rotate);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
