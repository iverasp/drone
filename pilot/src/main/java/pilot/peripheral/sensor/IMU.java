package pilot.peripheral.sensor;

public interface IMU {

    void initialize() throws Exception;

    float getYaw();

    float getPitch();

    float getRoll();
}
