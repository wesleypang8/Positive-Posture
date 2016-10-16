import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PositivePosture {
    CognitiveServices cognitiveServices;
    BufferedImage image;
    DisplayFrame displayFrame;
    DisplayPanel panel;
    Webcam webcam;
    Capture capture;
    int frameWidth = 1366, frameHeight = 768;
    int faceTop, faceLeft, faceWidth, faceHeight;

    public static void main(String[] args) {
        PositivePosture pp = new PositivePosture();
    }

    public PositivePosture() {
        setupCognitiveServices();
        setupWebcam();
        setupUI();

        //        new Capture().run();
    }

    public void setupCognitiveServices() {
        cognitiveServices = new CognitiveServices("45480e1bce5d49028bf803d097db09c5");
        // Json data returned from Microsoft Cognitive Services
        //http://www.businessnewsdaily.com/images/i/000/007/961/i02/Photo5.jpg
    }

    public void setupWebcam() {
        webcam = Webcam.getDefault();
        Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
                WebcamResolution.HD720.getSize(), };
        webcam.setCustomViewSizes(nonStandardResolutions);
        webcam.setViewSize(WebcamResolution.HD720.getSize());
    }

    public void setupUI() {
        displayFrame = new DisplayFrame(this, "", frameWidth, frameHeight);
        panel = displayFrame.panel;
    }

    public void runCalibrateCapture() {
        new CalibrateCapture().run();
    }

    public void runCapture() {
        capture = new Capture();
        capture.run();
    }

    class CalibrateCapture implements Runnable {
        @Override
        public void run() {
            webcam.open();
            BufferedImage calibrateImage = webcam.getImage();
            webcam.close();
            JsonArray jsonArray = cognitiveServices.postLocalToFaceAPI(calibrateImage);
            if (jsonArray.size() > 0) {
                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                JsonObject faceRectangle = jsonObject.get("faceRectangle").getAsJsonObject();
                faceTop = faceRectangle.get("top").getAsInt();
                faceLeft = faceRectangle.get("left").getAsInt();
                faceWidth = faceRectangle.get("width").getAsInt();
                faceHeight = faceRectangle.get("height").getAsInt();
                panel.drawImage(calibrateImage);
                panel.drawFaceRectangle(Color.GREEN, faceLeft, faceTop, faceWidth, faceHeight);
            }
        }
    }

    class Capture implements Runnable {
        long lastCaptureTime = 0;
        long timeBetweenCaptures = 5000;
        volatile boolean run = true;

        @Override
        public void run() {
            while (true) {
                //                if (System.currentTimeMillis() - lastCaptureTime >= timeBetweenCaptures) {
                //                    lastCaptureTime = System.currentTimeMillis();

                webcam.open();
                image = webcam.getImage();
                webcam.close();
                panel.drawImage(image);

                JsonArray jsonArray = cognitiveServices.postLocalToFaceAPI(image);
                int maxSize = 0, index = 0;
                for (int i = 0; i < jsonArray.size(); i++) {

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    JsonObject faceRectangle = jsonObject.get("faceRectangle").getAsJsonObject();

                    int left = faceRectangle.get("left").getAsInt();
                    int top = faceRectangle.get("top").getAsInt();
                    int width = faceRectangle.get("width").getAsInt();
                    int height = faceRectangle.get("height").getAsInt();

                    if (width + height > maxSize) {
                        maxSize = width + height;
                        index = i;
                    }
                }
                if (jsonArray.size() > 0) {
                    JsonObject faceRectangle = jsonArray.get(index).getAsJsonObject()
                    .get("faceRectangle").getAsJsonObject();
                    int diffWidth = faceRectangle.get("width").getAsInt() - faceWidth;
                    int diffHeight = faceRectangle.get("height").getAsInt() - faceHeight;
                    boolean out = false;
                    if (diffWidth >= 30 && diffHeight >= 30) {
                        out = true;
                    }

                    panel.drawFaceRectangle(out ? Color.BLUE : Color.RED,
                    faceRectangle.get("left").getAsInt(), faceRectangle.get("top").getAsInt(),
                    faceRectangle.get("width").getAsInt(), faceRectangle.get("height").getAsInt());
                }
                //                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }

    }

}
