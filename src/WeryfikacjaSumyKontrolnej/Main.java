package WeryfikacjaSumyKontrolnej;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) {
        try (var socket = new Socket("127.0.0.1", new Scanner(System.in).nextInt())) {
            var digset = MessageDigest.getInstance("MD5");
            var inputString = new String(new BufferedInputStream(socket.getInputStream()).readAllBytes());

            var map = inputString
                    .lines()
                    .skip(1)
                    .collect(HashMap::new,
                            (BiConsumer<Map<String, String>, String>) (m, s) -> {
                                var builder = new StringBuilder();
                                for (byte b : digset.digest(s.getBytes(StandardCharsets.UTF_8)))
                                    builder.append(String.format("%02X", b));
                                var copy = builder.toString();
                                builder.setLength(0);
                                m.putIfAbsent(copy, s);
                            },
                            (a, b) -> {
                            }
                    );
            if (inputString.isEmpty())
                throw new RuntimeException();
            System.out.println(inputString);
            String out = map.get(inputString.lines().findFirst().get());
            System.out.println(inputString.lines().findFirst().get());
            map.entrySet().forEach(System.out::println);



            System.out.println("S:"+out+":S");

            new BufferedOutputStream(socket.getOutputStream()).write(out.getBytes());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
