package com.example.triggerbusters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HighScoreActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.high_score);
		update();
	}

	public void onMainMenuScreenClick(View v) {
		Intent i = new Intent(this, MainMenuActivity.class);
		startActivity(i);
	}
	
	/*
	 * Initial call to update score that extracts high score from last game
	 * and adds new high score list to view
	 */
	public void update(){

		//Checking current score and updating high score accordingly
    	Intent game = getIntent();
    	int currScore = (int) game.getLongExtra("Milliseconds", 0);
    	SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    	updateScore(currScore, prefs);
    	
    	//Updating views
    	TextView view = (TextView) findViewById(R.id.counter);
    	view.setText(Integer.toString(prefs.getInt("key0", 0)));
    	view = (TextView) findViewById(R.id.counter1);
    	view.setText(Integer.toString(prefs.getInt("key1", 0)));
    	view = (TextView) findViewById(R.id.counter2);
    	view.setText(Integer.toString(prefs.getInt("key2", 0)));
    	view = (TextView) findViewById(R.id.counter3);
    	view.setText(Integer.toString(prefs.getInt("key3", 0)));
    	view = (TextView) findViewById(R.id.counter4);
    	view.setText(Integer.toString(prefs.getInt("key4", 0)));
    	view = (TextView) findViewById(R.id.counter5);
    	view.setText(Integer.toString(prefs.getInt("key5", 0)));
    	view = (TextView) findViewById(R.id.counter6);
    	view.setText(Integer.toString(prefs.getInt("key6", 0)));
    	view = (TextView) findViewById(R.id.counter7);
    	view.setText(Integer.toString(prefs.getInt("key7", 0)));
    	view = (TextView) findViewById(R.id.counter8);
    	view.setText(Integer.toString(prefs.getInt("key8", 0)));
    	view = (TextView) findViewById(R.id.counter9);
    	view.setText(Integer.toString(prefs.getInt("key9", 0)));
	}
	

	/*
	 * Updates the High score list in memory
	 */
	public void updateScore(int currScore, SharedPreferences prefs) {
		Editor editor = prefs.edit();
    	for(int i = 0; i < 10; i++) {
    		String key = "key" + i;
    		int score = prefs.getInt(key, 0); //0 is the default value
    		if(score < currScore) {
    			editor.putInt(key, currScore);
    			if(currScore == 0) {
    				break;
    			}
    			currScore = score;
    		}
    	}
    	
    	if(!editor.commit()) {
    		Log.d("Update", "YOU SHALL NOT PASS!");
    	}
	}
	
	/*
	 * Resets the sharedPrefences settings to zero
	 */
	public void resetScores (SharedPreferences prefs) {
		Editor editor = prefs.edit();
	    editor.clear();
	    editor.commit();
	}
}
