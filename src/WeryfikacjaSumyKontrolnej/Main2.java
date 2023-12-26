package WeryfikacjaSumyKontrolnej;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        try(var socket = new Socket("127.0.0.1", new Scanner(System.in).nextInt())) {
            socket.getInputStream().readAllBytes();
            socket.getOutputStream().write("test".getBytes());
            socket.getOutputStream().flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
