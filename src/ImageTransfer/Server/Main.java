package ImageTransfer.Server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try (var serverSocket = new ServerSocket(1010)) {
            while (true) {
                var socket = serverSocket.accept();
                int[] recived = Main.byteToIntArray(new BufferedInputStream(socket.getInputStream()).readAllBytes());
                ImageDrawer.draw(ImageDrawer.toBufferedImage(Arrays.stream(recived).skip(2).boxed().toList(), recived[0], recived[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
