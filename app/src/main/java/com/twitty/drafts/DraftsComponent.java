package com.twitty.drafts;

import com.twitty.dagger.components.UserComponent;
import com.twitty.dagger.modules.ViewModule;
import com.twitty.dagger.scopes.ActivityScope;

import android.support.v4.app.LoaderManager;

import dagger.Component;

@ActivityScope
@Component(dependencies = UserComponent.class, modules = ViewModule.class)
public interface DraftsComponent {

    public DraftsPresenter getPresenter();

    public LoaderManager getLoaderManager();

    public void inject(DraftsActivity activity);

}
