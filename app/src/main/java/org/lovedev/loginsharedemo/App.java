package org.lovedev.loginsharedemo;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * @author Kevin
 * @data 2017/12/18
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
    }
}
