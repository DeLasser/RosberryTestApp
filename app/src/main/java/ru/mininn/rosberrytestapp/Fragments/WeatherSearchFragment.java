package ru.mininn.rosberrytestapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import ru.mininn.rosberrytestapp.Database.DBHelper;
import ru.mininn.rosberrytestapp.Models.Weather;
import ru.mininn.rosberrytestapp.R;
import ru.mininn.rosberrytestapp.WeatherParser;
import ru.mininn.rosberrytestapp.WeatherURLClient;

/**
 * Created by nik on 17.02.17.
 */

public class WeatherSearchFragment extends Fragment {
    private TextView cityText;
    private TextView temperature;
    private TextView pressure;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView humidity;
    private ImageView imgView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onSaveInstanceState(savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_weather_search, container, false);
        cityText = (TextView) view.findViewById(R.id.city);
        temperature = (TextView) view.findViewById(R.id.temp);
        humidity = (TextView) view.findViewById(R.id.hum);
        pressure = (TextView) view.findViewById(R.id.press);
        windSpeed = (TextView) view.findViewById(R.id.wind_speed);
        windDeg = (TextView) view.findViewById(R.id.wind_deg);
        imgView = (ImageView) view.findViewById(R.id.icon);
        return view;

    }



    public class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherURLClient())).getWeatherData(params[0]);
            DBHelper db = new DBHelper(getContext());
            db.close();
            try {
                weather = WeatherParser.getWeather(data);
                if(weather != null) {
                    db.addWeather(weather,getContext());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            if(weather != null) {
                cityText.setText(weather.getCity() + "," + weather.getCountry());
                temperature.setText("" + Math.round((weather.getTemp() - 273.15)) + "C");
                humidity.setText(getString(R.string.humidity) + weather.getHumidity() + "%");
                pressure.setText(getString(R.string.pressure) + weather.getPressure() / 1000 + " mBar");
                windSpeed.setText(getString(R.string.wind_speed) + Math.round(weather.getWindSpeed()) + " mps");
                windDeg.setText(getString(R.string.wind_deg) + Math.round(weather.getWindDeg() - 273.15) + "C");
                imgView.setImageResource(weather.getIconResId());
            }else {
                Toast.makeText(getContext(),"City not found",Toast.LENGTH_LONG).show();
            }

        }

    }

    public void startSearch(String query){
        WeatherTask task = new WeatherTask();
        task.execute(new String[]{query});
    }

}
