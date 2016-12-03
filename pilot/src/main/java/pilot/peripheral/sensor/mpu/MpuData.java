package pilot.peripheral.sensor.mpu;

public class MpuData {
    public final short[] rawGyro = new short[3];
    public final short[] rawAccel = new short[3];
    public final int[] rawQuat = new int[4];
    public final long[] dmpTimestamp = new long[1];

    public final short[] rawMag = new short[3];
    public final long[] magTimestamp = new long[1];

    public final short[] calibratedAccel = new short[3];
    public final short[] calibratedMag = new short[3];

    public final Quaternion fusedQuat = new Quaternion();
    public final Quaternion unfusedQuat = new Quaternion();
    public final Quaternion magQuat = new Quaternion();
    public final Vec3d fusedEuler = new Vec3d();

    float lastDMPYaw;
    float lastYaw;
}