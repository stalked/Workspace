package com.cs3283.projectkim;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Toast;

public class PKGame extends Activity{
	
	private PKGameView gameView;
	PKGameRenderer renderer;
	private float mScrWidth, mScrHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		mScrWidth = size.x; 
        mScrHeight = size.y;
        try {
        	System.out.println("Establishing connection...");
			PKEngine.client = new GameClient();
			System.out.println("Connected.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
        
        PKEngine.addMap();
        gameView = new PKGameView(this);
        renderer = new PKGameRenderer();
        setContentView(gameView);
        gameView.setRenderer(renderer);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		gameView.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gameView.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean isTreasure;
		
		// This toast is used for debugging purposes
		/*Toast msg = Toast.makeText(this.getApplicationContext(), "X: " + x + ", Y: " + y, Toast.LENGTH_SHORT);
		msg.show();*/
		
		// If no chest is selected
		if (PKEngine.CHEST_STATE == 0){
			if ((x>180 && x<300) && (y>415 && y<580)){
				isTreasure = renderer.isTreasure();
				if (isTreasure){
					switch (event.getAction() )
					{ 
		            case MotionEvent.ACTION_UP: //
		            	PKEngine.CHEST_STATE = 1;
		            	break; 
					}
				}
			}
		}else{
			// Quiz Minigame

			if ((x>65 && x<210) && (y>540 && y<625)) //TRUE
			{
				switch (event.getAction() ) { 
				
	            case MotionEvent.ACTION_UP:
	            	if (renderer.quizAns == 1)
	            	{
	            		PKEngine.PLAYER_SCORE += 50;
		            	Toast msg2 = Toast.makeText(this.getApplicationContext(), "Chest Opened! Congratulations!", Toast.LENGTH_SHORT);
						msg2.show();
	            	} else{
	            		Toast msg2 = Toast.makeText(this.getApplicationContext(), "Failed to open chest.", Toast.LENGTH_SHORT);
						msg2.show();
	            	}
	            	PKEngine.CHEST_STATE = 0;
	            	PKEngine.GET_QUESTION = false;
	            	PKEngine.INIT_QUIZ_TIMER = false;
	            	break; 
				}
			} else if ((x>250 && x<4100) && (y>540 && y<625)) //FALSE
			{
				switch (event.getAction() ) { 
				
	            case MotionEvent.ACTION_UP:
	            	if (renderer.quizAns == 0)
	            	{
	            		PKEngine.PLAYER_SCORE += 50;
		            	Toast msg2 = Toast.makeText(this.getApplicationContext(), "Chest Opened! Congratulations!", Toast.LENGTH_SHORT);
						msg2.show();
	            	} else{
	            		Toast msg2 = Toast.makeText(this.getApplicationContext(), "Failed to open chest.", Toast.LENGTH_SHORT);
						msg2.show();
	            	}
	            	PKEngine.CHEST_STATE = 0;
	            	PKEngine.GET_QUESTION = false;
	            	PKEngine.INIT_QUIZ_TIMER = false;
	            	break; 
				}
			}
		}
		return false;
	}
}