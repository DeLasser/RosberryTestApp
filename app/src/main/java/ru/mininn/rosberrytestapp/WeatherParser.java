package ru.mininn.rosberrytestapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.mininn.rosberrytestapp.Models.Weather;

/**
 * Created by nik on 18.02.17.
 */

public class WeatherParser {

    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();
        if(data==null){
            return null;
        }
        JSONObject jObj = new JSONObject(data);

        JSONObject coordObj = jObj.getJSONObject("coord");
        weather.setLatitude(getFloat("lat",coordObj));
        weather.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        weather.setCountry(getString("country", sysObj));
        weather.setSunrise(getInt("sunrise", sysObj));
        weather.setSunset(getInt("sunset", sysObj));
        weather.setCity(getString("name", jObj));

        JSONArray jArr = jObj.getJSONArray("weather");

        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.setWeatherId(getInt("id", JSONWeather));
        weather.setDescription(getString("description", JSONWeather));
        weather.setCondition(getString("main", JSONWeather));
        weather.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.setHumidity(getInt("humidity", mainObj));
        weather.setPressure(getInt("pressure", mainObj));
        weather.setMaxTemp(getFloat("temp_max", mainObj));
        weather.setMinTemp(getFloat("temp_min", mainObj));
        weather.setTemp( getFloat("temp",mainObj));

        JSONObject wObj = getObject("wind", jObj);
        weather.setWindSpeed(getFloat("speed", wObj));
        weather.setWindDeg(getFloat("deg", wObj));

        JSONObject cObj = getObject("clouds", jObj);
        weather.setCludPerc(getInt("all", cObj));
            return weather;

    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
