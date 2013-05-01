package com.example.triggerbusters;

import android.os.SystemClock;

public class GameThread implements Runnable{
	
	private long mLastTime;
    
    private ObjectManager mGameRoot;
    private GameRenderer mRenderer;
    private Object mPauseLock;
    private boolean mFinished;
    private boolean mPaused = false;
    private int mProfileFrames;
    private long mProfileTime;
    
    private static final float PROFILE_REPORT_DELAY = 3.0f;
    
    public GameThread(GameRenderer renderer) {
        mLastTime = SystemClock.uptimeMillis();
        mRenderer = renderer;
        mPauseLock = new Object();
        mFinished = false;
        mPaused = false;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopGame() {
    	synchronized (mPauseLock) {
            mPaused = false;
            mFinished = true;
            mPauseLock.notifyAll();
    	}
    }
    
    public void pauseGame() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    public void resumeGame() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
    
    public boolean getPaused() {
        return mPaused;
    }

    public void setGameRoot(ObjectManager gameRoot) {
        mGameRoot = gameRoot;
    }

}
