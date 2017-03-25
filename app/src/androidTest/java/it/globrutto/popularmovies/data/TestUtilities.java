package it.globrutto.popularmovies.data;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import it.globrutto.popularmovies.utils.PollingCheck;

/**
 * Created by giuseppelobrutto on 24/03/17.
 */

public class TestUtilities {

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    static class TestContentObserver extends ContentObserver {

        final HandlerThread mHt;

        boolean mContentChanged = false;

        private TestContentObserver(HandlerThread handlerThread) {
            super(new Handler(handlerThread.getLooper()));
            mHt = handlerThread;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        @Override
        public void onChange(boolean selfChanged) {
            onChange(selfChanged, null);
        }

        @Override
        public void onChange(boolean selfChanged, Uri uri) {
            mContentChanged = true;
        }

        void waitForNotificationOrFail() {

            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHt.quit();
        }
    }
}
