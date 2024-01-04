package ChatServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
    public static void main(String[] args) {
        try (var socket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (!socket.isClosed())
                new ClientHandler(socket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ClientHandler extends Thread{
        private final static boolean DEBUG = false;
        private final static Set<ClientHandler> clientHalndlers = new HashSet<>();
        private final static AtomicInteger clientCount = new AtomicInteger(0);
        private final int clientId;
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.clientId = ClientHandler.clientCount.getAndIncrement();
            ClientHandler.clientHalndlers.add(this);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.setName(String.valueOf(this.clientId));
            this.start();
        }
        @Override
        public synchronized void run() {
            this.log("info", "start");
            try {
                String line = in.readLine();
                this.log("read", line);
                this.log(line);
                ClientHandler.clientHalndlers.stream()
                        .filter(client->!client.equals(this))
                        .forEach(c->c.handleResponse(line));
            } catch (IOException e) {
                e.printStackTrace();
                this.log("error", String.valueOf(e.getCause()));
            }
        }
        private void handleResponse(String message){
            this.log("out", message);
            this.out.println(message);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClientHandler that = (ClientHandler) o;
            return clientId == that.clientId && Objects.equals(socket, that.socket);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientId, socket);
        }
        private void log(String type, String msg){
            if (!ClientHandler.DEBUG)
                return;
            System.out.printf("T: [%s/%s], %s: [%s] %n", this.clientId, Thread.currentThread().getName(),type.toUpperCase(),msg);
        }
        private void log(String msg) {
            if (ClientHandler.DEBUG)
                return;
            System.out.println(msg);
        }
    }
}
