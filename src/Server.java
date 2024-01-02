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
                var accept = socket.accept();
                String command = new String(new BufferedInputStream(accept.getInputStream()).readAllBytes());
                Server.log(command);
                var split = command.split(" ");
                if (command.equals("FILE LIST")) {
                    var response = Server.fileList().stream()
                            .filter(f -> f.getName().matches(".+\\.txt"))
                            .map(f -> f.getName() + "\n")
                            .collect(Collectors.joining());
                    Server.response(accept, response.substring(0, response.length()-1));
                }
                else if (split.length > 1 && split[0].equals("GET")){
                    var optionalFile = Server.fileList().stream().filter(f->f.getName().equals(split[1])).findFirst();
                    if (optionalFile.isPresent()) {
                        var response = new BufferedReader(new FileReader(optionalFile.get())).lines().map(s -> s + '\n').collect(Collectors.joining());
                        Server.response(accept, response.substring(0, response.length()-1));
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
        System.out.println(catalog.getAbsolutePath());
        var files = catalog.listFiles();
        return new LinkedList<>(Arrays.asList(files));
    }
    public static void response(Socket respondTo, String message) throws IOException {
        System.out.println("Start");
        System.out.print(message);
        System.out.println("End");
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
