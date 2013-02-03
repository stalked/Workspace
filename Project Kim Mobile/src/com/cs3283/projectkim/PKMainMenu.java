package com.cs3283.projectkim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PKMainMenu extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/* Fires up background music */
		PKEngine.musicThread = new Thread(){
			public void run(){
				Intent bgmusic = new Intent(getApplicationContext(), PKMusic.class);
				startService(bgmusic);
				PKEngine.context = getApplicationContext();
			} 
		};
		PKEngine.musicThread.start();
		
		final PKEngine engine = new PKEngine();
		
		/* Set menu button options */
		ImageButton startGame = (ImageButton)findViewById(R.id.btnStartGame);
        ImageButton tutorial = (ImageButton)findViewById(R.id.btnTutorial);
        ImageButton settings = (ImageButton)findViewById(R.id.btnSettings);
        ImageButton credits = (ImageButton)findViewById(R.id.btnCredits);
        
        startGame.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
        startGame.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
        
        tutorial.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
        tutorial.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
        
        settings.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
        settings.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);

        credits.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
        credits.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
		
		startGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/* Start Game!!!!! */
				Intent game = new Intent(getApplicationContext(), PKGame.class);
				PKMainMenu.this.startActivity(game);
				System.out.println("Starting the game..");
			}
		});
		
		/*exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean clean = false;
				clean = engine.onExit(v);
				if (clean){
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
			}
		});*/
		tutorial.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v) 
        	{
        		Intent tutorialMenu = new Intent(PKMainMenu.this, pkTutorial.class); 
        		PKMainMenu.this.startActivity(tutorialMenu);
        		overridePendingTransition(R.layout.fadein,R.layout.fadeout);
        	}
        });
        
        settings.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v) 
        	{
        		Intent settingsMenu = new Intent(PKMainMenu.this, pkSettings.class); 
        		PKMainMenu.this.startActivity(settingsMenu);
        		overridePendingTransition(R.layout.fadein,R.layout.fadeout);
        	}
        });
        
        credits.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v) 
        	{
        		/*Intent creditsMenu = new Intent(PKMainMenu.this, pkCredits.class); 
        		PKMainMenu.this.startActivity(creditsMenu);
        		overridePendingTransition(R.layout.fadein,R.layout.fadeout);*/
        		boolean clean = false;
				clean = engine.onExit(v);
				engine.client.closeSocket();
				if (clean){
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
        	}
        });
	}
}
