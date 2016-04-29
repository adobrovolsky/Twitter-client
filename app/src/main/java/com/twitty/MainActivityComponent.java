package com.twitty;

import com.twitty.dagger.components.ApplicationComponent;
import com.twitty.dagger.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface MainActivityComponent {

    public void inject(MainActivity activity);
}
