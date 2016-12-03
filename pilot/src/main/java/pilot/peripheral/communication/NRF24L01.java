package pilot.peripheral.communication;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.util.CommandArgumentParser;

import java.io.IOException;

public class NRF24L01 implements Radio {

    private Pin PIN_INTERRUPT = RaspiPin.GPIO_00;
    private static SpiDevice spi = null;

    public NRF24L01() throws IOException {
        initialize();
    }

    @Override
    public void initialize() throws IOException {
        spi = SpiFactory.getInstance(SpiChannel.CS0);
        final GpioController gpio = GpioFactory.getInstance();
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,
                PIN_INTERRUPT
        );
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP
        );
        final GpioPinDigitalInput interrupt = gpio.provisionDigitalInputPin(pin, pull);
        interrupt.setShutdownOptions(true);
        interrupt.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().isLow()) {
                readBuffer();
            }
        });
    }

    @Override
    public void readBuffer() {

    }

    @Override
    public float getYaw() {
        return 0;
    }

    @Override
    public float getPitch() {
        return 0;
    }

    @Override
    public float getRoll() {
        return 0;
    }

    @Override
    public float getThrottle() {
        return 0;
    }
}
