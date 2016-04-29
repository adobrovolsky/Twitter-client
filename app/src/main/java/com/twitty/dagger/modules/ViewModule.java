package com.twitty.dagger.modules;


import com.twitty.dagger.scopes.ActivityScope;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    private final LoaderManager loaderManager;

    public ViewModule(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    @Provides
    @ActivityScope
    public LoaderManager provideLoaderManager() {
        return loaderManager;
    }
}
