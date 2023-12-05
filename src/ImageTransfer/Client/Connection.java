package ImageTransfer.Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    private final List<Integer> integers;

    public Connection(BufferedImage image){
        this.integers = this.getPixelList(image);
        this.send();
    }
    private void send(){
        try (var socket = new Socket(InetAddress.getByName("192.168.8.100"), 1010)){
            socket.getOutputStream().write(this.intToByteArray(integers.stream().mapToInt(i->i).toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] intToByteArray(int[]src) {
        byte[]dst = new byte[src.length << 2];
        for (int i=0; i< src.length; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x >>> 24) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 0) & 0xff);
        }
        return dst;
    }
    /*
        WIDTH,HEIGHT,MSG
     */
    private List<Integer> getPixelList(BufferedImage image){
        int w = image.getData().getWidth();
        int h = image.getData().getHeight();
        List<Integer> pixels = new ArrayList<>(h*w+2);
        pixels.add(w);
        pixels.add(h);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pixels.add(image.getRGB(j, i));
        return pixels;
    }
}