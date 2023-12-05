package ImageTransfer.Kolos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2 {
    public static void main(String[] args) throws IOException {
        try(var socket = new DatagramSocket(Integer.parseInt(args[0]))) {
            DatagramPacket receivePacket = null;
            List<DatagramPacket> datagramPacketList = new ArrayList<>();
            int[] reqValue = {5, 7, 11};
            boolean hello = false;

            while (datagramPacketList.size() != 3) {
                byte[] data = new byte[1024];
                receivePacket = new DatagramPacket(data, data.length);
                socket.receive(receivePacket);

                int dataNumber = data[0];
                int dataSize = data[1];
                String dataMessage = new String(data, 2, dataSize);

                if (dataSize == receivePacket.getLength() - 2) {
                    if (!hello) {
                        if (dataMessage.equals("HELLO")) {
                            socket.send(receivePacket);
                            hello = true;
                        }
                    } else {
                        if (dataNumber == reqValue[0]) {
                            datagramPacketList.add(receivePacket);
                        }
                        if (dataNumber == reqValue[1]) {
                            datagramPacketList.add(receivePacket);
                        }
                        if (dataNumber == reqValue[2]) {
                            datagramPacketList.add(receivePacket);
                        }
                    }
                }
            }
            datagramPacketList.stream().map(i->new String(i.getData())).forEach(System.out::println);
            String responseMessage = "";
            for (DatagramPacket datagramPacket : datagramPacketList) {
                responseMessage += new String(datagramPacket.getData(), 2, datagramPacket.getData()[1]);
            }
            byte[] responseData = new byte[responseMessage.length() + 2];
            responseData[1] = (byte) responseMessage.length();
            System.arraycopy(responseMessage.getBytes(), 0, responseData, 2, responseMessage.length());
            socket.send(new DatagramPacket(responseData, responseData.length, InetAddress.getByName("127.0.0.1"), receivePacket.getPort()));
            System.out.println(Arrays.toString(responseData));
        }

    }
}
