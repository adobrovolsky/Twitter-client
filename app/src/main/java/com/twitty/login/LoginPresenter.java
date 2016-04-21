package com.twitty.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.twitty.AppPreferences;
import com.twitty.BuildConfig;
import com.twitty.Constants;
import com.twitty.util.TwitterUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class LoginPresenter extends MvpBasePresenter<LoginContract.LoginView> {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private final Twitter mTwitter;
    private final AppPreferences mPrefs;

    public LoginPresenter() {
        mPrefs = AppPreferences.getInstance();
        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    }

    public void doLogin() {
        if (TwitterUtil.isAuthenticated()) {
            if (isViewAttached()) {
                getView().loginSuccessful();
            }
        } else {
            new GetRequestTokenTask().execute();
        }
    }

    public void doLogin(String verifier) {
        new GetAccessTokenTask().execute(verifier);
    }

    private class GetRequestTokenTask extends AsyncTask<Void, Void, RequestToken> {

        @Override protected void onPreExecute() {
            if (isViewAttached()) {
                getView().showLoading();
            }
        }

        @Override protected RequestToken doInBackground(Void ... params) {
            try {
                RequestToken requestToken = mTwitter.getOAuthRequestToken();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "GetRequestTokenTask::doInBackground");
                    Log.d(TAG, "Request token = " + requestToken.getToken());
                    Log.d(TAG, "Request token secret = " + requestToken.getTokenSecret());
                }
                return requestToken;
            } catch (TwitterException e) {
                return null;
            }
        }

        @Override protected void onPostExecute(RequestToken requestToken) {
            if (isViewAttached()) {
                if (requestToken != null) {
                    getView().showOAuthDialog(requestToken.getAuthorizationURL());
                } else {
                    getView().showError();
                }
            }
        }
    }

    private class GetAccessTokenTask extends AsyncTask<String, Void, AccessToken> {

        @Override protected void onPreExecute() {
            if (isViewAttached()) {
                getView().showLoading();
            }
        }

        @Override protected AccessToken doInBackground(String ... params) {
            try {
                final String verifier = params[0];
                AccessToken accessToken = mTwitter.getOAuthAccessToken(verifier);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "GetAccessTokenTask::doInBackground");
                    Log.d(TAG, "Access token = " + accessToken.getToken());
                    Log.d(TAG, "Access token secret = " + accessToken.getTokenSecret());
                }
                return accessToken;
            } catch (TwitterException e) {
                return null;
            }
        }

        @Override protected void onPostExecute(AccessToken accessToken) {
            if (accessToken != null) {
                mPrefs.setAccessToken(accessToken.getToken(), accessToken.getTokenSecret());
                if (isViewAttached()) {
                    getView().loginSuccessful();
                }
            } else {
                if (isViewAttached()) {
                    getView().showError();
                }
            }
        }
    }
}

