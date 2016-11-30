package peripheral.sensor;

public interface Battery {

    void initialize() throws Exception;

    double getCharge();

    boolean isCritical();
}
