package pilot;

import com.pi4j.gpio.extension.ads.ADS1015GpioProvider;
import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import pilot.peripheral.sensor.Altimeter;
import pilot.peripheral.sensor.Distance;
import pilot.peripheral.sensor.GP2Y0A21YK;
import pilot.peripheral.sensor.MPL3115A2;
import pilot.peripheral.sensor.mpu.MPU9250;
import pilot.peripheral.sensor.mpu.MpuData;
import pilot.peripheral.sensor.mpu.Quaternion;
import pilot.peripheral.sensor.mpu.Vec3d;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Altimeter altimeter = null;
        Distance dist = null;
        try {
            altimeter = new MPL3115A2();
            altimeter.update();
            System.out.println("Altitude: " + altimeter.getAltitude());
            dist = new GP2Y0A21YK();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPU9250 mpu = new MPU9250();
        mpu.init(I2CBus.BUS_1, (short)0x68, 200, 1, true);
        MpuData data = new MpuData();
        while(true) {
            mpu.read(data);
            mpu.readDMP(data);
            Vec3d vec = data.fusedEuler;
            System.out.println(vec);

            Quaternion quat = data.fusedQuat;
            byte[] w = float2ByteArray((float)quat.getW());
            byte[] x = float2ByteArray((float)quat.getX());
            byte[] y = float2ByteArray((float)quat.getY());
            byte[] z = float2ByteArray((float)quat.getZ());

            short[] payload = new short[32];
            payload[0] = (short)w[0];
            payload[1] = (short)w[1];
            payload[2] = (short)w[2];
            payload[3] = (short)w[3];
            payload[4] = (short)x[0];
            payload[5] = (short)x[1];
            payload[6] = (short)x[2];
            payload[7] = (short)x[3];
            payload[8] = (short)y[0];
            payload[9] = (short)y[1];
            payload[10] = (short)y[2];
            payload[11] = (short)y[3];
            payload[12] = (short)z[0];
            payload[13] = (short)z[1];
            payload[14] = (short)z[2];
            payload[15] = (short)z[3];
            if (altimeter != null) {
                altimeter.update();
            }
        }

    }

    public static byte [] float2ByteArray (float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }
}
