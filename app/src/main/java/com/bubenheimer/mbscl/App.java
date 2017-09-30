package com.bubenheimer.mbscl;

import android.app.Application;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.GcTrigger;
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

        final ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                //TODO https://code.google.com/p/android/issues/detail?id=237834
                .instanceField("android.service.media.MediaBrowserService$ServiceBinder", "this$0")
                .build();
        final GcTrigger gcTrigger = new GcTrigger() {
            @Override
            public void runGc() {
                try {
                    final Runtime runtime = Runtime.getRuntime();
                    for (int i = 0; i < 2; ++i) {
                        runtime.gc();
                        Thread.sleep(500L);
                        runtime.runFinalization();
                    }
                } catch (final InterruptedException e) {
                    throw new AssertionError();
                }
            }
        };
        refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(1L, TimeUnit.SECONDS)
                .maxStoredHeapDumps(1)
                .excludedRefs(excludedRefs)
//                .gcTrigger(gcTrigger)
                .buildAndInstall();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
