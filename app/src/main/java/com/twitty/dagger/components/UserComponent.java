package com.twitty.dagger.components;

import com.twitty.dagger.modules.RepositoryModule;
import com.twitty.dagger.scopes.UserScope;
import com.twitty.store.DraftsLoader;
import com.twitty.store.DraftsRepository;

import dagger.Component;
import twitter4j.AsyncTwitter;

@UserScope
@Component(dependencies = ApplicationComponent.class, modules = RepositoryModule.class)
public interface UserComponent {

    AsyncTwitter getAsyncTwitter();

    DraftsLoader getDraftsLoader();

    DraftsRepository getDraftsRepository();
}
