package com.twitty.tweets;

import com.twitty.base.AuthView;

import twitter4j.ResponseList;
import twitter4j.Status;

public interface TweetsView extends AuthView<ResponseList<Status>> {
}
