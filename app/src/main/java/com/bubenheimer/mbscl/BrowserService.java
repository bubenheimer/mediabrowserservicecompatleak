package com.bubenheimer.mbscl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import java.util.List;

public final class BrowserService extends MediaBrowserServiceCompat {
    private static final String TAG = BrowserService.class.getSimpleName();

    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, TAG);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        mediaSession.release();
        ((App) getApplication()).getRefWatcher().watch(this);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull final String clientPackageName, final int clientUid,
                                 @Nullable final Bundle rootHints) {
        return new BrowserRoot(getString(R.string.app_name), null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentId,
                               @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }
}
