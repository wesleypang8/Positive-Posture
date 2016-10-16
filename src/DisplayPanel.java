import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
    public BufferedImage camPic;

    public DisplayPanel() {
        camPic = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void drawImage() {
        getGraphics().drawImage(camPic, 0, 0, null);
    }
}
