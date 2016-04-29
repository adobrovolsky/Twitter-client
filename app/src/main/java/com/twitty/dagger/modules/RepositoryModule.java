package com.twitty.dagger.modules;

import com.twitty.AppPreferences;
import com.twitty.dagger.scopes.UserScope;
import com.twitty.store.DatabaseHelper;
import com.twitty.store.DraftsLoader;
import com.twitty.store.DraftsRepository;
import com.twitty.util.Constants;

import org.jetbrains.annotations.NotNull;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.auth.AccessToken;

@Module
public class RepositoryModule {

    @Provides
    @UserScope
    DatabaseHelper provideDatabaseHelper() {
        return new DatabaseHelper();
    }

    @Provides
    @UserScope
    public DraftsRepository provideDraftsRepository(@NotNull DatabaseHelper dbHelper) {
        return new DraftsRepository(dbHelper);
    }

    @Provides
    @UserScope
    public DraftsLoader provideDraftsLoader(Context context, DraftsRepository repository) {
        return new DraftsLoader(context, repository);
    }

    @Provides
    @UserScope
    public AsyncTwitter provideAsyncTwitter(@NotNull AppPreferences prefs) {
        final AsyncTwitter twitter = new AsyncTwitterFactory().getInstance();
        final AccessToken accessToken = new AccessToken(prefs.getAccessToken(), prefs.getAccessTokenSecret());
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }
}
