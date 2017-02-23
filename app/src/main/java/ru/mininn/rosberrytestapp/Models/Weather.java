package ru.mininn.rosberrytestapp.Models;

import android.widget.Switch;

import ru.mininn.rosberrytestapp.R;

/**
 * Created by nik on 18.02.17.
 */

public class Weather {

    private int mWeatherId;
    private String mCountry;
    private String mCity;
    private float mLongitude;
    private float mLatitude;
    private long mSunset;
    private long mSunrise;
    private String mCondition;
    private String mDescription;
    private String mIcon;
    private float mPressure;
    private float mHumidity;
    private float mTemp;
    private float mMinTemp;
    private float mMaxTemp;
    private float mWindSpeed;
    private float mWindDeg;
    private int mCludPerc;

    public int getWeatherId() {
        return mWeatherId;
    }

    public int getIconResId() {
        switch (mIcon) {
            case "01d":
                return R.drawable.d01d;
            case "02d":
                return R.drawable.d02d;
            case "03d":
                return R.drawable.d03d;
            case "04d":
                return R.drawable.d04d;
            case "09d":
                return R.drawable.d09d;
            case "10d":
                return R.drawable.d10d;
            case "11d":
                return R.drawable.d11d;
            case "13d":
                return R.drawable.d13d;
            case "50d":
                return R.drawable.d50d;
            case "01n":
                return R.drawable.n01n;
            case "02n":
                return R.drawable.n02n;
            case "03n":
                return R.drawable.n03n;
            case "04n":
                return R.drawable.n04n;
            case "09n":
                return R.drawable.n09n;
            case "10n":
                return R.drawable.n10n;
            case "11n":
                return R.drawable.n11n;
            case "13n":
                return R.drawable.n13n;
            case "50n":
                return R.drawable.n50n;
        }
        return R.drawable.logo_rosberry;
    }

    public void setWeatherId(int weatherId) {
        mWeatherId = weatherId;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public long getSunset() {
        return mSunset;
    }

    public void setSunset(long sunset) {
        mSunset = sunset;
    }

    public long getSunrise() {
        return mSunrise;
    }

    public void setSunrise(long sunrise) {
        mSunrise = sunrise;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String condition) {
        mCondition = condition;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        mHumidity = humidity;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    public double getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(float minTemp) {
        mMinTemp = minTemp;
    }

    public double getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        mMaxTemp = maxTemp;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        mWindSpeed = windSpeed;
    }

    public double getWindDeg() {
        return mWindDeg;
    }

    public void setWindDeg(float windDeg) {
        mWindDeg = windDeg;
    }

    public int getCludPerc() {
        return mCludPerc;
    }

    public void setCludPerc(int cludPerc) {
        mCludPerc = cludPerc;
    }
}
