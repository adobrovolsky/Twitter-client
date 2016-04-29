package com.twitty.drafts;

import com.twitty.BuildConfig;
import com.twitty.store.DraftsLoader;
import com.twitty.store.DraftsRepository;
import com.twitty.store.entity.Draft;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

public class DraftsPresenter extends DraftsContract.Presenter
        implements LoaderManager.LoaderCallbacks<List<Draft>> {

    private static final int DRAFTS_QUERY = 1;
    private static final String TAG = DraftsPresenter.class.getSimpleName();

    private final DraftsLoader loader;
    private final DraftsRepository repository;
    private final LoaderManager loaderManager;

    @Inject public DraftsPresenter(DraftsLoader loader,
                           DraftsRepository repository, LoaderManager loaderManager) {
        this.loader = loader;
        this.repository = repository;
        this.loaderManager = loaderManager;
    }

    @Override public void loadDrafts(boolean pullToRefresh) {
        repository.refresh();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "::loadDrafts()");
        }
    }

    @Override void removeDraft(Draft draft) {
        repository.delete(draft);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "::removeDraft()");
        }
    }

    @Override public void attachView(DraftsContract.View view) {
        super.attachView(view);
        loaderManager.initLoader(DRAFTS_QUERY, null, this);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        loaderManager.destroyLoader(DRAFTS_QUERY);
    }

    @Override public Loader<List<Draft>> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override public void onLoadFinished(Loader<List<Draft>> loader, List<Draft> data) {
        if (isViewAttached()) {
            if (data == null) {
                getView().showError(null, false);
            } else {
                getView().setData(data);
                getView().showContent();
            }
        }
    }

    @Override public void onLoaderReset(Loader<List<Draft>> loader) {
        // NOP
    }
}
