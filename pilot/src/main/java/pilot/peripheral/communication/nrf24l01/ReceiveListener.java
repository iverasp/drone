package pilot.peripheral.communication.nrf24l01;

public interface ReceiveListener {

    /**
     * @param data data bytes arrived
     */
    void dataReceived(int[] data);

}
