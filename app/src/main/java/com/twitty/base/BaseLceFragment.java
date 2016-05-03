package com.twitty.base;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.twitty.IntentStarter;

import android.view.View;

public abstract class BaseLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceFragment<CV, M, V, P> implements AuthView<M> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override public void showAuthentication() {
        new IntentStarter().showAuthentication(getActivity());
    }
}