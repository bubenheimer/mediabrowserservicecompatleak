package com.bubenheimer.mbscl;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

public final class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MediaBrowserCompat mediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final MediaBrowserCompat.ConnectionCallback connectionCallback = new MyConnectionCallback();
        mediaBrowser = new MediaBrowserCompat(
                getApplicationContext(), new ComponentName(this, BrowserService.class), connectionCallback, null);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() called");
        mediaBrowser.disconnect();
        super.onStop();
    }

    private static class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected() called");
        }

        @Override
        public void onConnectionSuspended() {
            Log.d(TAG, "onConnectionSuspended() called");
        }

        @Override
        public void onConnectionFailed() {
            Log.d(TAG, "onConnectionFailed() called");
        }
    }
}
