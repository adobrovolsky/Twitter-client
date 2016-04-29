package com.twitty.login;

import com.twitty.IntentStarter;
import com.twitty.R;
import com.twitty.util.Constants;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OAuthDialog extends DialogFragment {

    private static final String ARG_AUTHORIZATION_URL = "authorization_url";

    private WebView webView;
    private String authorizationUrl;

    public static OAuthDialog newInstance(String authorizationUrl) {
        final Bundle args = new Bundle();
        final OAuthDialog dialog = new OAuthDialog();
        args.putCharSequence(ARG_AUTHORIZATION_URL, authorizationUrl);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setArguments(args);
        return dialog;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorizationUrl = getArguments().getString(ARG_AUTHORIZATION_URL);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_auth, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authorizationUrl);
        webView.setWebViewClient(new AuthWebViewClient());
    }

    private class AuthWebViewClient extends WebViewClient {

        boolean mAuthComplete = false;

        @Override public void onPageFinished(WebView view, String url) {
            if (url.contains(Constants.OAUTH_VERIFIER) && !mAuthComplete) {
                mAuthComplete = true;
                final Uri uri = Uri.parse(url);
                final String oauthVerifier = uri.getQueryParameter(Constants.OAUTH_VERIFIER);
                dismiss();

                new IntentStarter().showLoginActivity(getActivity(), oauthVerifier);

            } else if (url.contains(Constants.DENIED)) {
                dismiss();
                Toast.makeText(getActivity(),
                        "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
