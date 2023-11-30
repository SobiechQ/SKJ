package ImageTransfer.Client;
import java.awt.image.BufferedImage;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
public class Connection {
    private final BufferedImage image;
    private final DatagramSocket socket;
    public Connection(BufferedImage image){
        this.image = image;
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    private void send(){
        if (this.socket.isClosed())
            return;
        
    }
    private List<Integer> getPixelList(){
        int h = this.image.getData().getHeight();
        int w = this.image.getData().getWidth();
        List<Integer> pixels = new ArrayList<>(h*w);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pixels.add(this.image.getRGB(j, i));
        return pixels;
    }
}