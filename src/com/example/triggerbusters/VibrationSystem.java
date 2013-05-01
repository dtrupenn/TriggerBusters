package com.example.triggerbusters;

import android.content.Context;
import android.os.Vibrator;

public class VibrationSystem extends BaseObject {

	public VibrationSystem() {
        super();
    }
    
    @Override
    public void reset() {
    }
    
    public void vibrate(float seconds) {
    	// Gotta figure out the whole contextParameters thing.
        /*ContextParameters params = sSystemRegistry.contextParameters;
        if (params != null && params.context != null) {
            Vibrator vibrator = (Vibrator)params.context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate((int)(seconds * 1000));
            }
        }*/
    }
}
