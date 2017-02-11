package com.example.android.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static android.R.attr.description;
import static android.R.attr.value;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class Main2Activity extends AppCompatActivity {
    EditText cityName;
    TextView resultMSG;

    public void onCity(View v) {

        InputMethodManager mg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mg.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
        try {
            String plusSpaceCity = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            //String result = null;


            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + plusSpaceCity + "&appid=e221ca35e019fa560dd7b47bf0db346f");

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        Log.i("cityEntered", cityName.getText().toString());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cityName = (EditText) findViewById(R.id.city);
        resultMSG = (TextView) findViewById(R.id.result);
    }

    //dl
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();

                return "Failed";

            }


        }

        @Override
        protected void onPostExecute(String result) {
            String messageSKY = cityName.getText().toString();
           // String temperature = "";
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                String totalJsonWeather = jsonObject.getString("weather");
                //  String tempe = jsonObject.getString("main");
                Log.i("all_waether", totalJsonWeather);

                JSONArray arr = new JSONArray(totalJsonWeather);
//JSONArray arrayTemp = jsonObject.getJSONArray("main");
                JSONObject oo = jsonObject.getJSONObject("main");
JSONObject kk =jsonObject.getJSONObject("wind");
                for (int i = 0; i < arr.length(); i++) {

                    String des = "";

                    JSONObject jj = arr.getJSONObject(i);

                    des = jj.getString("description");
                    if (des != "") {
                        messageSKY += ", "+des;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();
                    }
                }


                String normal = "";
                String windS = "";
                String humidity1 = "";


                normal = oo.getString("temp");
                windS=kk.getString("speed");
                humidity1=oo.getString("humidity");
                Double tempF=Double.parseDouble(normal);
                Double tempC=(tempF-273.15);


                tempC=Double.parseDouble(new DecimalFormat("##.##").format(tempC));

                Log.i("min", normal);
                if (normal != "") {
                    messageSKY += "\r\nTemperature: " + Double.toString(tempC) +" C"+ "\r\n"+"Wind Speed: "+windS+" m/s"+"\r\nHumidity: "+humidity1+"%";
                }else{
                    Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();
                }


                if (messageSKY != "") {
                    resultMSG.setText(messageSKY);

                } else
                    Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Blank/No weather found", Toast.LENGTH_LONG).show();
            }
        }


    }
}
