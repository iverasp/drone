package pilot;

import com.pi4j.io.i2c.I2CBus;
import pilot.peripheral.sensor.mpu.MPU9250;
import pilot.peripheral.sensor.mpu.MpuData;
import pilot.peripheral.sensor.mpu.Vec3d;

public class Main {

    public static void main(String[] args) {
        MPU9250 mpu = new MPU9250();
        mpu.init(I2CBus.BUS_1, (short)0x68, 200, 1, true);
        MpuData data = new MpuData();
        while(true) {
            mpu.read(data);
            mpu.readDMP(data);
            Vec3d vec = data.fusedEuler;
            System.out.println(vec);
        }
    }
}
