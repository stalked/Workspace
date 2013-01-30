package com.cs3283.projectkim;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler; 
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;


public class MainActivity extends Activity 
{
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Display Splash Screen
        setContentView(R.layout.splashscreen);
        
        //Start up the splash screen and main menu in a time delayed thread
        new Handler().postDelayed(new Thread() 
        { 
            @Override 
            public void run() 
            { 
            	Intent mainMenu = new Intent(MainActivity.this, PKMainMenu.class); 
            	MainActivity.this.startActivity(mainMenu);
            	MainActivity.this.finish();
            	overridePendingTransition(R.layout.fadein,R.layout.fadeout);
            } 
        }, PKEngine.GAME_THREAD_DELAY); 
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id)
    {
    	return null;
    }
    
}
