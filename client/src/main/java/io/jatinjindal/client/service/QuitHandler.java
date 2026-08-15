package io.jatinjindal.client.service;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import javax.swing.*;

public class QuitHandler extends CefMessageRouterHandlerAdapter {

    @Override
    public boolean onQuery(
            CefBrowser browser, CefFrame frame, long queryId,
            String request, boolean persistent, CefQueryCallback callback
    ) {
        if (request.equals("quit")) {
            callback.success(""); SwingUtilities.invokeLater(
                    () -> CefApp.getInstance().dispose()
            ); return true;
        } return false;
    }
}
