package ChatServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerTest {

    private static final Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    private static int clientCount = 0;

    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCount);
                clients.put(clientCount, clientHandler);
                clientHandler.start();
                clientCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void logAndBroadcastMessage(String message, int thisClientCount) {
        for (int i = 0; i < clients.size(); i++) {
            if(i != thisClientCount) {
                clients.get(i).out.println(message);
            }
        }
    }

    private static class ClientHandler extends Thread {
        private final int thisClientCount;
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket, int clientCount) {
            this.clientSocket = socket;
            this.thisClientCount = clientCount;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    logAndBroadcastMessage(inputLine, thisClientCount);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    clientSocket.close();
                    clients.remove(thisClientCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}