package com.example.weatherappbharat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText wcity,wcountry;
    TextView result;
    private final String url="http://api.openweathermap.org/data/2.5/weather";
            private final String appid="e5f40b77255ee0147eb7c5e42185933b";
            DecimalFormat df=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wcity=findViewById(R.id.city);
        wcountry=findViewById(R.id.country);
        result=findViewById(R.id.result);
    }

    public void getweatherdetails(View view) {
        String tempurl="";
        String country= wcountry.getText().toString().trim();
        String city = wcity.getText().toString().trim();
        if(city.equals("")){
            result.setText("city field cannot be empty!");
        }else{
            if(!country.equals("")){
                tempurl=url+"?q="+city+","+country+"&appid="+appid;
            }else{
                tempurl=url+"?q="+city+"&appid="+appid;
            }
            StringRequest stringRequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d( "response",response);
                    String output="";
                    try {
                        JSONObject jsonResponse =new JSONObject(response);
                        JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                        String description= jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain= jsonResponse.getJSONObject("main");
                        double temp=jsonObjectMain.getDouble("temp")-273.15;
                        double feelsLike=jsonObjectMain.getDouble("feels_like")-273.15;
                        float pressure=jsonObjectMain.getInt("pressure");
                        int humidity=jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind=jsonResponse.getJSONObject("wind");
                        String wind=jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds=jsonResponse.getJSONObject("clouds");
                        String clouds=jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys= jsonResponse.getJSONObject("sys");
                        String countryName= jsonObjectSys.getString("country");
                        String cityName= jsonResponse.getString("name");
                        result.setTextColor(Color.rgb( 68,135, 199));
                        output+="Current Weather of " + cityName + "(" + countryName + ")" + "\n Temp: "
                                + df.format(temp) + " °C" +"\n Feels like: " + df.format(feelsLike) + "°C" + "\n " +
                                "Humidity: " + humidity + "%" + "\n Description: " + description + "\n" +
                                " Wind Speed: "+ wind + "m/s" + "\n Cloudiness: " + clouds + "%"
                                +"\n Pressure: " + pressure + " hPa";
                        result.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, error -> Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}