// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.DriveTrain;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystem defined here
    private final DriveTrain m_driveTrain = new DriveTrain();

    // controllers
    private final CommandXboxController m_driverController = new CommandXboxController(
            OperatorConstants.kDriverControllerPort);
    private final CommandXboxController m_coDriverController = new CommandXboxController(
            OperatorConstants.kCoDriverControllerPort);

    private SendableChooser<Command> m_autoChooser;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        configureBindings();

        // lock drive until throttle is zero
        // Command driveLockCommand = new WaitUntilCommand(() -> m_driverController.getThrottle() == 0)
        //         .withInterruptBehavior(InterruptionBehavior.kCancelIncoming);
        // driveLockCommand.addRequirements(m_driveTrain);

        new Trigger(DriverStation::isEnabled)
                .onTrue(m_driveTrain.runOnce(() -> m_driveTrain.setIdleMode(IdleMode.kBrake)));
                // .onTrue(driveLockCommand);

        RobotModeTriggers.disabled()
                .onTrue(m_driveTrain.runOnce(() -> m_driveTrain.setIdleMode(IdleMode.kCoast))
                        .ignoringDisable(true));

        // m_autoChooser = AutoBuilder.buildAutoChooser();

        // Shuffleboard.getTab("Autonomous").add("Auto", m_autoChooser);
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be
     * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
     * constructor with an arbitrary predicate, or via the named factories in
     * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
     * for {@link CommandXboxController
     * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
     * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick
     * Flight joysticks}.
     */
    private void configureBindings() {
        m_driveTrain.setDefaultCommand(
                m_driveTrain.driveJoysticks(m_driverController::getLeftY,
                        m_driverController::getRightX,
                        m_driverController.getHID()::getLeftBumperButton));
    }

    /**
     * Gets the {@link Command} to run in autonomous.
     */
    public Command getAutonomousCommand() {
        return m_autoChooser.getSelected();
    }
}
