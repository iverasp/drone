package peripheral.controller;

public interface PWMController {

    void initialize() throws Exception;

    void setChannelFrequency(int channel, int frequency);

}
