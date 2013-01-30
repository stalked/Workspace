package com.cs3283.projectkim;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class PKEngine {
	/* Constants that will be used in the game */
	public static final int GAME_THREAD_DELAY = 1000;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);
	public static final int MENU_BUTTON_ALPHA = 0;
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true;

	public static final int SPLASH_SCREEN_MUSIC = R.raw.loop;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;
	
	public static int[][] map = new int[3][3];
	public static final int NO_MAP = R.drawable.nomap;
	public static final int GRID = R.drawable.grid;
	public static final float GRID_COLUMNSHIFT = 0.5f;
	public static final float GRID_ROWSHIFT = -0.75f;
	public static final int CHEST = R.drawable.chest;
	public static final int PLAYER_INDICATOR = R.drawable.playerloc;
	public static final long TIME_LIMIT = 99;
	public static boolean INIT_GAME_TIMER = false;
	
	public static final int QUIZ_QUESTION = R.drawable.quiz;
	public static boolean GET_QUESTION = false;
	public static boolean INIT_QUIZ_TIMER = false;
	
	public static int CHEST_STATE = 0; // [0] - Chest Closed; [1] - Chest Opened; [2] - Mini Game Start
	
	public static final int PLAYER_ID = 1; 
	public static int PLAYER_SCORE = 0;
	
	public static GameClient client;
	public static Context context;
	public static Thread musicThread;
	
	/* Kill game and exit */
	public boolean onExit(View v){
		try{
			Intent bgmusic = new Intent(context, PKMusic.class);
			context.stopService(bgmusic);
			return true;
		}catch(Exception e){
			Intent bgmusic = new Intent(context, PKMusic.class);
			context.stopService(bgmusic);
			return false;
		}
	}

	public static void addMap() {
		map[0][0] = R.drawable.map_one;
		map[0][1] = R.drawable.map_two;
		map[0][2] = R.drawable.map_three;
		map[1][0] = R.drawable.map_four;
		map[1][1] = R.drawable.map_five;
		map[1][2] = R.drawable.map_six;
		map[2][0] = R.drawable.map_seven;
		map[2][1] = R.drawable.map_eight;
		map[2][2] = R.drawable.map_nine;
	}
}
