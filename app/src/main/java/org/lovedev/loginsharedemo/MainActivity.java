package org.lovedev.loginsharedemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1003;
    private TwitterLoginButton mLoginButton;
    private TwitterAuthClient authClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        findViewById(R.id.login_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithTwitter();
            }
        });
        findViewById(R.id.share_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithTwitter();
            }
        });


        mLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i("TwitterLoginButton", "success");
                // Do something with result, which provides a TwitterSession for making API calls
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("TwitterLoginButton", "failure");
                // Do something on failure
            }
        });
    }

    private void shareWithTwitter() {
        Glide.with(this).asFile().load("http://o9fbxdmjk.bkt.clouddn.com/headImg").into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {

                File file = new File(getFilesDir(), "share.png");
                boolean success = resource.renameTo(file);
                if (success) {
                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".file_provider", file);
                    TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                            .text("just setting up my Twitter Kit.")
                            .image(imageUri);
                    builder.show();
                }
            }
        });

    }

    private void loginWithTwitter() {
        getTwitterAuthClient().authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i("Button", "success");

            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("Button", "failure");

            }
        });
    }


    TwitterAuthClient getTwitterAuthClient() {
        if (authClient == null) {
            synchronized (TwitterLoginButton.class) {
                if (authClient == null) {
                    authClient = new TwitterAuthClient();
                }
            }
        }
        return authClient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getTwitterAuthClient().getRequestCode()) {
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }
}
