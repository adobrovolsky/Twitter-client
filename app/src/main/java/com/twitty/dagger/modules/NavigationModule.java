package com.twitty.dagger.modules;

import com.twitty.IntentStarter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @Singleton
    @Provides
    public IntentStarter getIntentStarter() {
        return new IntentStarter();
    }
}
