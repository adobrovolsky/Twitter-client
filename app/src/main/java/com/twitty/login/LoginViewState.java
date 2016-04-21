package com.twitty.login;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class LoginViewState implements ViewState<LoginContract.LoginView> {

    final int STATE_SHOW_AUTH_FORM = 0;
    final int STATE_SHOW_OAUTH_DIALOG = 1;
    final int STATE_SHOW_LOADING = 2;
    final int STATE_SHOW_ERROR = 3;

    int state = STATE_SHOW_AUTH_FORM;

    @Override public void apply(LoginContract.LoginView view, boolean retained) {
        switch (state) {
            case STATE_SHOW_OAUTH_DIALOG:
                // TODO: restore request token
                view.showOAuthDialog("");
                break;
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;
            case STATE_SHOW_ERROR:
                view.showError();
                break;
            case STATE_SHOW_AUTH_FORM:
                view.showAuthForm();
                break;
        }
    }

    public void setShowAuthForm() {
        state = STATE_SHOW_AUTH_FORM;
    }

    public void setShowOauthDialog() {
        state = STATE_SHOW_OAUTH_DIALOG;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }
}
