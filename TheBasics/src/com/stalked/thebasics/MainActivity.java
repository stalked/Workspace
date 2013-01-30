package com.stalked.thebasics;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	
	MediaPlayer logoMusic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        logoMusic = MediaPlayer.create(MainActivity.this, R.raw.lost);
        logoMusic.start();
        Thread logoTimer = new Thread(){
        	public void run(){
        		try{
        			sleep(3000);
        			Intent menuIntent = new Intent("com.stalked.thebasics.MENU");
        			startActivity(menuIntent);
        		} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		finally{
        			finish();
        		}
        	}
        };
        logoTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		logoMusic.release();
	}
}
