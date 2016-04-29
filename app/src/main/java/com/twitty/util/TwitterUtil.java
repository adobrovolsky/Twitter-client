package com.twitty.util;

import com.twitty.AppPreferences;
import com.twitty.MainApplication;

import android.content.Context;

import javax.inject.Inject;

public class TwitterUtil {

    private final AppPreferences prefs;

    @Inject public TwitterUtil(AppPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean isAuthenticated() {
        return !prefs.getAccessToken().isEmpty()
                && !prefs.getAccessTokenSecret().isEmpty();
    }

    public void logout(Context context) {
        prefs.removeAccessToken();
        MainApplication.releaseUserComponent();
    }

    public void login(String token, String tokenSecret) {
        prefs.setAccessToken(token, tokenSecret);
        MainApplication.createUserComponent();
    }

}
