import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayFrame extends JFrame implements WindowListener {
    PositivePosture pp;
    DisplayPanel panel;

    public DisplayFrame(PositivePosture pp, String name, int width, int height) {
        super(name);

        this.pp = pp;
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(this);
        panel = new DisplayPanel();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(panel, BorderLayout.CENTER);
        JPanel uiPanel = new JPanel();
        JButton calibrateButton = new JButton("Calibrate");
        uiPanel.add(calibrateButton);
        calibrateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pp.runCalibrateCapture();
            }
        });
        JButton runButton = new JButton("Run");
        uiPanel.add(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pp.runCapture();
            }
        });

        containerPanel.add(uiPanel, BorderLayout.SOUTH);
        add(containerPanel);
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (pp.capture != null)
            pp.capture.run = false;
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }
}
