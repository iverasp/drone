package peripheral.sensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class MPL3115A2 implements Altimeter {

    public static final int I2C_ADDRESS = 0x60;

    private I2CBus bus;
    private I2CDevice device;
    private double temperatureCelsius;
    private double altitude;

    public MPL3115A2() throws IOException, I2CFactory.UnsupportedBusNumberException {
        initialize();
    }

    @Override
    public void initialize() throws IOException, I2CFactory.UnsupportedBusNumberException {
        bus = I2CFactory.getInstance(I2CBus.BUS_1);
        device = bus.getDevice(I2C_ADDRESS);
        // Select control register
        // Active mode, OSR = 128, altimeter mode
        device.write(0x26, (byte)0xB9);
        // Select data configuration register
        // Data ready event enabled for altitude, pressure, temperature
        device.write(0x13, (byte)0x07);

        // Select control register
        // Active mode, OSR = 128, altimeter mode
        device.write(0x26, (byte)0xB9);
    }

    @Override
    public void update() throws IOException {
        byte[] data = new byte[6];
        device.read(0x00, data, 0, 6);

        // Convert the data to 20-bits
        int tHeight = ((((data[1] & 0xFF) * 65536) + ((data[2] & 0xFF) * 256) + (data[3] & 0xF0)) / 16);
        int temp = ((data[4] * 256) + (data[5] & 0xF0)) / 16;
        altitude = tHeight / 16.0;
        temperatureCelsius = (temp / 16.0);
    }

    @Override
    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }
}
