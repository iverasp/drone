package peripheral.sensor;

import java.io.IOException;

public interface Altimeter {

    void initialize() throws Exception;

    void update() throws Exception;

    double getTemperatureCelsius();

    double getAltitude();

}
