package com.twitty.base;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.delegate.BaseMvpViewStateDelegateCallback;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpViewStateDelegateImpl;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public abstract class MvpViewStateDialogFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends DialogFragment implements BaseMvpViewStateDelegateCallback<V, P> {

    protected FragmentMvpViewStateDelegateImpl<V, P> delegate = new FragmentMvpViewStateDelegateImpl<>(this);
    protected P presenter;
    protected ViewState<V> viewState;
    protected boolean retainInstance;
    protected boolean restoringViewState;

    //
    // Delegate callback
    //

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate.onCreate(savedInstanceState);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        delegate.onDestroy();
    }

    @Override public void onPause() {
        super.onPause();
        delegate.onPause();
    }

    @Override public void onResume() {
        super.onResume();
        delegate.onResume();
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delegate.onViewCreated(view, savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        delegate.onStart();
    }

    @Override public void onStop() {
        super.onStop();
        delegate.onStop();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        delegate.onAttach(activity);
    }

    @Override public void onDetach() {
        super.onDetach();
        delegate.onDetach();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        delegate.onActivityCreated(savedInstanceState);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        delegate.onSaveInstanceState(outState);
    }

    //
    // End delegates
    //

    @Override public ViewState<V> getViewState() {
        return viewState;
    }

    @Override public void setViewState(ViewState<V> viewState) {
        this.viewState = viewState;
    }

    @Override public void setRestoringViewState(boolean restoringViewState) {
        this.restoringViewState = restoringViewState;
    }

    @Override public boolean isRestoringViewState() {
        return restoringViewState;
    }

    @Override public P getPresenter() {
        return presenter;
    }

    @Override public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override public V getMvpView() {
        return null;
    }

    @Override public boolean isRetainInstance() {
        return retainInstance;
    }

    @Override public boolean shouldInstanceBeRetained() {
        return retainInstance && getActivity().isChangingConfigurations();
    }
}
