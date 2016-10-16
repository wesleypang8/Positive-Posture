import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    
    public void drawFaceRectangle(int left, int top, int width, int height){
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        g2d.drawRect(left, top, width, height);
    }
}
