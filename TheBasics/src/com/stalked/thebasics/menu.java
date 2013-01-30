package com.stalked.thebasics;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Button Sound
		final MediaPlayer buttonSound = MediaPlayer.create(menu.this, R.raw.button_click);
		
		// Setting up button references
		Button tut = (Button) findViewById(R.id.tutorial);
		Button tut1 = (Button) findViewById(R.id.tutorial1);
		
		tut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonSound.start();
				// TODO Auto-generated method stub
				startActivity(new Intent("com.stalked.thebasics.TUTORIAL"));
			}
		});
		
		tut1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonSound.start();
				// TODO Auto-generated method stub
				startActivity(new Intent("com.stalked.thebasics.TUTORIAL"));
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater awesome = getMenuInflater();
		awesome.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.menuSweet:
			startActivity(new Intent("com.stalked.thebasics.SWEET"));
			return true;
		case R.id.menuToast:
			Toast myToast = Toast.makeText(menu.this, "This is a toast", Toast.LENGTH_LONG);
			myToast.show();
			return true;
		}
		return false;
	}
}
