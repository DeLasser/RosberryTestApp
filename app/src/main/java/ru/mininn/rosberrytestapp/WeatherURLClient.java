package ru.mininn.rosberrytestapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nik on 18.02.17.
 */

public class WeatherURLClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String API_KEY = "&appid=dbf299a29dae5ed023e8a06da75e3cea";

    public String getWeatherData(String location) {
        HttpURLConnection connection = null ;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) ( new URL(BASE_URL + location +API_KEY)).openConnection();
            Log.d("URL",BASE_URL + location +API_KEY);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            inputStream.close();
            connection.disconnect();
            Log.d("buffer",buffer.toString());
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

}
