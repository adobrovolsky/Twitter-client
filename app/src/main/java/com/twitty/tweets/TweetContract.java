package com.twitty.tweets;

import com.twitty.base.AuthView;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public interface TweetContract {

    interface View extends AuthView<ResponseList<Status>> {

    }

    interface Presenter {

        void loadFavoriteTweets(boolean pullToRefresh);

        void loadHomeTimeLine(boolean pullToRefresh);

        void retweet(Status status);

        void destroyRetweet(Status status);

        void reply(StatusUpdate statusUpdate);

        void createFavoriteTweet(long statusId);

        void destroyFavoriteTweet(long statusId);
    }
}
