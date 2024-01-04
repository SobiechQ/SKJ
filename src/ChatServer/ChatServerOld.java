package ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ChatServerOld {
    static {
        new Thread(()->{
            if (System.getProperty("os.name").equals("Windows 10"))
                return;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
            new RuntimeException("Automatic Time Out").printStackTrace();
            System.out.println("Automatic Time Out");
            System.exit(2);
        }).start();
    }
    public static void main(String[] args) {
        Map<String, ClientHandler> clients = new HashMap<>();
        try (var socket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (!socket.isClosed()){
                var accept = socket.accept();
                var builder = new StringBuilder();
                for (int read; (read = accept.getInputStream().read()) != -1; )
                    builder.append((char) read);
                var msg = new Message(builder.toString());
//                System.out.printf("RECIVED: [%s]%n", msg);
                System.out.println(msg);
                if (!clients.containsKey(msg.name))
                    clients.put(msg.name, new ClientHandler(msg.name, accept));
                clients.get(msg.name).addMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final class ClientHandler extends Thread implements UserListener {
        private static final List<Message> pastMessages = new LinkedList<>();
        private final String owner;
        private final Socket ownerSocket;

        public ClientHandler(String owner, Socket ownerSocket) {
            this.addToListeners(this);
            this.owner = owner;
            this.ownerSocket = ownerSocket;
            this.outputStream(pastMessages);
        }
        public void addMessage(Message msg){
            this.fireAllListeners(msg);
            ClientHandler.pastMessages.add(msg);
        }
        private void outputStream(Collection<Message> output){
            output.stream()
                    .filter(m->!m.name.equals(this.owner))
                    .forEach(m->this.outputStream(m.toString()));
        }
        @Override
        public void fire(Message msg) {
            this.outputStream(Set.of(msg));
        }
        private void outputStream(String output){
            try {
                this.ownerSocket.getOutputStream().write(output.getBytes());
                this.ownerSocket.getOutputStream().flush();
//                System.out.printf("DESTINATION: [%s], MESSAGE: [%s]%n", this.owner, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public interface UserListener {
        List<UserListener> listeners = new LinkedList<>();
        void fire(Message msg);
        default void fireAllListeners(Message msg){
            UserListener.listeners.forEach(l->l.fire(msg));
        }
        default void addToListeners(UserListener target){
            UserListener.listeners.add(target);
        }
    }

    private static final class Message {
        private final String name;
        private final String content;

        private Message(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public Message(String message) {
                this(message.split(" ")[0], Arrays.stream(message.split(" ")).skip(1).collect(Collectors.joining()));
            }

        public String name() {
            return name;
        }

        public String content() {
            return content;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Message) obj;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, content);
        }

        @Override
        public String toString() {
            return String.format("%s %s", this.name, this.content);
        }
    }
}
