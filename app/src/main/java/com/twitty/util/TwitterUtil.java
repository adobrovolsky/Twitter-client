package com.twitty.util;

import com.twitty.AppPreferences;

import android.content.Context;

public class TwitterUtil {

    public static boolean isAuthenticated() {
        AppPreferences prefs = AppPreferences.getInstance();
        return !prefs.getAccessToken().isEmpty()
                && !prefs.getAccessTokenSecret().isEmpty();
    }

    public static void logout(Context context) {
        AppPreferences.getInstance().removeAccessToken();
    }

}
