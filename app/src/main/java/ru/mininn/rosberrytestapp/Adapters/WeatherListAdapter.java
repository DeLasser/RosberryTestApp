package ru.mininn.rosberrytestapp.Adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.mininn.rosberrytestapp.Database.DBHelper;
import ru.mininn.rosberrytestapp.Models.Weather;
import ru.mininn.rosberrytestapp.R;

/**
 * Recycler view for cards and creating Dialogs. Creating dialogs at this adapter is not a good idea but it so easy and fast.
 */

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private Context mContext;
    private List<Weather> mItems = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCityTextView;
        public TextView mTempTextView;
        public ImageView mImageView;
        public CardView mCardView;

        public ViewHolder(View view) {
            super(view);

            mCityTextView = (TextView) view.findViewById(R.id.card_city);
            mTempTextView = (TextView) view.findViewById(R.id.card_temp);
            mImageView = (ImageView) view.findViewById(R.id.card_icon);
            mCardView = (CardView) view.findViewById(R.id.card_weather);
        }
    }

    public WeatherListAdapter(Context context) {
        mContext = context;
        updateItems();
    }

    public void updateItems() {
        mItems.clear();
        mItems.addAll(new DBHelper(mContext).getAllWeather(mContext));
        super.notifyDataSetChanged();
    }

    @Override
    public WeatherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather, parent, false);
        return new WeatherListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherListAdapter.ViewHolder holder, final int position) {
        final Weather weather = mItems.get(position);
        holder.mCityTextView.setText(weather.getCity());
        holder.mTempTextView.setText(String.valueOf(Math.round(weather.getTemp()-273.15))+"C");
        holder.mImageView.setImageResource(weather.getIconResId());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.dialog_weather, null);
                builder.setTitle(weather.getCity() +"," + weather.getCountry());
                TextView cityTextView = (TextView) dialogView.findViewById(R.id.dialog_temp);
                cityTextView.setText("" + Math.round((weather.getTemp() - 273.15)) + "C");
                ((TextView) dialogView.findViewById(R.id.dialog_hum)).setText(mContext.getString(R.string.humidity) + weather.getHumidity() + "%");
                ((TextView) dialogView.findViewById(R.id.dialog_press)).setText(mContext.getString(R.string.pressure) + weather.getPressure() / 1000 + " mBar");
                ((TextView) dialogView.findViewById(R.id.dialog_wind_speed)).setText(mContext.getString(R.string.wind_speed) + Math.round(weather.getWindSpeed()) + " mps");
                ((TextView) dialogView.findViewById(R.id.dialog_wind_deg)).setText(mContext.getString(R.string.wind_deg) + Math.round(weather.getWindDeg() - 273.15) + "C");
                ((ImageView) dialogView.findViewById(R.id.dialog_icon)).setImageResource(weather.getIconResId());
                builder.setPositiveButton("OK", null);
                builder.setView(dialogView);
                builder.create();
                builder.show();

            }
        });

    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }
}