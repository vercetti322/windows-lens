package io.jatinjindal.daemon;

import io.jatinjindal.daemon.service.CompletionManager;
import io.jatinjindal.daemon.service.NotepadManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static io.jatinjindal.daemon.constant.DaemonConstants.*;

public class Main {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {
        scheduler.scheduleAtFixedRate(
                NotepadManager::manageInBackground, MANAGE_NOTEPAD_START,
                MANAGE_NOTEPAD_INTERVAL, TimeUnit.MILLISECONDS
        );
    }
}
