package com.twitty.tweets;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.twitty.AppPreferences;
import com.twitty.Constants;

import android.content.Context;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;

public class TweetsPresenter extends MvpBasePresenter<TweetsView> {

    private AsyncTwitter mTwitter;
    private final AppPreferences mPrefs;

    public TweetsPresenter() {
        mPrefs = AppPreferences.getInstance();
        initTwitter();
    }

    private void initTwitter() {
        final String token = mPrefs.getAccessToken();
        final String secretToken = mPrefs.getAccessTokenSecret();

        AccessToken accessToken = new AccessToken(token, secretToken);
        mTwitter = new AsyncTwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        mTwitter.setOAuthAccessToken(accessToken);
    }

    public void loadTweets(boolean pullToRefresh) {
        mTwitter.addListener(new TwitterAdapter() {

            @Override public void gotHomeTimeline(ResponseList<Status> statuses) {
                if (isViewAttached()) {
                    getView().setData(statuses);
                    getView().showContent();
                }
            }

            @Override public void onException(TwitterException te, TwitterMethod method) {
                if (method == TwitterMethod.HOME_TIMELINE) {
                    if (isViewAttached()) {
                        getView().showError(te, false);
                    }
                }
            }
        });
        mTwitter.getHomeTimeline();
    }

    public void retweet(long statusId) {
        mTwitter.retweetStatus(statusId);
    }
}