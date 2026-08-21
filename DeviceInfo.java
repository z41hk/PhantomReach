package com.phantom.reach;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceInfo {
    public static String getFullInfo(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder info = new StringBuilder();
        info.append("📱 DEVICE INFO\n");
        info.append("Model: ").append(Build.MODEL).append("\n");
        info.append("Brand: ").append(Build.BRAND).append("\n");
        info.append("Android: ").append(Build.VERSION.RELEASE).append("\n");
        info.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n");
        info.append("IMEI: ").append(tm.getDeviceId()).append("\n");
        info.append("Phone: ").append(tm.getLine1Number()).append("\n");
        info.append("Network: ").append(tm.getNetworkOperatorName()).append("\n");
        info.append("Country: ").append(tm.getSimCountryIso()).append("\n");
        return info.toString();
    }
}