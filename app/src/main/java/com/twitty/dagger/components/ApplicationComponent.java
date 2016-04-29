package com.twitty.dagger.components;

import com.twitty.AppPreferences;
import com.twitty.IntentStarter;
import com.twitty.dagger.modules.ApplicationModule;
import com.twitty.dagger.modules.NavigationModule;
import com.twitty.util.TwitterUtil;

import android.content.Context;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import twitter4j.Twitter;

@Singleton
@Component(modules = {ApplicationModule.class, NavigationModule.class})
public interface ApplicationComponent {

    Context getContext();

    LayoutInflater getLayoutInflater();

    AppPreferences getPreferences();

    IntentStarter getIntentStarter();

    Twitter getTwitter();

    TwitterUtil getTwitterUtil();
}
