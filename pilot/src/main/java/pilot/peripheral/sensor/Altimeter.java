package pilot.peripheral.sensor;

public interface Altimeter {

    void initialize() throws Exception;

    void update() throws Exception;

    double getTemperatureCelsius();

    double getAltitude();

}
