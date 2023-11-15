package TcpClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {

        Socket clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 1020);


        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        InputStreamReader isr = new InputStreamReader(is);
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osw);


        bw.write("DIVISIBLE 27");
        bw.newLine();
        bw.newLine();
        bw.flush();

        String resp = br.readLine();
        System.out.println(resp);

        clientSocket.close();

    }
}
