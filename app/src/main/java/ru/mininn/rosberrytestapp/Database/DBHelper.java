package ru.mininn.rosberrytestapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.mininn.rosberrytestapp.Models.Weather;

/**
 * SQLite helper. No comments
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "rosberryweather";

    private static final String TABLE_WEATHER = "weatherhistory";

    private static final String KEY_ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CCONDITION = "currentcondition";
    private static final String KEY_CDESCRIPTION = "conditiondescription";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_PRESSURE = "pressure";
    private static final String KEY_WINDSPEED = "winddpeed";
    private static final String KEY_WINDDEG = "winddeg";
    private static final String KEY_ICON = "icon";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createWeatherTable = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_CCONDITION + " TEXT,"
                + KEY_CDESCRIPTION + " TEXT,"
                + KEY_TEMPERATURE + " REAL,"
                + KEY_HUMIDITY + " REAL,"
                + KEY_PRESSURE + " REAL,"
                + KEY_WINDSPEED + " REAL,"
                + KEY_WINDDEG + " REAL,"
                + KEY_ICON + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(createWeatherTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(sqLiteDatabase);
    }

    public void addWeather(Weather weather,Context context) {

        int isUpdate = updateWeather(weather,context);

        if(isUpdate == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            SharedPreferences prefs = context.getSharedPreferences("Prefs", context.MODE_PRIVATE);
            String user = prefs.getString("CurrentUser",null);
            ContentValues values = new ContentValues();
            values.put(KEY_CITY, weather.getCity());
            values.put(KEY_USER, user);
            values.put(KEY_COUNTRY, weather.getCountry());
            values.put(KEY_CCONDITION, weather.getCondition());
            values.put(KEY_CDESCRIPTION, weather.getDescription());
            values.put(KEY_TEMPERATURE, weather.getTemp());
            values.put(KEY_HUMIDITY, weather.getHumidity());
            values.put(KEY_PRESSURE, weather.getPressure());
            values.put(KEY_WINDSPEED, weather.getWindSpeed());
            values.put(KEY_WINDDEG, weather.getWindDeg());
            values.put(KEY_ICON, weather.getIcon());

            db.insert(TABLE_WEATHER, null, values);
            db.close();
        }
    }

    public int updateWeather(Weather weather,Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        SharedPreferences prefs = context.getSharedPreferences("Prefs", context.MODE_PRIVATE);
        String user = prefs.getString("CurrentUser",null);
        ContentValues values = new ContentValues();
        values.put(KEY_CITY, weather.getCity());
        values.put(KEY_USER, user);
        values.put(KEY_COUNTRY, weather.getCountry());
        values.put(KEY_CCONDITION, weather.getCondition());
        values.put(KEY_CDESCRIPTION, weather.getDescription());
        values.put(KEY_TEMPERATURE, weather.getTemp());
        values.put(KEY_HUMIDITY, weather.getHumidity());
        values.put(KEY_PRESSURE, weather.getPressure());
        values.put(KEY_WINDSPEED, weather.getWindSpeed());
        values.put(KEY_WINDDEG, weather.getWindDeg());
        values.put(KEY_ICON, weather.getIcon());

        return db.update(TABLE_WEATHER, values, KEY_CITY + " = ?",
                new String[] { weather.getCity() });
    }

    public List<Weather> getAllWeather(Context context) {
        List<Weather> weatherList = new ArrayList<Weather>();
        SharedPreferences prefs = context.getSharedPreferences("Prefs", context.MODE_PRIVATE);
        String user = prefs.getString("CurrentUser",null);

        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER + " WHERE " + KEY_USER + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        if (cursor.moveToFirst()) {
            do {
                Weather weather = new Weather();
                weather.setWeatherId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                weather.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
                weather.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
                weather.setCondition(cursor.getString(cursor.getColumnIndex(KEY_CCONDITION)));
                weather.setDescription(cursor.getString(cursor.getColumnIndex(KEY_CDESCRIPTION)));
                weather.setTemp((float)(cursor.getDouble(cursor.getColumnIndex(KEY_TEMPERATURE))));
                weather.setHumidity((float)(cursor.getDouble(cursor.getColumnIndex(KEY_HUMIDITY))));
                weather.setPressure((float)(cursor.getDouble(cursor.getColumnIndex(KEY_PRESSURE))));
                weather.setWindSpeed((float)(cursor.getDouble(cursor.getColumnIndex(KEY_WINDSPEED))));
                weather.setWindDeg((float)(cursor.getDouble(cursor.getColumnIndex(KEY_WINDDEG))));
                weather.setWindDeg((float)(cursor.getDouble(cursor.getColumnIndex(KEY_WINDDEG))));
                weather.setIcon((cursor.getString(cursor.getColumnIndex(KEY_ICON))));
                weatherList.add(weather);
            } while (cursor.moveToNext());
        }

        return weatherList;
    }
}
