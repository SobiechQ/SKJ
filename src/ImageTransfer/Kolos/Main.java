package ImageTransfer.Kolos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        args = new String[]{"1010"};
        try (var socket = new DatagramSocket(Integer.parseInt(args[0]))) {
            DatagramPacket packet;
            do{
                packet = new DatagramPacket(new byte[2000], 2000);
                socket.receive(packet);
                System.out.println(Arrays.toString(packet.getData()));
            } while (packet.getData()[1] != packet.getLength()-2 && new String(packet.getData(), 2, 5).equals("HELLO"));
            byte[] hello = new byte[]{4, 5, 72, 69, 76, 76, 79};
            socket.send(new DatagramPacket(hello, hello.length, InetAddress.getByName("127.0.0.1"), packet.getPort()));

            List<DatagramPacket> selectedRecivedPackets = new ArrayList<>();
            do {
                packet = new DatagramPacket(new byte[2000], 2000);
                socket.receive(packet);

                if (packet.getData()[0] == 5 || packet.getData()[0] == 7 || packet.getData()[0] == 11) {
                    System.out.println("\t dodano: " +packet.getLength() + " | " + Arrays.toString(packet.getData()));
                    selectedRecivedPackets.add(packet);
                }
            } while (selectedRecivedPackets.size() != 3);
            selectedRecivedPackets.sort((o1, o2) -> o1.getData()[0] - o2.getData()[0]);
            selectedRecivedPackets.forEach(s-> System.out.println(s.getLength() + " | " + Arrays.toString(s.getData())));



            byte[] header = {0, (byte) (selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1] + selectedRecivedPackets.get(2).getData()[1])};
            byte[] output = new byte[2 + (selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1] + selectedRecivedPackets.get(2).getData()[1])];
            System.arraycopy(header, 0, output, 0, 2);
            System.arraycopy(selectedRecivedPackets.get(0).getData(), 2, output, 2, selectedRecivedPackets.get(0).getData()[1]);
            System.arraycopy(selectedRecivedPackets.get(1).getData(), 2, output, 2 + selectedRecivedPackets.get(0).getData()[1], selectedRecivedPackets.get(1).getData()[1]);
            System.arraycopy(selectedRecivedPackets.get(2).getData(), 2, output, 2 + selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1], selectedRecivedPackets.get(2).getData()[1]);
            socket.send(new DatagramPacket(output, output.length, InetAddress.getByName("127.0.0.1"), packet.getPort()));
            System.out.println(Arrays.toString(output));
            while (true) {
                packet = new DatagramPacket(new byte[2000], 2000);
                socket.receive(packet);
                System.out.println(packet.getData()[0] + ", " +packet.getData()[1] + new String(packet.getData()));
            }

        }
    }
}
/*
[0, -90, 76, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109, 32, 100, 111, 108, 111, 114, 32, 115, 105, 116, 32, 97, 109, 101, 116, 44, 32, 99, 111, 110, 115, 101, 99, 116, 101, 116, 117, 114, 32, 97, 100, 105, 112, 105, 115, 99, 105, 110, 103, 32, 101, 108, 105, 116, 46, 78, 97, 109, 32, 118, 105, 116, 97, 101, 32, 109, 97, 115, 115, 97, 32, 118, 101, 104, 105, 99, 117, 108, 97, 44, 32, 116, 105, 110, 99, 105, 100, 117, 110, 116, 32, 109, 97, 117, 114, 105, 115, 32, 97, 44, 32, 118, 101, 104, 105, 99, 117, 108, 97, 32, 101, 110, 105, 109, 46, 68, 111, 110, 101, 99, 32, 116, 101, 109, 112, 117, 115, 32, 110, 101, 113, 117, 101, 32, 97, 99, 32, 100, 111, 108, 111, 114, 32, 117, 108, 116, 114, 105, 99, 105, 101, 115, 32, 117, 108, 108, 97, 109, 99, 111, 114, 112, 101, 114, 46]
[0, -90, 76, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109, 32, 100, 111, 108, 111, 114, 32, 115, 105, 116, 32, 97, 109, 101, 116, 44, 32, 99, 111, 110, 115, 101, 99, 116, 101, 116, 117, 114, 32, 97, 100, 105, 112, 105, 115, 99, 105, 110, 103, 32, 101, 108, 105, 116, 46, 78, 97, 109, 32, 118, 105, 116, 97, 101, 32, 109, 97, 115, 115, 97, 32, 118, 101, 104, 105, 99, 117, 108, 97, 44, 32, 116, 105, 110, 99, 105, 100, 117, 110, 116, 32, 109, 97, 117, 114, 105, 115, 32, 97, 44, 32, 118, 101, 104, 105, 99, 117, 108, 97, 32, 101, 110, 105, 109, 46, 68, 111, 110, 101, 99, 32, 116, 101, 109, 112, 117, 115, 32, 110, 101, 113, 117, 101, 32, 97, 99, 32, 100, 111, 108, 111, 114, 32, 117, 108, 116, 114, 105, 99, 105, 101, 115, 32, 117, 108, 108, 97, 109, 99, 111, 114, 112, 101, 114, 46]
            byte[] header = {12, (byte) (selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1] + selectedRecivedPackets.get(2).getData()[1])};
            byte[] output = new byte[2 + (selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1] + selectedRecivedPackets.get(2).getData()[1])];
            System.arraycopy(header, 0, output, 0, 2);
            System.arraycopy(selectedRecivedPackets.get(0).getData(), 2, output, 2, selectedRecivedPackets.get(0).getData()[1]);
            System.arraycopy(selectedRecivedPackets.get(1).getData(), 2, output, 2 + selectedRecivedPackets.get(0).getData()[1], selectedRecivedPackets.get(1).getData()[1]);
            System.arraycopy(selectedRecivedPackets.get(2).getData(), 2, output, 2 + selectedRecivedPackets.get(0).getData()[1] + selectedRecivedPackets.get(1).getData()[1], selectedRecivedPackets.get(2).getData()[1]);
            socket.send(new DatagramPacket(output, output.length, InetAddress.getByName("127.0.0.1"), packet.getPort()));
 */