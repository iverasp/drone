import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.PureJavaHidApi;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*
        HidServices services = HidManager.getHidServices();
        services.scan();
        for (HidDevice device : services.getAttachedHidDevices()) {
            System.out.println(device);
        }
        services.shutdown();
        */
        /*
        List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
        for (HidDeviceInfo info : devList) {
            System.out.printf("VID = 0x%04X PID = 0x%04X Manufacturer = %s Product = %s Path = %s\n", //
                    info.getVendorId(), //
                    info.getProductId(), //
                    info.getManufacturerString(), //
                    info.getProductString(), //
                    info.getPath());
        }
        */
        Float pi = 87.35f;
        System.out.println("Pi as float: " + pi);
        System.out.println("Pi as half float: " + toFloat(toHalfFloat(pi)));

        String val = "101010101010101";
        byte[] bval = new BigInteger(val, 2).toByteArray();
        System.out.println(Arrays.toString(bval));


    }

    public static short toHalfFloat(final float v)
    {
        if(Float.isNaN(v)) throw new UnsupportedOperationException("NaN to half conversion not supported!");
        if(v == Float.POSITIVE_INFINITY) return(short)0x7c00;
        if(v == Float.NEGATIVE_INFINITY) return(short)0xfc00;
        if(v == 0.0f) return(short)0x0000;
        if(v == -0.0f) return(short)0x8000;
        if(v > 65504.0f) return 0x7bff;  // max value supported by half float
        if(v < -65504.0f) return(short)( 0x7bff | 0x8000 );
        if(v > 0.0f && v < 5.96046E-8f) return 0x0001;
        if(v < 0.0f && v > -5.96046E-8f) return(short)0x8001;

        final int f = Float.floatToIntBits(v);

        return(short)((( f>>16 ) & 0x8000 ) | (((( f & 0x7f800000 ) - 0x38000000 )>>13 ) & 0x7c00 ) | (( f>>13 ) & 0x03ff ));
    }

    public static float toFloat(final short half)
    {
        switch((int)half)
        {
            case 0x0000 :
                return 0.0f;
            case 0x8000 :
                return -0.0f;
            case 0x7c00 :
                return Float.POSITIVE_INFINITY;
            case 0xfc00 :
                return Float.NEGATIVE_INFINITY;
            // @TODO: support for NaN ?
            default :
                return Float.intBitsToFloat((( half & 0x8000 )<<16 ) | ((( half & 0x7c00 ) + 0x1C000 )<<13 ) | (( half & 0x03FF )<<13 ));
        }
    }
}
