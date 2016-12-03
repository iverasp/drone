package pilot.peripheral.sensor.mpu;

import java.io.*;

public class CalData {

    public final long[] offset = new long[3];
    public final short[] range = new short[3];

    public CalData() {}

    public CalData(short[] val) {
        offset[0] = (short)((val[0] + val[1]) / 2);
        offset[1] = (short)((val[2] + val[3]) / 2);
        offset[2] = (short)((val[4] + val[5]) / 2);
        range[0] = (short)(val[1] - offset[0]);
        range[1] = (short)(val[3] - offset[1]);
        range[2] = (short)(val[5] - offset[2]);
    }

    public CalData(short[] minVal, short[] maxVal) {
        offset[0] = (short)((minVal[0] + maxVal[0]) / 2);
        offset[1] = (short)((minVal[1] + maxVal[1]) / 2);
        offset[2] = (short)((minVal[2] + maxVal[2]) / 2);
        range[0] = (short)(maxVal[0] - offset[0]);
        range[1] = (short)(maxVal[1] - offset[1]);
        range[2] = (short)(maxVal[2] - offset[2]);
    }

    public boolean readFromFile(String calFile) {
        try {
            BufferedReader input;
            try {
                input = new BufferedReader(new FileReader(calFile));
            } catch (FileNotFoundException ex) {
                System.out.println("Unable to open calibration file " + calFile);
                return false;
            }
            try {
                String line = null;
                int i = 0;
                while ((line = input.readLine()) != null) {
                    if (i < 3) {
                        offset[i] = Short.parseShort(line);
                    } else if (i < 6) {
                        range[i-3] = Short.parseShort(line);
                    } else {
                        break;
                    }
                    i++;
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            System.out.println("Failed to read from calibration file " + calFile);
            return false;
        }
        return true;
    }

    public boolean writeToFile(String calFile) {
        File file = new File(calFile);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.out.println("Unable to create calibration file " + calFile);
            return false;
        }

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            try {
                for (int i = 0; i < 3; i++) {
                    output.write(String.format("%d\n", offset[i]));
                }
                for (int i = 0; i < 3; i++) {
                    output.write(String.format("%d\n", range[i]));
                }
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            System.out.println("Failed to write to calibration file " + calFile);
            return false;
        }
        return true;
    }
}
