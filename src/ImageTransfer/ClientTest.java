package ImageTransfer;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientTest {
    public static void main(String[] args) {
        String[] messages = {
                "c1 test1",
                "c1 test2",
                "c2 test3",
                "c2 test4",
        };
        Arrays.stream(messages)
                .forEach(s-> {
                    try (var socket = new Socket("127.0.0.1", 1010)){
                        socket.getOutputStream().write(s.getBytes());
                        socket.getOutputStream().flush();
                        System.out.println("send..");
                        var builder = new StringBuilder();
                        for (int read; (read = socket.getInputStream().read()) != -1 && read != 10; ) {
                            System.out.print((char) read);
                            builder.append((char) read);
                        }
                        System.out.println(builder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
