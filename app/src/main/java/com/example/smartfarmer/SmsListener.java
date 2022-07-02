package com.example.smartfarmer;

import android.content.SharedPreferences;

import androidx.annotation.StringRes;

public interface SmsListener {
    public void messageReceived(String messageText);
}
