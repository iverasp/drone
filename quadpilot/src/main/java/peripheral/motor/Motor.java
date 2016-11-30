package peripheral.motor;

import peripheral.controller.PWMController;

public class Motor {

    private PWMController pwmController;
    private int pwmChannel;
    private int speed;

    public Motor(PWMController pwmController, int pwmChannel) {
        this.pwmController = pwmController;
        this.pwmChannel = pwmChannel;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        pwmController.setChannelFrequency(pwmChannel, speed);
    }

}
