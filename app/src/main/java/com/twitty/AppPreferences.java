package com.twitty;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static AppPreferences instance;
    private static Context context;
    private final SharedPreferences preferences;

    private static final String PREFS_NAME = "app_prefs";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCESS_TOKEN_SECRET = "access_token_secret";


    private AppPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (AppPreferences.class) {
                if (instance == null) {
                    instance = new AppPreferences(context);
                }
            }
        }
        return instance;
    }

    public void setAccessToken(final String accessTokenKey, final String accessTokenSecret) {
        preferences.edit()
                .putString(ACCESS_TOKEN, accessTokenKey)
                .putString(ACCESS_TOKEN_SECRET, accessTokenSecret)
                .apply();
    }

    public void removeAccessToken() {
        preferences.edit()
                .remove(ACCESS_TOKEN)
                .remove(ACCESS_TOKEN_SECRET)
                .apply();
    }

    public String getAccessToken() {
        return preferences.getString(ACCESS_TOKEN, "");
    }

    public String getAccessTokenSecret() {
        return preferences.getString(ACCESS_TOKEN_SECRET, "");
    }
}
