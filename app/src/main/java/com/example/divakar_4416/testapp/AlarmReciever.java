package com.example.divakar_4416.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class AlarmReciever extends BroadcastReceiver {
    private static final String DEBUG_TAG = "testing";

    @Override
    public void onReceive(Context context, Intent intent) {
        String phone=intent.getStringExtra("phone");
        String msg=intent.getStringExtra("msg");
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null,msg, null, null);
            Log.i(DEBUG_TAG, "success");
        } catch (Exception ex) {
            Log.i(DEBUG_TAG, "failed");
        }
    }
}
