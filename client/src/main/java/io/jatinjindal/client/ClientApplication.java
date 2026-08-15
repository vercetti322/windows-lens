package io.jatinjindal.client;

import io.jatinjindal.client.component.MainFrame;
import io.jatinjindal.client.service.QuitHandler;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static io.jatinjindal.client.constant.ClientConstants.*;

public class ClientApplication {

    public static void main(String[] args) {
        Thread.ofPlatform().start(() -> {
            try {
                CefAppBuilder builder = new CefAppBuilder();
                builder.getCefSettings().windowless_rendering_enabled = true;

                builder.setAppHandler(new MavenCefAppHandlerAdapter() {
                    @Override
                    public void stateHasChanged(CefApp.CefAppState state) {
                        if (state == CefApp.CefAppState.TERMINATED) {
                            System.exit(0);
                        }
                    }
                });

                setInstallationDirectory(builder); var cefApp = builder.build();
                CefClient client = cefApp.createClient();

                var router = CefMessageRouter.create();
                router.addHandler(new QuitHandler(), true);

                client.addMessageRouter(router);
                CefBrowser browser = client.createBrowser(
                        UI_RESOURCE_URI, true, false
                );

                SwingUtilities.invokeLater(() ->
                        MainFrame.getInstance(browser).showFrame()
                );
            } catch (Exception e) { throw new RuntimeException(e); }
        });
    }

    private static void setInstallationDirectory(CefAppBuilder builder) {
        Path appDir = Paths.get(System.getProperty(
                USER_HOME), WINDOWS_LENS, JCEF_BUNDLE
        );

        try { Files.createDirectories(appDir);
            builder.setInstallDir(appDir.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
