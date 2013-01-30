package com.cs3283.projectkim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;

public class MainActivity extends Activity {
	
/*	// handle to main screen
    android.widget.FrameLayout mainFrame;
    
    Game game = null;*/
    
    int mScrWidth;
    int mScrHeight;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //create pointer to main screen
        /*mainFrame = (android.widget.FrameLayout) findViewById(R.id.main_view);*/
        
        //establish connection to server
        
        //get screen dimensions
        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScrWidth = size.x; 
        mScrHeight = size.y;
        
        //create the view that will do the hard work (animator)
        game = new Game(this, mainFrame, mScrWidth, mScrHeight);  
        mainFrame.addView(game); //link view this context
*/        
        /* Start a new game thread */
        new Handler().postDelayed(new Thread(){
        	@Override
        	public void run(){
        	Intent mainMenu = new Intent(MainActivity.this, PKMainMenu.class);
        	MainActivity.this.startActivity(mainMenu);
        	MainActivity.this.finish();
        	}
        }, PKEngine.GAME_THREAD_DELAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
