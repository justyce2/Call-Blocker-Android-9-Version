package com.example.callblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallBlockerReceiver extends BroadcastReceiver {

    private static final String TAG = "CallBlockerReceiver";

    // Numbers starting with any of these prefixes will be blocked
    private static final String[] BLOCKED_PREFIXES = {
            "0700",
            "0201",
            "070806",
            "0209",
            "0723"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) return;

        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                if (incomingNumber != null && shouldBlockNumber(incomingNumber)) {
                    Log.d(TAG, "Blocking call from: " + incomingNumber);
                    blockCall(context);
                    Toast.makeText(context, "Blocked call from: " + incomingNumber, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean shouldBlockNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) return false;

        String cleanNumber = phoneNumber.replaceAll("[^0-9+]", "");

        for (String prefix : BLOCKED_PREFIXES) {
            if (cleanNumber.startsWith(prefix)) {
                return true;
            }
            // Also match Nigeria international format, e.g. +234700... for local 0700...
            if (cleanNumber.startsWith("+234") && cleanNumber.length() > 4
                    && ("0" + cleanNumber.substring(4)).startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private void blockCall(Context context) {
        try {
            // Works on Android 9 (API 28) and above with ANSWER_PHONE_CALLS permission granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                if (telecomManager != null) {
                    telecomManager.endCall();
                    Log.d(TAG, "Call ended via TelecomManager");
                }
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Missing permission to end call: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Error blocking call: " + e.getMessage());
        }
    }
}
