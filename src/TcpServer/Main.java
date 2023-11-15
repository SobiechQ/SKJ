package TcpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        args = new String[]{"1020"};
        ServerSocket serverSocket = new ServerSocket(Integer.decode(args[0]));
        while (true) {
            Socket socket = serverSocket.accept();
            String readLine = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            OutputStream output = socket.getOutputStream();
            if (readLine.equals("Hi!")) {
                System.out.println(readLine);
                output.write("Greetings!".getBytes());
            }
            String[] command = readLine.split(" ");
            System.out.println(Arrays.toString(command));
            if (command.length > 2 && command[0].equals("COUNT"))
                output.write(
                        ("RESULT:" +
                                (Integer.decode(command[1]) * Integer.decode(command[2])))
                                .getBytes()
                );
            if (command.length > 1 && command[0].equals("DIVISIBLE"))
                output.write(
                        ("NUMBER" + (Integer.decode(command[1])%3==0?" ":" NOT ") + "DIVISIBLE")
                                .getBytes()
                );

            output.flush();
            output.close();
            socket.close();
        }
    }
}
