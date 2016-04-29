package com.twitty.login;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.twitty.MainApplication;
import com.twitty.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpViewStateActivity<LoginContract.LoginView, LoginPresenter>
        implements LoginContract.LoginView {

    public static final String EXTRA_VERIFIER = "com.twitty.login.verifier";
    private static final String TAG_DIALOG_OAUTH = "oauth_dialog";

    @Bind(R.id.authButton) Button authButton;
    @Bind(R.id.loadingView) View loadingView;
    @Bind(R.id.errorView) TextView errorView;

    LoginComponent component;

    @Override protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String verifier = (String) intent.getSerializableExtra(EXTRA_VERIFIER);
        if (verifier != null) {
            presenter.doLogin(verifier);
        }
    }

    @NonNull @Override public LoginPresenter createPresenter() {
        return component.getPresenter();
    }

    @OnClick(R.id.authButton) public void onLoginClicked() {
        presenter.doLogin();
    }

    @Override public ViewState<LoginContract.LoginView> createViewState() {
        return new LoginViewState();
    }

    @Override public void onNewViewStateInstance() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowAuthForm();
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    @Override public void showAuthForm() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowAuthForm();
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    @Override public void showError() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowError();
        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override public void showLoading() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowLoading();
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override public void showOAuthDialog(String authorizationUrl) {
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        OAuthDialog.newInstance(authorizationUrl)
                .show(getSupportFragmentManager(), TAG_DIALOG_OAUTH);
    }

    @Override public void loginSuccessful() {
        finish();
    }

    protected void injectDependencies() {
        component = DaggerLoginComponent.builder()
                .applicationComponent(MainApplication.getAppComponent())
                .build();
        component.inject(this);
    }
}