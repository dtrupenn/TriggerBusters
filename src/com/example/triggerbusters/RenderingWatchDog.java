package com.example.triggerbusters;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import android.util.Log;

public class RenderingWatchDog implements Runnable {
	/** panic if watch-dog is not reset over this amount of time */
    private static final long DEFAULT_TIMEOUT_IN_MSECS = 10 * 1000;
    private static final String TAG = "RenderingWatchDog";
    private Thread mThread;
    private Semaphore mSemaphore;
    private volatile boolean mStopRequested;
    private final long mTimeoutInMilliSecs;

    public RenderingWatchDog() {
        this(DEFAULT_TIMEOUT_IN_MSECS);
    }

    public RenderingWatchDog(long timeoutInMilliSecs) {
        mTimeoutInMilliSecs = timeoutInMilliSecs;
    }

    /** start watch-dog */
    public void start() {
        Log.i(TAG, "start");
        mStopRequested = false;
        mSemaphore = new Semaphore(0);
        mThread = new Thread(this);
        mThread.start();
    }

    /** stop watch-dog */
    public void stop() {
        Log.i(TAG, "stop");
        if (mThread == null) {
            return; // already finished
        }
        mStopRequested = true;
        mSemaphore.release();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // ignore
        }
        mThread = null;
        mSemaphore = null;
    }

    /** resets watch-dog, thus prevent it from panic */
    public void reset() {
        if (!mStopRequested) { // stop requested, but rendering still on-going
            mSemaphore.release();
        }
    }

    @Override
    public void run() {
        while (!mStopRequested) {
            try {
                Assert.assertTrue("Watchdog timed-out",
                        mSemaphore.tryAcquire(mTimeoutInMilliSecs, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                // this thread will not be interrupted,
                // but if it happens, just check the exit condition.
            }
        }
    }
}
