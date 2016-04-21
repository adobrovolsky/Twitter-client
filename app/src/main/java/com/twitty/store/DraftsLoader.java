package com.twitty.store;

import com.twitty.store.entity.Draft;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class DraftsLoader extends AsyncTaskLoader<List<Draft>>
        implements DraftsRepository.DraftsRepositoryObserver {

    private final DraftsRepository repository;

    public DraftsLoader(Context context, DraftsRepository repository) {
        super(context);
        this.repository = repository;
    }

    @Override public List<Draft> loadInBackground() {
        return repository.findAll();
    }

    @Override protected void onStartLoading() {
        super.onStartLoading();
        repository.addObserver(this);
        forceLoad();
    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override protected void onReset() {
        onStopLoading();
        repository.removeObserver(this);
    }

    @Override public void onDraftsChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
