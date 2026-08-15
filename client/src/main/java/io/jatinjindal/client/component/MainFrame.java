package io.jatinjindal.client.component;

import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static io.jatinjindal.client.constant.ClientConstants.*;

public final class MainFrame extends JFrame {

    private static MainFrame INSTANCE;

    private MainFrame(CefBrowser browser) {
        super(MAIN_FRAME_TITLE); setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false); setLayout(new BorderLayout());
        add(browser.getUIComponent(), BorderLayout.CENTER);

        setSize(MAIN_FRAME_SIZE);
        setLocationRelativeTo(null); validate();

        setShape(new RoundRectangle2D.Double(
                0, 0, MAIN_FRAME_SIZE.width,
                MAIN_FRAME_SIZE.height, 20, 20
        ));
    }

    public void showFrame() {
        setVisible(true);
    }

    public static synchronized MainFrame getInstance(CefBrowser browser) {
        if (INSTANCE == null) {
            INSTANCE = new MainFrame(browser);
        } return INSTANCE;
    }
}