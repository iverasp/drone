package pilot.peripheral.communication;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

public class NRF24L01 {

    // SPI commands
    public static final short CMD_READ_REG = 0x00;
    public static final short CMD_WRITE_REG = 0x20;
    public static final short CMD_NO_OPERATION = 0xFF;

    // registers
    public static final short REG_CONFIG = 0x00;
    public static final short REG_EN_AA = 0x01;
    public static final short REG_EN_RXADDR = 0x02;
    public static final short REG_SETUP_AW = 0x03;
    public static final short REG_SETUP_RETR = 0x04;
    public static final short REG_RF_CH = 0x05;
    public static final short REG_RF_SETUP = 0x06;
    public static final short REG_STATUS = 0x07;
    public static final short REG_OBSERVE_TX = 0x08;
    public static final short REG_RPD = 0x09;
    public static final short REG_RX_ADDR_P0 = 0x0a;
    public static final short REG_RX_ADDR_P1 = 0x0b;
    public static final short REG_RX_ADDR_P2 = 0x0c;
    public static final short REG_RX_ADDR_P3 = 0x0d;
    public static final short REG_RX_ADDR_P4 = 0x0e;
    public static final short REG_RX_ADDR_P5 = 0x0f;
    public static final short REG_TX_ADDR = 0x10;
    public static final short REG_RX_PW_P0 = 0x11;
    public static final short REG_RX_PW_P1 = 0x12;
    public static final short REG_RX_PW_P2 = 0x13;
    public static final short REG_RX_PW_P3 = 0x14;
    public static final short REG_RX_PW_P4 = 0x15;
    public static final short REG_RX_PW_P5 = 0x16;
    public static final short REG_FIFO_STATUS = 0x17;
    public static final short REG_DYNPD = 0x1c;
    public static final short REG_FEATURE = 0x1d;

    // config register
    public static final short CONFIG_PRIM_RX = (1 << 0);
    public static final short CONFIG_PWR_UP = (1 << 1);
    public static final short CONFIG_CRC0 = (1 << 2);
    public static final short CONFIG_EN_CRC = (1 << 3);
    public static final short CONFIG_MASK_MAX_RT = (1 << 4);
    public static final short CONFIG_MASK_TX_DS = (1 << 5);
    public static final short CONFIG_MASK_RX_DR = (1 << 6);

    // RF setup register
    public static final short RF_PWR_MASK = (0x3 << 1);
    public static final short RF_PWR_0DBM = (0x3 << 1);
    public static final short RF_PWR_MINUS_6DBM = (0x2 << 1);
    public static final short RF_PWR_MINUS_12DBM = (0x1 << 1);
    public static final short RF_PWR_MINUS_18DBM = (0x0 << 1);
    public static final short RF_DR_HIGH_BIT = (1 << 3);
    public static final short RF_DR_LOW_BIT = (1 << 5);
    public static final short RF_DR_MASK = RF_DR_LOW_BIT | RF_DR_HIGH_BIT;
    public static final short RF_DR_250KBPS = RF_DR_LOW_BIT;
    public static final short RF_DR_1MBPS = 0;
    public static final short RF_DR_2MBPS = RF_DR_HIGH_BIT;

    // status register
    public static final short STATUS_TX_FULL = (1 << 0);
    public static final short STATUS_RX_P_NO = (0x7 << 1);
    public static final short STATUS_MAX_RT = (1 << 4);
    public static final short STATUS_TX_DS = (1 << 5);
    public static final short STATUS_RX_DR = (1 << 6);

    private enum Mode {
        UNKNOWN,
        POWER_DOWN,
        STANDBY,
        RX,
        TX
    }

    public enum DataRate {
        RATE_1MBPS,
        RATE_2MBPS,
        RATE_250KBPS,
        RATE_UNKNOWN
    }

    private Mode currentMode = Mode.UNKNOWN;

    private Pin PIN_CSN = RaspiPin.GPIO_03;
    private Pin PIN_CE = RaspiPin.GPIO_04;
    final GpioPinDigitalOutput csn = GpioFactory.getInstance().provisionDigitalOutputPin(PIN_CSN, PinState.HIGH);
    final GpioPinDigitalOutput ce = GpioFactory.getInstance().provisionDigitalOutputPin(PIN_CE, PinState.LOW);

    private static SpiDevice spi = null;
    private short MAX_PAYLOAD_SIZE = 32;

    public NRF24L01() throws IOException {
        initialize();
    }

