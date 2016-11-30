package peripheral.communication;

public interface Radio {

    void initialize() throws Exception;

    void readBuffer();

    float getYaw();

    float getPitch();

    float getRoll();

    float getThrottle();

}
