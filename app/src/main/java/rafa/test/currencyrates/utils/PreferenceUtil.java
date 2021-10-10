package rafa.test.currencyrates.utils;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.util.Calendar;


public class PreferenceUtil {
    private static final String PREFS_FILE_NAME = "CURRENCY_RATE_PREF_FILE_NAME";


    public static void saveRequestTime(long time, Context context) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreference.edit();
        prefsEditor.putLong("lastRequestTime", time);
        prefsEditor.apply();
    }

    public static long getLastRequestTime(Context context) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        return sharedPreference.getLong("lastRequestTime", 0);
    }

}
