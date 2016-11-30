package peripheral.sensor;

import com.pi4j.io.gpio.GpioProvider;

public interface ADC {

    void initialize() throws Exception;

    GpioProvider getProvider();
}
