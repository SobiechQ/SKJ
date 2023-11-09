package UdpServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {
    private static final Map<Integer, Integer> map = new HashMap<>();
    public static final Logger log = Logger.getLogger("App");

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(10053);
        while (true) {
            DatagramPacket recivedUdp = new DatagramPacket(new byte[1500], 1500);
            socket.receive(recivedUdp);
            log.info("Reciving connection from: (" + recivedUdp.getAddress().toString() + ":" + recivedUdp.getPort() + ")...");

            var byteBuffered = ByteBuffer.wrap(recivedUdp.getData());

            int value;
            try {
                value = Integer.decode(
                        Stream.generate(byteBuffered::get)
                                .limit(recivedUdp.getLength())
                                .map(s -> (char) ((int) s))
                                .collect(StringBuilder::new,
                                        StringBuilder::append,
                                        StringBuilder::append).toString());
            } catch (NumberFormatException e) {
                String unable = "Unable to parse recived data";
                log.log(Level.WARNING, unable);
                socket.send(new DatagramPacket(unable.getBytes(), unable.length(), recivedUdp.getAddress(), recivedUdp.getPort()));
                continue;
            }
            log.info("Recived " + value);
            if (map.containsKey(getHash(recivedUdp))) {
                String result = Integer.valueOf((map.get(getHash(recivedUdp)) + value)).toString();
                map.remove(getHash(recivedUdp));
                log.info("Sending result " + result + " to: (" + recivedUdp.getAddress().toString() +":" + recivedUdp.getPort()+")");
                socket.send(new DatagramPacket(result.getBytes(), result.length(), recivedUdp.getAddress(), recivedUdp.getPort()));
                continue;
            }

            String ack = "ack";
            log.info("Sending ack to: (" + recivedUdp.getAddress().toString() +":" + recivedUdp.getPort()+")");
            socket.send(new DatagramPacket(ack.getBytes(), ack.length(), recivedUdp.getAddress(), recivedUdp.getPort()));
            map.putIfAbsent(getHash(recivedUdp), value);

        }
    }
    private static int getHash(DatagramPacket datagramPacket){
        return (datagramPacket.getAddress().hashCode() + " " + datagramPacket.getPort()).hashCode();
    }

}