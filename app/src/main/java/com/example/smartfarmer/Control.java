//package com.example.smartfarmer;
//
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Control extends AppCompatActivity {
//
//    TextView textViewMessage;
//    SmsBroadcastReceiver smsBroadcastReceiver;
//    EditText otpText;
//    String registeredMobileNo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.control);
//        textViewMessage=findViewById(R.id.tvMessage);
//        registeredMobileNo=getIntent().getStringExtra("phone");
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                textViewMessage.setText(messageText);
//            }
//        });
//    }
//
//}
