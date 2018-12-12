package com.example.parrot.phoneandsms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Manpreet";


    // veraibles for persmissions
    private static final int PHONECALL_PERMISSIONS_REQUESTCODE = 555;
    private static final int SMS_PERMISSIONS_REQUESTCODE = 888;

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 900;
    // 1. Create a telephony variable
    TelephonyManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. setup the telphony manager
        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // 3. if the device has telephony services, cool! otherwise, show an error.
        if (manager == null) {
            Log.d(TAG, "Sorry, this device cannot make phone calls.");

        }
        else {
            if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                // everything is okay!
                Log.d(TAG, "Ready to make phone calls!");

                // check for phone and sms permissions
                checkManyPermisions();


            }
            else {
                Log.d(TAG, "Sorry, this device cannot make phone calls.");
            }
        }

    }


    // check for permissions
    private void checkManyPermisions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            Log.d(TAG, "App does not have permission to make phone calls.");
            Log.d(TAG, "Asking for permission now!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS},
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted.
            Log.d(TAG, "App already has permission for phone calls.");
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean phonePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean smsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    Log.d(TAG, "Does phone have permission? " + phonePermission);
                    Log.d(TAG, "Does SMS have permission? " + smsPermission);
                }
            }
        }
    }

    public void callNumber(View view) {
        //@TODO:  Add code to get phone number from input box.
        // Create the intent.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // Set the data for the intent as the phone number.
        callIntent.setData(Uri.parse("tel:64755512343"));
        // If package resolves to an app, check for phone permission,
        // and send intent.
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            // checkForPhonePermission();
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }
    }



    public void sendSMS(View view) {

        // 1. get phone and sms from input boxes
        EditText phoneBox = (EditText) findViewById(R.id.phoneNumberBox);
        String phoneNum = phoneBox.getText().toString();

        EditText smsBox = (EditText) findViewById(R.id.smsMessageBox);
        String smsMessage = smsBox.getText().toString();

        // ----

        // 2. Build a SMS message object
        // --------------
        // Set the service center address if needed, otherwise null.
        // + Required nonsense
        String scAddress = null;

        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        // + Required nonsense
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;


        // 3. Use the SMS Manager to send the text
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, scAddress, smsMessage,
                sentIntent, deliveryIntent);

        // 4. Show a success message
        Log.d(TAG, "Message sent!");
        Toast.makeText(this, "Message sent!", Toast.LENGTH_LONG).show();



    }


}