    public void initialize() throws IOException {
        spi = SpiFactory.getInstance(SpiChannel.CS0);
        ce.setState(false);
        csn.setState(true);
        short config = 0x00;
        writeRegister(REG_CONFIG, config);
        writeRegister(REG_STATUS, (short)(STATUS_MAX_RT | STATUS_TX_DS | STATUS_RX_DR));
        writeRegister(REG_EN_AA, (short)0);
        setChannel(100);
        writeRegister(REG_RF_SETUP, (short)0x0E);
        short pipe = 0;
        short rxPwPxRegister = (short) (0x11 + pipe);
        writeRegister(rxPwPxRegister, (short)(MAX_PAYLOAD_SIZE & 0x3F));
        writeRegister((short) 0x04, (short)0);
        config |= (1 << 0);
        config |= (1 << 1);
        writeRegister(REG_CONFIG, config);
        ce.setState(true);
        System.out.println("Channel set to " + readRegister(REG_RF_CH));
        System.out.println("Config register is " + readRegister(REG_CONFIG));
        currentMode = Mode.RX;
    }

    public void powerUp() {
        short config = readRegister(REG_CONFIG);
        config |= CONFIG_PWR_UP;
        writeRegister(REG_CONFIG, config);
        currentMode = Mode.STANDBY;
    }

    public void setReceiveMode() {
        if (currentMode == Mode.POWER_DOWN) powerUp();
        short config = readRegister(REG_CONFIG);
        config |= CONFIG_PRIM_RX;
        if (writeRegister(REG_CONFIG, config)) currentMode = Mode.RX;
    }

    public void setTransmitMode() {
        if (currentMode == Mode.POWER_DOWN) powerUp();
        short config = readRegister(REG_CONFIG);
        config &= ~CONFIG_PRIM_RX;
        if (writeRegister(REG_CONFIG, config)) currentMode = Mode.TX;
    }

    public void setDataRate(DataRate dataRate) {
        short rfSetup = (short) (readRegister(REG_RF_SETUP) &~ RF_DR_MASK);
        switch (dataRate) {
            case RATE_250KBPS:
                rfSetup |= RF_DR_250KBPS;
                break;
            case RATE_1MBPS:
                rfSetup |= RF_DR_1MBPS;
                break;
            case RATE_2MBPS:
                rfSetup |= RF_DR_2MBPS;
                break;
            default:
                rfSetup |= RF_DR_1MBPS;
                break;
        }
        writeRegister(REG_RF_SETUP, rfSetup);
    }

    public DataRate getDataRate() {
        short dataRate = (short)(readRegister(REG_RF_SETUP) & RF_DR_MASK);
        switch (dataRate) {
            case RF_DR_250KBPS:
                return DataRate.RATE_250KBPS;
            case RF_DR_1MBPS:
                return DataRate.RATE_1MBPS;
            case RF_DR_2MBPS:
                return DataRate.RATE_2MBPS;
            default:
                return DataRate.RATE_UNKNOWN;
        }
    }

    public short[] readPayload() {
        csn.setState(false);
        short[] result = new short[MAX_PAYLOAD_SIZE];
        try {
            spi.write((short)0x61);
            for (int i = 0; i < MAX_PAYLOAD_SIZE; i++) {
                result[i] = spi.write(CMD_NO_OPERATION)[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        csn.setState(true);
        writeRegister((short)0x07, (short)(1 << 6));
        return result;
    }

    public void sendPayload(short[] payload) {
        ce.setState(false);
        writeRegister((short)0x07, (short)(1 << 5));
        csn.setState(false);
        try {
            spi.write((short)0xa0);
            for (int i = 0; i < payload.length; i++) {
                spi.write(payload[i]);
            }
            csn.setState(true);
            short config = readRegister((short)0x00);
            config &= ~(1 << 0);
            writeRegister((short)0x00, config);
            ce.setState(true);
            Thread.sleep(10);
            ce.setState(false);

            while (true) {
                short status = readRegister((short)0x07);
                System.out.println("Status " + status);
                if ((status & (1 << 5)) != 0) break;
                System.out.println("waiting for transfer to complete");
            }

            writeRegister((short)0x07, (short)(1 << 5));
            Thread.sleep(10);
            ce.setState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private short readRegister(short register) {
        try {
            csn.setState(false);
            spi.write((short) (CMD_READ_REG | register));
            short[] result = spi.write(CMD_NO_OPERATION);
            csn.setState(true);
            return result[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean writeRegister(short register, short data) {
        try {
            csn.setState(false);
            spi.write((short) (CMD_WRITE_REG | register));
            spi.write(data);
            csn.setState(true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean setChannel(int channel) {
        return writeRegister(REG_RF_CH, (short) channel);
    }
}