package com.twitty.tweets;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

public class TweetsPresenter extends MvpBasePresenter<TweetsView> {

    private AsyncTwitter twitter;

    @Inject public TweetsPresenter(AsyncTwitter twitter) {
       this.twitter = twitter;
    }

    public void loadTweets(boolean pullToRefresh) {
        twitter.addListener(new TwitterAdapter() {

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
        twitter.getHomeTimeline();
    }

    public void retweet(long statusId) {
        twitter.retweetStatus(statusId);
    }
}