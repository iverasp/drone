package pilot.peripheral.sensor;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class ADS1115 implements ADC {

    ADS1115GpioProvider provider;

    public ADS1115() throws IOException, I2CFactory.UnsupportedBusNumberException {
        initialize();
    }

    @Override
    public void initialize() throws IOException, I2CFactory.UnsupportedBusNumberException {
        provider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
        provider.setMonitorInterval(100);
    }

    @Override
    public GpioProvider getProvider() {
        return provider;
    }


}
