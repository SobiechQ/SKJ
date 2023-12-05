package ImageTransfer.Kolos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException {
        args = new String[]{"1010"};
        try (var socket = new DatagramSocket(Integer.parseInt(args[0]))) {
            DatagramPacket packet;
            do{
                packet = new DatagramPacket(new byte[2000], 2000);
                socket.receive(packet);
                System.out.println(Arrays.toString(packet.getData()));
            } while (packet.getData()[1] != packet.getLength()-2);
            byte[] abc = new byte[]{4, 5, 72, 69, 76, 76, 79};
            System.out.println(new String(abc));
            socket.send(new DatagramPacket(abc, 7, InetAddress.getByName("127.0.0.1"), packet.getPort()));
        }
    }
}