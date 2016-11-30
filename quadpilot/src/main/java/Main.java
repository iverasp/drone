import com.pi4j.io.i2c.I2CFactory;
import core.QuadController;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        QuadController controller = null;
        try {
            controller = new QuadController();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        }
        while(true) {
            controller.update();
        }
    }
}
