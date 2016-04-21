package com.twitty.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoginContract {

    interface LoginView extends MvpView {

        public void showAuthForm();

        public void showError();

        public void showLoading();

        public void showOAuthDialog(String requestTokenUrl);

        public void loginSuccessful();
    }
}
