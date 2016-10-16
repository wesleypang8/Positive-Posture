import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.gson.JsonObject;

public class PositivePosture {
    CognitiveServices cognitiveServices;
    BufferedImage image;
    JFrame frame;
    DisplayPanel panel;
    Webcam webcam;

    public static void main(String[] args) {
        PositivePosture pp = new PositivePosture();
    }

    public PositivePosture() {
        setupCognitiveServices();
        //        setupWebcam();
        //        setupUI();
        //        new Capture().run();
    }

    public void setupCognitiveServices() {
        cognitiveServices = new CognitiveServices("45480e1bce5d49028bf803d097db09c5");
        // Json data returned from Microsoft Cognitive Servicess
        JsonObject jsonObject = cognitiveServices
        .postToFaceAPI("http://www.businessnewsdaily.com/images/i/000/007/961/i02/Photo5.jpg");

    }

    public void setupWebcam() {
        webcam = Webcam.getDefault();
        Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
                WebcamResolution.HD720.getSize(), };
        webcam.setCustomViewSizes(nonStandardResolutions);
        webcam.setViewSize(WebcamResolution.HD720.getSize());
    }

    public void setupUI() {
        frame = new JFrame("");
        frame.setVisible(true);
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DisplayPanel();
        frame.add(panel);
    }

    class Capture implements Runnable {
        long lastCaptureTime = 0;
        long timeBetweenCaptures = 5000;

        @Override
        public void run() {
            while (true) {
                if (System.currentTimeMillis() - lastCaptureTime >= timeBetweenCaptures) {
                    lastCaptureTime = System.currentTimeMillis();
                    webcam.open();
                    image = webcam.getImage();
                    webcam.close();
                    panel.camPic = image;
                    panel.drawImage();
                }
            }
        }

    }

}
