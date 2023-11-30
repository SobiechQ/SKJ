package ImageTransfer.Server;

import javax.imageio.ImageIO;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception{
//        byte[] pixels = ((DataBufferByte) ImageIO.read(new File(System.getProperty("user.home") + "/img.jpeg")).getRaster().getDataBuffer()).getData();
//        System.out.println(Arrays.toString(pixels));
        var img = ImageIO.read(new File(System.getProperty("user.home") + "/img.jpeg"));
        var a = ImageDrawer.toPixelArray(img);
        var zapis = ImageDrawer.toBufferedImage(a, img.getWidth(), img.getHeight());
        ImageDrawer.draw(zapis);
    }
}
