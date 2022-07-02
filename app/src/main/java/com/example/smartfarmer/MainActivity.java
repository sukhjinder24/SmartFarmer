package com.example.smartfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView weatherTextViewLeft, weatherTextViewRight, moistTV, locationTV, tV;
    LinearLayout weatherLL;
    TimePickerDialog timePickerDialog;
    EditText hrsEd, minEd;


    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appId = "0b4ed7633aeeb550775e6294f3a24e1a";
    String city = "Maur";
    String registeredMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherLL = findViewById(R.id.weatherLL);
        weatherTextViewLeft = findViewById(R.id.weatherTvL);
        weatherTextViewRight = findViewById(R.id.weatherTvR);
        locationTV = findViewById(R.id.locationTV);
        moistTV = findViewById(R.id.moistTV);
        hrsEd = findViewById(R.id.hoursED);
        minEd = findViewById(R.id.minED);
//        tV = findViewById(R.id.tv);
        registeredMobileNo = getString(R.string.registered_mobile_no);
//        locationTV.setText("Device installed at " + city +" connected with "+registeredMobileNo);
        weatherLL.setBackgroundResource((weatherTextViewLeft.getText().toString().isEmpty()) ? 0 : R.drawable.weather_back);


        fetchSF();


    }

    private void fetchSF() {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_APPEND);
//        tV.setText((String) sharedPreferences.getString("phone", "123"));
        registeredMobileNo = (String) sharedPreferences.getString("phone", "123");
        city = (String) sharedPreferences.getString("location", "Bathinda");
        locationTV.setText("Device installed at " + city +" connected with "+registeredMobileNo);
    }


    public void weatherUpdate(View view) {
        String tempUrl = "";
        tempUrl = url + "?q=" + city + "&appid=" + appId;
        //        tempUrl="http://api.openweathermap.org/data/2.5/weather?q=Bathinda&appid=0b4ed7633aeeb550775e6294f3a24e1a";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String left = "";
                String right = "";

                DecimalFormat df = new DecimalFormat("#.##");
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonObjectResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonObjectResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonObjectResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonObjectResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonObjectResponse.getString("name");

                    left += " " + cityName + " (" + countryName + ")"
                            + "\n\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Wind Speed: " + wind + "m/s"
                            + "\n Pressure: " + pressure + " hPa";

                    right += "\n\n " + description
                            + "\n Humidity: " + humidity + "%"
                            + "\n Clouds: " + clouds + "%";

                } catch (Exception e) {
                    e.printStackTrace();
                }
                weatherTextViewLeft.setText(left);
                weatherTextViewRight.setText(right);
                weatherLL.setBackgroundResource((weatherTextViewLeft.getText().toString().isEmpty()) ? 0 : R.drawable.weather_back);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Check I" +
                        "nternet Connection and City chosen.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public void pickTime(View view) {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hOfDay, int minOfHour) {
                hrsEd.setText(String.format("%02d", hOfDay));
                minEd.setText(String.format("%02d", minOfHour));

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    public void setAlarm(View view) {
        if (!hrsEd.getText().toString().isEmpty() && !minEd.getText().toString().isEmpty()) {
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(hrsEd.getText().toString()));
            intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(minEd.getText().toString()));
            intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Time to Switch off Device");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Action not Supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
        }

    }


    public void On(View view) {
        sendSMS(registeredMobileNo, "ON");
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                moistTV.setText(messageText);
            }
        });
//        Intent intent = new Intent(MainActivity.this, Control.class);
//        intent.putExtra("phone", registeredMobileNo);
//        startActivity(intent);
    }

    public void Off(View view) {
        sendSMS(registeredMobileNo, "OFF");
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                moistTV.setText(messageText);
            }
        });
    }


    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);
            Toast.makeText(getApplicationContext(), "Processing", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...\n" +
                    "Check App Permissions", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    public void moist(View view) {
        sendSMS(registeredMobileNo, "Moisture Update");

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                moistTV.setText(messageText);
            }
        });

    }

    public void profileUpdate(View view) {
        Intent intent = new Intent(MainActivity.this, Profiles.class);
        startActivity(intent);
    }
}