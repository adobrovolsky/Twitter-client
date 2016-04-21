package com.twitty;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static AppPreferences sInstance;
    private final Context mContext;
    private final SharedPreferences mPreferences;

    private static final String PREFS_NAME = "appPrefs";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCESS_TOKEN_SECRET = "access_token_secret";

    private AppPreferences() {
        mContext = MainApplication.getContext();
        mPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance() {
        if (sInstance == null) {
            synchronized (AppPreferences.class) {
                if (sInstance == null) {
                    sInstance = new AppPreferences();
                }
            }
        }
        return sInstance;
    }

    public void setAccessToken(final String accessTokenKey, final String accessTokenSecret) {
        mPreferences.edit()
                .putString(ACCESS_TOKEN, accessTokenKey)
                .putString(ACCESS_TOKEN_SECRET, accessTokenSecret)
                .apply();
    }

    public void removeAccessToken() {
        mPreferences.edit()
                .remove(ACCESS_TOKEN)
                .remove(ACCESS_TOKEN_SECRET)
                .apply();
    }

    public String getAccessToken() {
        return mPreferences.getString(ACCESS_TOKEN, "");
    }

    public String getAccessTokenSecret() {
        return mPreferences.getString(ACCESS_TOKEN_SECRET, "");
    }
}
