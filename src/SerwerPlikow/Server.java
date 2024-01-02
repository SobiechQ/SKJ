package SerwerPlikow;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args){
        try (var socket = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Server.log("before");
                var accept = socket.accept();
                Server.log("accept");
                String command = "";
                var builder = new StringBuilder();
                int read;
                while ((read = accept.getInputStream().read()) != -1 && read != 10){
                    builder.append((char) read);
                    Server.log(String.valueOf((char)read + ", " + read));
                }
                command = builder.toString();

                Server.log(command);
                var split = command.split(" ");
                if (command.equals("FILE LIST")) {
                    var response = Server.fileList().stream()
                            .filter(f -> f.getName().matches(".+\\.txt"))
                            .map(f -> f.getName() + "\n")
                            .collect(Collectors.joining());
                    Server.response(accept, response.substring(0, response.length()));
                }
                else if (split.length > 1 && split[0].equals("GET")){
                    var optionalFile = Server.fileList().stream().filter(f->f.getName().equals(split[1])).findFirst();
                    if (optionalFile.isPresent()) {
                        var response = new BufferedReader(new FileReader(optionalFile.get())).lines().map(s -> s + '\n').collect(Collectors.joining());
                        Server.response(accept, response.substring(0, response.length()));
                    }
                    if (optionalFile.isEmpty())
                        Server.response(accept, "NO SUCH FILE\n");
                } else {
                    Server.response(accept, "NO SUCH COMMAND\n");
                }

                accept.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<File> fileList(){
        var catalog = new File("files");
        var files = catalog.listFiles();
        return new LinkedList<>(Arrays.asList(files));
    }
    public static void response(Socket respondTo, String message) throws IOException {
        Server.log(message);
        respondTo.getOutputStream().write(message.getBytes());

    }
    public static void log(String log){
        try {
            throw new RuntimeException(log);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
