package core;

import com.pi4j.io.i2c.I2CFactory;
import peripheral.communication.NRF24L01;
import peripheral.communication.Radio;
import peripheral.motor.Motor;
import peripheral.motor.MotorPlacement;
import peripheral.controller.PCA9685;
import peripheral.controller.PWMController;
import peripheral.sensor.*;

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

    public QuadController() throws IOException, I2CFactory.UnsupportedBusNumberException {
        motors = new HashMap<>();
        pwmController = new PCA9685();
        setupMotors();
        setupSensors();
        setupCommunication();
        setupPID();
    }

    private void setupMotors() {
        motors.put(MotorPlacement.NORTHEAST, new Motor(pwmController, 0));
        motors.put(MotorPlacement.SOUTHEAST, new Motor(pwmController, 1));
        motors.put(MotorPlacement.SOUTHWEST, new Motor(pwmController, 2));
        motors.put(MotorPlacement.NORTHWEST, new Motor(pwmController, 3));
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
        pid = new PID();
    }

    public void update() throws Exception {
        altimeter.update();
        battery.getCharge();
        distance.getDistance();
    }
}
