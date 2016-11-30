package peripheral.sensor;

public interface IMU {

    void initialize();

    float getYaw();

    float getPitch();

    float getRoll();
}
