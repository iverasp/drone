package pilot.core;

import com.pi4j.io.i2c.I2CFactory;
import pilot.peripheral.communication.NRF24L01;
import pilot.peripheral.communication.Radio;
import pilot.peripheral.motor.Motor;
import pilot.peripheral.motor.MotorPlacement;
import pilot.peripheral.controller.PCA9685;
import pilot.peripheral.controller.PWMController;
import pilot.peripheral.sensor.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuadController {

    private PWMController pwmController;
    private Map<MotorPlacement, Motor> motors;
    private Altimeter altimeter;
    private IMU imu;
    private Radio radio;
    private PID pid;
    private ADC adc;
    private Battery battery;
    private Distance distance;
    private PIDParameter pidParameter;

    public QuadController() throws IOException, I2CFactory.UnsupportedBusNumberException {
        motors = new HashMap<>();
        pwmController = new PCA9685();
        setupPID();
        setupMotors();
        setupSensors();
        setupCommunication();
    }

    private void setupMotors() {
        motors.put(MotorPlacement.DEGREES_45, new Motor(pwmController, 0));
        motors.put(MotorPlacement.DEGREES_225, new Motor(pwmController, 1));
        motors.put(MotorPlacement.DEGREES_135, new Motor(pwmController, 2));
        motors.put(MotorPlacement.DEGREES_315, new Motor(pwmController, 3));
    }

    private void setupSensors() throws IOException, I2CFactory.UnsupportedBusNumberException {
        imu = new MPU9250();
        altimeter = new MPL3115A2();
        adc = new ADS1115();
        battery = new AnalogBattery(adc.getProvider());
        distance = new GP2Y0A21YK(adc.getProvider());
    }

    private void setupCommunication() throws IOException {
        radio = new NRF24L01();
    }

    private void setupPID() {
        pidParameter = PIDParameter.getInstance();
        pidParameter.setP(1);
        pidParameter.setI(2);
        pidParameter.setD(3);
    }

    public void update() throws Exception {
        altimeter.update();
        double altitude = altimeter.getAltitude();
        double temperatureCelsius = altimeter.getTemperatureCelsius();
        double charge = battery.getCharge();
        double groundDistance = distance.getDistance();
    }
}
