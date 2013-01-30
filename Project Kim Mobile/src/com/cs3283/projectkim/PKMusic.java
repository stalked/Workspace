package com.cs3283.projectkim;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PKMusic extends Service{
	
	public static boolean isRunning = false;
	MediaPlayer player;

	public PKMusic() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setMusicOptions(this, PKEngine.LOOP_BACKGROUND_MUSIC, PKEngine.R_VOLUME, PKEngine.L_VOLUME, PKEngine.SPLASH_SCREEN_MUSIC);
	}
	
	private void setMusicOptions(Context context, boolean isLooped,
			int rVolume, int lVolume, int soundFile) {
		player = MediaPlayer.create(context, soundFile);
		player.setLooping(isLooped);
		player.setVolume(rVolume, lVolume);
	}

	public int onStartCommand(Intent intent, int flags, int startId){
		try{
			player.start();
			isRunning = true;
		} catch(Exception e){
			isRunning = false;
			player.stop();
		}
		return 1;
	}
	
	public void onStart(Intent intent, int startId){
		
	}
	
	public void onStop(){
		isRunning = false;
	}
	
	public IBinder onUnBind(Intent arg0){
		return null;
	}
	
	public void onPause(){
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		player.stop();
		player.release();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		player.stop();
	}

}
