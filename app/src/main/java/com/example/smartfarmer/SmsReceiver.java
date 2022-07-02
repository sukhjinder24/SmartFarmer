package com.example.smartfarmer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    Boolean b;
    String code;
    String mobileNo="";
//    String mobileNoM = "7888840871";
//    String mobileNoA = "7973354887";
//    String mobileNoS = "9041971969";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
//        mobileNoA=getSF(context);
        mobileNo=getSF(context);
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
//            b = (sender.endsWith(mobileNoM) || sender.endsWith(mobileNoA) || sender.endsWith(mobileNoS));  //Just to fetch otp sent from particular No.
            b = (sender.endsWith(mobileNo));
            String messageBody = smsMessage.getMessageBody();
            code = messageBody.replaceAll("[^0-9]", ""); // here abcd contains otp
            if (b) {
                if (messageBody.matches("ON\r")) {
                    Toast.makeText(context.getApplicationContext(), "Switched ON", Toast.LENGTH_SHORT).show();
                } else if (messageBody.matches("OFF\r")) {
                    Toast.makeText(context.getApplicationContext(), "Switched OFF", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.messageReceived(code); // attach value to interface object
                }
            }
        }
    }

    private String getSF(Context context) {
        SharedPreferences sf=context.getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
        return (String) sf.getString("phone", "123");
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}


