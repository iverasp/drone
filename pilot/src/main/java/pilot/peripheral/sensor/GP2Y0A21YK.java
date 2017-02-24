package pilot.peripheral.sensor;

import com.pi4j.component.sensor.DistanceSensorChangeEvent;
import com.pi4j.component.sensor.impl.DistanceSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1015Pin;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class GP2Y0A21YK implements Distance {

    private Pin ADC_PIN_DISTANCE = ADS1015Pin.INPUT_A1;
    private ADS1015GpioProvider adcProvider;
    private double distance;

    public GP2Y0A21YK() {
        try {
            adcProvider = new ADS1015GpioProvider(1, 0x48);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize();
    }

    @Override
    public void initialize() {
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinAnalogInput distanceSensorPin = gpio.provisionAnalogInputPin(adcProvider, ADC_PIN_DISTANCE, "DistanceSensor");
        adcProvider.setProgrammableGainAmplifier(ADS1015GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADC_PIN_DISTANCE);
        adcProvider.setEventThreshold(150, ADC_PIN_DISTANCE);

        DistanceSensorComponent distanceSensor = new DistanceSensorComponent(distanceSensorPin);
        distanceSensor.addCalibrationCoordinate(21600, 13);
        distanceSensor.addCalibrationCoordinate(21500, 14);
        distanceSensor.addCalibrationCoordinate(21400, 15);
        distanceSensor.addCalibrationCoordinate(21200, 16);
        distanceSensor.addCalibrationCoordinate(21050, 17);
        distanceSensor.addCalibrationCoordinate(20900, 18);
        distanceSensor.addCalibrationCoordinate(20500, 19);
        distanceSensor.addCalibrationCoordinate(20000, 20);
        distanceSensor.addCalibrationCoordinate(15000, 30);
        distanceSensor.addCalibrationCoordinate(12000, 40);
        distanceSensor.addCalibrationCoordinate(9200,  50);
        distanceSensor.addCalibrationCoordinate(8200,  60);
        distanceSensor.addCalibrationCoordinate(6200,  70);
        distanceSensor.addCalibrationCoordinate(4200,  80);

        distanceSensor.addListener((DistanceSensorChangeEvent event) -> {
            this.distance = event.getDistance();
        });
    }

    @Override
    public double getDistance() {
        return distance;
    }
}
