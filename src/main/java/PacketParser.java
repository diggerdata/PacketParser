import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class PacketProcessor {
    HidDevice myHidDevice;
    ByteOrder be = ByteOrder.LITTLE_ENDIAN;
    int packetSize = 64
    int numFloats = (packetSize / 4) - 1
    byte[] message = new byte[packetSize];

    PacketProcessor(HidDevice hidDevice) {
        myHidDevice = hidDevice;

    }

    float[] command(int idOfCommand, float[] values) {
        ByteBuffer.wrap(message).order(be).putInt(0, idOfCommand).array();
        for (int i = 0; i < numFloats && i < values.length; i++) {
            int baseIndex = (4 * i) + 4;
            ByteBuffer.wrap(message).order(be).putFloat(baseIndex, values[i]).array();
        }
        float[] returnValues = new float[numFloats]
        //println "Writing packet"
        int val = myHidDevice.write(message, packetSize, (byte) 0);
        if (val > 0) {
            //println "Reading packet"
            int read = myHidDevice.read(message, 1000);
            if (read > 0) {
                //println "Parsing packet"
                for (int i = 0; i < numFloats; i++) {
                    int baseIndex = (4 * i) + 4;
                    returnValues[i] = ByteBuffer.wrap(message).order(be).getFloat(baseIndex)
                }
            } else {
                println "Read failed"
            }
        } else {
            println "Writing failed"
        }
        return returnValues
    }

}
