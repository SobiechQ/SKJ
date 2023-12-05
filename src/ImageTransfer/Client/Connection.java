package ImageTransfer.Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Connection {
    private final List<Integer> integers;
    private final int width;
    private final int height;
    public Connection(BufferedImage image){
        this.integers = this.getPixelList(image);
//        this.integers = IntStream.range(0,110).map(i->i*10).boxed().collect(Collectors.toList());
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.send();
    }
    private static final int UDP_MAXSIZE = 100; //Bytes size of message
    private void send(){
        try (DatagramSocket socket = new DatagramSocket()){
            int[] inputArray = this.integers.stream().mapToInt(i->i).toArray();
            List<int[]> blocks = IntStream.iterate(0, i -> i + UDP_MAXSIZE)
                    .limit((int) Math.ceil((double) inputArray.length / UDP_MAXSIZE))
                    .mapToObj(j -> Arrays.copyOfRange(inputArray, j, Math.min(inputArray.length, j + UDP_MAXSIZE)))
                    .toList();

            int packetIndex = 0;
            int totalPackets = blocks.size();
            for (int[] block : blocks) {
                /*
                    METADATA
                    packetIndex indexed from 0 to blocks.size() - 1;
                    totalPackets - total number of all packets
                    width
                    height
                    DataLength
                 */
                int[] metadata = new int[]{packetIndex, totalPackets, this.width, this.height, block.length};
                int[] metadataPlusBlock = Arrays.copyOf(metadata, metadata.length + block.length);
                System.arraycopy(block, 0, metadataPlusBlock, metadata.length, block.length);

                System.out.println(Arrays.toString(this.intToByteArray(metadataPlusBlock)));
                socket.send(new DatagramPacket(this.intToByteArray(metadataPlusBlock), metadataPlusBlock.length*4, InetAddress.getByName("127.0.0.1"), 4567));
                packetIndex++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
    private List<Integer> getPixelList(BufferedImage image){
        int h = image.getData().getHeight();
        int w = image.getData().getWidth();
        List<Integer> pixels = new ArrayList<>(h*w);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pixels.add(image.getRGB(j, i));
        return pixels;
    }
}