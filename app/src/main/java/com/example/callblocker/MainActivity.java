package com.example.callblocker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView statusText;
    private Button enableButton;
    private Button batteryButton;
    private TextView instructionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        enableButton = findViewById(R.id.enableButton);
        batteryButton = findViewById(R.id.batteryButton);
        instructionsText = findViewById(R.id.instructionsText);

        enableButton.setOnClickListener(v -> requestPermissions());
        batteryButton.setOnClickListener(v -> requestIgnoreBatteryOptimizations());

        updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        boolean hasPermissions = checkPermissions();
        boolean batteryIgnored = isBatteryOptimizationIgnored();

        if (hasPermissions) {
            String msg = "✓ Call Blocker is ACTIVE\n\nBlocking: 0700, 0201, 070806, 0209, 0723";
            if (!batteryIgnored) {
                msg += "\n\n⚠ For reliable background blocking, also tap 'Allow Background Running' below.";
            }
            statusText.setText(msg);
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            enableButton.setVisibility(View.GONE);
            instructionsText.setVisibility(View.GONE);
        } else {
            statusText.setText("⚠ Call Blocker is NOT active");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            enableButton.setVisibility(View.VISIBLE);
            instructionsText.setVisibility(View.VISIBLE);
            instructionsText.setText("To activate call blocking, tap the button below and allow all permissions.");
        }

        batteryButton.setVisibility(batteryIgnored ? View.GONE : View.VISIBLE);
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.CALL_PHONE
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "Permissions granted! Call blocking is now active.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "All permissions are required for call blocking to work", Toast.LENGTH_LONG).show();
            }
            updateStatus();
        }
    }

    private boolean isBatteryOptimizationIgnored() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isIgnoringBatteryOptimizations(getPackageName());
    }

    private void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Please disable battery optimization manually in Settings", Toast.LENGTH_LONG).show();
        }
    }
}
