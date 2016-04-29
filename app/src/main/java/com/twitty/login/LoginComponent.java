package com.twitty.login;

import com.twitty.dagger.components.ApplicationComponent;
import com.twitty.dagger.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface LoginComponent {

    LoginPresenter getPresenter();

    void inject(LoginActivity activity);
}
