package com.twitty.tweets;

import com.twitty.dagger.components.UserComponent;
import com.twitty.dagger.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = UserComponent.class)
public interface TweetsComponent {

    TweetsPresenter getPresenter();

    void inject(TweetsFragment fragment);
}
