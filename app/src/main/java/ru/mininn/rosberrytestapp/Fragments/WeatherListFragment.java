package ru.mininn.rosberrytestapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mininn.rosberrytestapp.Adapters.WeatherListAdapter;
import ru.mininn.rosberrytestapp.R;

/**
 * simple fragment, no comments
 */

public class WeatherListFragment extends Fragment {
    private RecyclerView mWeatherList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list,container,false);

        mWeatherList = (RecyclerView) view.findViewById(R.id.weather_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWeatherList.setLayoutManager(layoutManager);
        mWeatherList.setAdapter(new WeatherListAdapter(getContext()));

        return  view;
    }
}
