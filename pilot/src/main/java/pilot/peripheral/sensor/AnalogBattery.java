package pilot.peripheral.sensor;

import com.pi4j.component.sensor.AnalogSensorListener;
import com.pi4j.component.sensor.impl.AnalogSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.*;

public class AnalogBattery implements Battery {

    private float CHARGE_MAX_ADC_VALUE = 3f;
    private float CHARGE_MIN_ADC_VALUE = 1f;
    private float CHARGE_CRITICAL_ADC_VALUE = 1.3f;
    private Pin ADC_PIN_BATTERY = ADS1115Pin.INPUT_A0;

    private ADS1115GpioProvider adcProvider;
    private double adcValue;

    public AnalogBattery(GpioProvider adcProvider) {
        this.adcProvider = (ADS1115GpioProvider) adcProvider;
        initialize();
    }

    @Override
    public void initialize() {
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinAnalogInput batterySensorPin = gpio.provisionAnalogInputPin(adcProvider, ADC_PIN_BATTERY, "BatterySensor");
        adcProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADC_PIN_BATTERY);
        adcProvider.setEventThreshold(150, ADC_PIN_BATTERY);
        AnalogSensorComponent batterySensor = new AnalogSensorComponent(batterySensorPin);
        batterySensor.addListener((AnalogSensorListener) event -> adcValue = event.getNewValue());
    }

    @Override
    public double getCharge() {
        return (adcValue - CHARGE_MIN_ADC_VALUE) / (CHARGE_MAX_ADC_VALUE - CHARGE_MIN_ADC_VALUE);
    }

    @Override
    public boolean isCritical() {
        return adcValue <= CHARGE_CRITICAL_ADC_VALUE;
    }

}
