package com.twitty;

import com.twitty.dagger.components.ApplicationComponent;
import com.twitty.dagger.components.DaggerApplicationComponent;
import com.twitty.dagger.components.DaggerUserComponent;
import com.twitty.dagger.components.UserComponent;
import com.twitty.dagger.modules.ApplicationModule;
import com.twitty.dagger.modules.NavigationModule;
import com.twitty.dagger.modules.RepositoryModule;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static Context context;
    private static ApplicationComponent appComponent;
    private static UserComponent userComponent;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        createAppComponent();
        createUserComponent();
    }

    private void createAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .navigationModule(new NavigationModule())
                .build();
    }

    public static void createUserComponent() {
        userComponent = DaggerUserComponent.builder()
                .applicationComponent(appComponent)
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static void releaseUserComponent() {
        userComponent = null;
    }

    public static Context getContext() {
        return context;
    }

    public static ApplicationComponent getAppComponent() {
        return appComponent;
    }

    public static UserComponent getUserComponent() {
        return userComponent;
    }
}
