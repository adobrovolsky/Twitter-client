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

public class DraftsPresenter extends DraftsContract.Presenter
        implements LoaderManager.LoaderCallbacks<List<Draft>> {

    private static final int DRAFTS_QUERY = 1;
    private static final String TAG = DraftsPresenter.class.getSimpleName();

    private DraftsLoader mLoader;
    private DraftsRepository mRepository;
    private LoaderManager mLoaderManager;

    public DraftsPresenter(DraftsLoader loader,
                           DraftsRepository repository, LoaderManager loaderManager) {
        mLoader = loader;
        mRepository = repository;
        mLoaderManager = loaderManager;
    }

    @Override public void loadDrafts(boolean pullToRefresh) {
        mRepository.refresh();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "::loadDrafts()");
        }
    }

    @Override void removeDraft(Draft draft) {
        mRepository.delete(draft);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "::removeDraft()");
        }
    }

    @Override public void attachView(DraftsContract.View view) {
        super.attachView(view);
        mLoaderManager.initLoader(DRAFTS_QUERY, null, this);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        mLoaderManager.destroyLoader(DRAFTS_QUERY);
    }

    @Override public Loader<List<Draft>> onCreateLoader(int id, Bundle args) {
        return mLoader;
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
