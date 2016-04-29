package com.twitty.util;

public class Constants {

    public static final String CONSUMER_KEY = "cjcfF7MYtdEdWbT9lZucY32Ys";
    public static final String CONSUMER_SECRET= "eJuy08OecPhqCbJcjYw5Qdb78X7ALNozEdGntGIWJfFsMT1nzN";

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    public static final String OAUTH_VERIFIER = "oauth_verifier";
    public static final String DENIED = "denied";

    public static final String CALLBACK_SCHEME = "x-latify-oauth-twitter";
    public static final String CALLBACK_URL = CALLBACK_SCHEME + "://callback";
}
