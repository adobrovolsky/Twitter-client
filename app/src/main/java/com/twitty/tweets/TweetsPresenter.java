package com.twitty.tweets;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

public class TweetsPresenter extends MvpBasePresenter<TweetContract.View>
        implements TweetContract.Presenter {

    private AsyncTwitter twitter;

    @Inject public TweetsPresenter(AsyncTwitter twitter) {
       this.twitter = twitter;
    }

    public void loadHomeTimeLine(final boolean pullToRefresh) {
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
                        getView().showError(te, pullToRefresh);
                    }
                }
            }
        });
        twitter.getHomeTimeline();
    }

    @Override public void loadFavoriteTweets(final boolean pullToRefresh) {
        twitter.addListener(new TwitterAdapter() {
            @Override public void gotFavorites(ResponseList<Status> statuses) {
                if (isViewAttached()) {
                    getView().setData(statuses);
                    getView().showContent();
                }
            }

            @Override public void onException(TwitterException te, TwitterMethod method) {
                if(isViewAttached()) {
                    getView().showError(te, pullToRefresh);
                }
            }
        });
        twitter.getFavorites();
    }

    @Override public void retweet(Status status) {
        twitter.retweetStatus(status.getId());
    }

    @Override public void destroyRetweet(Status status) {
        long statusId = status.getId();
        try {
            if (status.getUser().getId() != twitter.getId()) {
                statusId = status.getCurrentUserRetweetId();
            }
            twitter.destroyStatus(status.getId());
        } catch (TwitterException e) {
           if (isViewAttached()) {
               getView().showError(e, false);
           }
        }
    }

    @Override public void reply(final StatusUpdate statusUpdate) {
        twitter.updateStatus(statusUpdate);
    }

    @Override public void createFavoriteTweet(long statusId) {
        twitter.createFavorite(statusId);
    }

    @Override public void destroyFavoriteTweet(long statusId) {
        twitter.destroyFavorite(statusId);
    }
}