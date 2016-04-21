package com.twitty.base;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

public interface AuthView<M> extends MvpLceView<M> {
    public void showAuthentication();
}
