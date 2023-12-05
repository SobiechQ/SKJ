package ImageTransfer.Server;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageDrawer {
    public static List<Integer> toPixelArray(BufferedImage bufferedImage){
        int h = bufferedImage.getData().getHeight();
        int w = bufferedImage.getData().getWidth();
        List<Integer> pixels = new ArrayList<>(h*w);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pixels.add(bufferedImage.getRGB(j, i));
        return pixels;
    }

    public static BufferedImage toBufferedImage(List<Integer> pixels, int w, int h){
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        double i=0;
        for (Integer pixel : pixels) {
            bufferedImage.setRGB((int) (i%w), (int) (i/w), pixel);
            i++;
        }
        return bufferedImage;
    }
    public static void draw(BufferedImage bufferedImage){
        JFrame f = new JFrame();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        f.getContentPane().add(new JLabel(imageIcon));
        f.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }




}
