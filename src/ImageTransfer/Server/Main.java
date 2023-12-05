package ImageTransfer.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        try(DatagramSocket socket = new DatagramSocket(4567)){
            List<Integer> integers = new ArrayList<>();
            Integer max = null;
            while (max == null || integers.size() != max/100){
                byte[] data = new byte[2000];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                int[] recived = Main.byteToIntArray(data);
                if (max == null)
                    max = recived[1];
                for (int i = 5; i < recived.length; i++) {
                    integers.add(recived[i]);
                }
                System.out.println(integers.size());
            }
        }
    }

    private static int[] byteToIntArray(byte[] buf) {
        int[] intArr = new int[buf.length / 4];
        int offset = 0;
        for(int i = 0; i < intArr.length; i++) {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
                    ((buf[1 + offset] & 0xFF) << 16) | ((buf[offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }
}
