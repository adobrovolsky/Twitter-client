package com.twitty.dagger.modules;

import com.twitty.AppPreferences;
import com.twitty.util.Constants;
import com.twitty.util.TwitterUtil;

import org.jetbrains.annotations.NotNull;

import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Module
public class ApplicationModule {

    private static Context appContext;

    public ApplicationModule(@NotNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public TwitterUtil provideTwitterUtil(@NotNull AppPreferences prefs) {
        return new TwitterUtil(prefs);
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public LayoutInflater provideLayoutInflater(@NotNull Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    @Singleton
    public AppPreferences provideAppPreferences(@NotNull Context context) {
        return AppPreferences.getInstance(context);
    }

    @Provides
    @Singleton
    public Twitter provideTwitter() {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        return twitter;
    }
}
