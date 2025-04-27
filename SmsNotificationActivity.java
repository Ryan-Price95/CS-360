package com.example.inventoryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsNotificationActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 101;

    TextView statusTextView;
    Button enableSmsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_notification);

        enableSmsButton = findViewById(R.id.buttonEnableSms);
        statusTextView = findViewById(R.id.textViewSmsPermissionStatus);

        updatePermissionStatus();

        enableSmsButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendTestSms();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // this closes the current activity and goes back
        return true;
    }

    // Updates Permission Status on Notification page
    private void updatePermissionStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            statusTextView.setText(R.string.sms_permission_granted);
        } else {
            statusTextView.setText(R.string.sms_permission_denied);
        }
    }

    // Sends test SMS on notification enabled
    private void sendTestSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+15551234567", null,
                    "Test Alert: SMS notifications enabled!", null, null);
            Toast.makeText(this, "Test SMS Sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            updatePermissionStatus();

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendTestSms();
            } else {
                Toast.makeText(this, "Permission Denied: SMS not sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
