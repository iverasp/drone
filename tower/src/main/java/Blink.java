import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Blink implements HidServicesListener {

    HidServices hidServices = HidManager.getHidServices();

    byte ledState = 0;

    public Blink() {
        hidServices.addHidServicesListener(this);
    }

    public void printDevices() {
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            System.out.println(hidDevice);
        }
    }

    public void printData() {
        HidDevice stm = hidServices.getHidDevice(0x0483, 0x5710, "DEMO");
        while(true) {
            byte[] msg = new byte[32];
            stm.read(msg);
            for (int i = 0; i < 32; i++) {
                System.out.print((char) (msg[i] & 0xFF));
            }
            System.out.println("\n");
        }
    }

    public void toggleLed() {
        HidDevice stm = hidServices.getHidDevice(0x1234, 0x0001, null);
        byte[] msg = new byte[8];
        long time = System.currentTimeMillis();
        long delay = 1000;
        while(true) {
            if (System.currentTimeMillis() > time + delay) {
                time = System.currentTimeMillis();
                System.out.println("setting led to " + ledState);
                for (int i = 0; i < 8; i++) {
                    msg[i] = (byte)ThreadLocalRandom.current().nextInt(0, 2);
                }
                if (ledState == 0) ledState = 1;
                else ledState = 0;
                stm.write(msg, 8, (byte)0);
            }
        }

    }

    public void hidDeviceAttached(HidServicesEvent hidServicesEvent) {

    }

    public void hidDeviceDetached(HidServicesEvent hidServicesEvent) {

    }

    public void hidFailure(HidServicesEvent hidServicesEvent) {

    }
}
