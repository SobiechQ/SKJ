package ImageTransfer.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame{
    File file = null;

    Main(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(350,200);
        this.setVisible(true);
        JLabel label1 = new JLabel("Nie wybrano pliku!");
        label1.setSize(300, 100);
        JFileChooser chooser = new JFileChooser();
        this.add(label1);
        this.add(chooser);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        chooser.setAcceptAllFileFilterUsed(false);

        JButton buttonSend = new JButton("Wyslij"){
            @Override
            protected void fireActionPerformed(ActionEvent event) {
                if (file == null)
                    return;
                try {
                    new Connection(ImageIO.read(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        JButton buttonChoose = new JButton("Wybierz"){
            @Override
            protected void fireActionPerformed(ActionEvent event) {
                int code = chooser.showOpenDialog(this);
                if (code == JFileChooser.APPROVE_OPTION){
                    file = chooser.getSelectedFile();
                    label1.setText(file.getAbsolutePath());
                }
            }
        };
        var buttonPanel = new JPanel();
        buttonPanel.setSize(30,30);
        buttonPanel.add(buttonChoose);
        buttonPanel.add(buttonSend);
        this.getContentPane().add(buttonPanel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
