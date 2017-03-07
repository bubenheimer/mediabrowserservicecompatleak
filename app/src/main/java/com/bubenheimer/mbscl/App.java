package com.bubenheimer.mbscl;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

public class App extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(5L, TimeUnit.SECONDS)
                .maxStoredHeapDumps(1)
                .buildAndInstall();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
