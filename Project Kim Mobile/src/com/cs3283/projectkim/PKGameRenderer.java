package com.cs3283.projectkim;

import java.io.IOException;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.os.CountDownTimer;

public class PKGameRenderer implements Renderer{
	
	private PKImage gridMap[][] = new PKImage[3][3];
	private PKImage noMap = new PKImage();
	private PKImage grid = new PKImage();
	private PKImage chest = new PKImage();
	private PKImage pIndicator = new PKImage();
	private PKImage quizQns = new PKImage();
	private TexFont font;
	
	private PKMiniGameQuiz quiz;
	private Random rn = new Random();
	private int currentQns;
	//private Calendar currentTime;
	//private Context context;
	
	private int[][] treasureLoc = new int[3][3];
	
	private long startGameTime;
	
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	
	private long startQuizTime;
	private long timeLeft = 10;
	public int quizAns;
	
	private int currentX, currentY; //location of player on map

	@Override
	public void onDrawFrame(GL10 gl) {
		loopStart = System.currentTimeMillis();
		/*try{
			if (loopRunTime < PKEngine.GAME_THREAD_FPS_SLEEP){
				Thread.sleep(PKEngine.GAME_THREAD_FPS_SLEEP - loopRunTime);
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}*/
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//TODO Update drawings
		if(!PKEngine.INIT_GAME_TIMER)
		{
			startGameTime = System.currentTimeMillis() / 1000;
			PKEngine.INIT_GAME_TIMER = true;
		}
		long currentTime = System.currentTimeMillis() / 1000;
		int timeElapsed = (int)currentTime - (int)startGameTime;
		if (PKEngine.TIME_LIMIT-timeElapsed <= 0)
		{
			endGame(gl);
		} else{
			font.SetScale(1.5f);
			font.PrintAt(gl, ""+(PKEngine.TIME_LIMIT-timeElapsed), 275, 590);
			if (PKEngine.CHEST_STATE == 1)
			{
				try {
					PKEngine.client.openTreasureEvent(PKEngine.PLAYER_ID);
					System.out.println("Sent");
					
					PKEngine.CHEST_STATE = 2;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (PKEngine.CHEST_STATE == 2)
			{
				if (!PKEngine.INIT_QUIZ_TIMER){
					startQuizTime = System.currentTimeMillis() / 1000;
					PKEngine.INIT_QUIZ_TIMER = true;
				}
				miniGameStart(gl,1);
			} else
			{
				try {
					PKEngine.client.scoreUpdateEvent(PKEngine.PLAYER_ID, PKEngine.PLAYER_SCORE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				updatePOVMap(gl);
				drawGrid(gl);
				checkTreasure();
				drawMiniMap(gl);
			}
			
			font.SetScale(1.5f);
			font.PrintAt(gl, "GOLD: $" + PKEngine.PLAYER_SCORE, 75, 600);
			font.PrintAt(gl, "RANK: " + PKEngine.client.getPlayerRanking(PKEngine.PLAYER_ID) + "/2", 75, 575);
		}
		
		gl.glEnable( GL10.GL_ALPHA_TEST );

		gl.glAlphaFunc( GL10.GL_GREATER, 0 );
	}

	private void endGame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.75f, 0.75f, 1.0f);
		gl.glTranslatef(0.165f, 0.085f, 0.0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		quizQns.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		if (PKEngine.client.getPlayerRanking(PKEngine.PLAYER_ID) == 1){
			font.SetScale(1.5f);
			font.PrintAt(gl, "YOU WIN!", 150, 300);
			font.PrintAt(gl, "YOU EARNED $" + PKEngine.PLAYER_SCORE + "!", 90, 275);
		} else{
			font.SetScale(1.5f);
			font.PrintAt(gl, "YOU LOSE!", 150, 300);
			font.PrintAt(gl, "YOU EARNED $" + PKEngine.PLAYER_SCORE + "!", 90, 275);
		}
	}

	private void miniGameStart(final GL10 gl, int gameNo) {
		System.out.println("Game Started");
		/*currentTime = Calendar.getInstance();
		int gameEnd = currentTime.SECOND + PKEngine.TIME_LIMIT;*/
		long currentTime = System.currentTimeMillis() / 1000;
		int timeElapsed = (int)currentTime - (int)startQuizTime; 
		
		font.PrintAt(gl, ""+(timeLeft-timeElapsed), 100, 450);
		
		if (!PKEngine.GET_QUESTION){
			int qns = rn.nextInt(11);
			currentQns = qns;
			PKEngine.GET_QUESTION = true;
		}
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.75f, 0.75f, 1.0f);
		gl.glTranslatef(0.165f, 0.085f, 0.0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		quizQns.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		String[] outputWords = quiz.getQuestion(currentQns).split(" ");
		quizAns = quiz.getAnswer(currentQns);
		
		int x = 90;
		int y = 330;
		font.SetScale(0.9f);
		if (outputWords.length == 1)
		{
			font.PrintAt(gl, outputWords[0], x, 300);
		} else if (outputWords.length == 2){
			font.PrintAt(gl, outputWords[0] + " " + outputWords[1], x, 300);
		} else if (outputWords.length == 3){
			font.PrintAt(gl, outputWords[0] + " " + outputWords[1] + " " + outputWords[2], x, 300);
		} else if (outputWords.length == 4){
			font.PrintAt(gl, outputWords[0] + " " + outputWords[1] + " " + outputWords[2] + " " + outputWords[3], x, 300);
		} else{
			for (int i=0; i<outputWords.length-4; i+=4)
			{
				font.PrintAt(gl, outputWords[i] + " " + outputWords[i+1] + " " + outputWords[i+2] + " " + outputWords[i+3], x, y);
				y-=15;
				if (i+8 > outputWords.length)
				{
					if (outputWords.length - (i+4) == 1)
					{
						font.PrintAt(gl, outputWords[i+4], x, y);
						break;
					} else if (outputWords.length - (i+4) == 2){
						font.PrintAt(gl, outputWords[i+4] + " " + outputWords[i+5], x, y);
						break;
					} else if (outputWords.length - (i+4) == 3){
						font.PrintAt(gl, outputWords[i+4] + " " + outputWords[i+5] + " " + outputWords[i+6], x, y);
						break;
					} else if (outputWords.length - (i+4) == 4){
						font.PrintAt(gl, outputWords[i+4] + " " + outputWords[i+5] + " " + outputWords[i+6] + " " + outputWords[i+7], x, y);
						break;
					}
				}
			}
		}
		//font.PrintAt(gl, quiz.getQuestion(currentQns), 80, 300);
		
		font.SetScale(1.5f);
		font.PrintAt(gl, "TRUE", 100, 170);
		font.PrintAt(gl, "FALSE", 270, 170);
	}
	
	public long timeCalculate(double ttime) {

	    long days, hours, minutes, seconds;
	    String daysT = "", restT = "";

	    days = (Math.round(ttime) / 86400);
	    hours = (Math.round(ttime) / 3600) - (days * 24);
	    minutes = (Math.round(ttime) / 60) - (days * 1440) - (hours * 60);
	    seconds = Math.round(ttime) % 60;
	    //String secondsString = ""+seconds;

	    if(days==1) daysT = String.format("%d day ", days);
	    if(days>1) daysT = String.format("%d days ", days);

	    restT = String.format("%02d:%02d:%02d", hours, minutes, seconds);

	    //return daysT + restT;
	    return seconds;
	}

	private void checkTreasure() {
		// TODO Auto-generated method stub
		for (int i=0; i<treasureLoc.length; i++){
			for (int j=0; j<treasureLoc[i].length; j++){
				treasureLoc[i][j] = PKEngine.client.getTreasureList2D()[i][j];
			}
		}
	}
	
	public boolean isTreasure() {
		// TODO Auto-generated method stub
		if (treasureLoc[currentX][currentY] == 1){
			return true;
		}
		return false;
	}
	
	private void drawMiniMap(GL10 gl) {
		float posX = 8.5f;
		float posY = 11.0f;
		
		for (int i=0; i<gridMap.length; i++){
			for (int j=0; j<gridMap[i].length; j++){
				if (currentX == i && currentY == j){
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadIdentity();
					gl.glPushMatrix();
					gl.glScalef(0.08f, 0.08f, 1.0f);
					gl.glTranslatef(posX, posY, 0.0f);
					gl.glMatrixMode(GL10.GL_TEXTURE);
					gl.glLoadIdentity();
					gl.glTranslatef(0.0f, 0.0f, 0.0f);
					gridMap[i][j].draw(gl);
					pIndicator.draw(gl);
					gl.glPopMatrix();
					gl.glLoadIdentity();
					posX += 1.0f;
				} else{
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadIdentity();
					gl.glPushMatrix();
					gl.glScalef(0.08f, 0.08f, 1.0f);
					gl.glTranslatef(posX, posY, 0.0f);
					gl.glMatrixMode(GL10.GL_TEXTURE);
					gl.glLoadIdentity();
					gl.glTranslatef(0.0f, 0.0f, 0.0f);
					gridMap[i][j].draw(gl);
					gl.glPopMatrix();
					gl.glLoadIdentity();
					posX += 1.0f;
				}
			}
			posX = 8.5f;
			posY -= 1.0f;
		}
	}

	private void drawGrid(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.75f, 0.75f, 1.0f);
		gl.glTranslatef(0.165f, 0.085f, 0.0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		grid.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}

	private void updatePOVMap(GL10 gl) {
		// TODO Auto-generated method stub
		try {
			PKEngine.client.mapUpdateEvent(PKEngine.PLAYER_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentX = PKEngine.client.getPlayerX(PKEngine.PLAYER_ID);
		currentY = PKEngine.client.getPlayerY(PKEngine.PLAYER_ID);
		System.out.println("[X]: " + currentX + ", [Y]: " + currentY);
		
		// Top Left
		if (currentX-1 < 0 || currentY-1 < 0){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+3.0f);
		} else{
			updateMap(gl, gridMap[currentX-1][currentY-1], PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+3.0f);
			if (treasureLoc[currentX-1][currentY-1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+3.0f);
			}
		}
		
		// Top
		if (currentX-1 < 0){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+3.0f);
		} else{
			updateMap(gl, gridMap[currentX-1][currentY], PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+3.0f);
			if (treasureLoc[currentX-1][currentY] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+3.0f);
			}
		}
		
		// Top Right
		if (currentX-1 < 0 || currentY+1 >= gridMap.length){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+3.0f);
		} else{
			updateMap(gl, gridMap[currentX-1][currentY+1], PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+3.0f);
			if (treasureLoc[currentX-1][currentY+1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+3.0f);
			}
		}
		
		// Left
		if (currentY-1 < 0){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+2.0f);
		} else{
			updateMap(gl, gridMap[currentX][currentY-1], PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+2.0f);
			if (treasureLoc[currentX][currentY-1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+2.0f);
			}
		}
		
		// Center
		updateMap(gl, gridMap[currentX][currentY], PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+2.0f);
		if (treasureLoc[currentX][currentY] == 1){
			updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+2.0f);
		}
		
		// Right
		if (currentY+1 >= gridMap.length){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+2.0f);
		} else{
			updateMap(gl, gridMap[currentX][currentY+1], PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+2.0f);
			if (treasureLoc[currentX][currentY+1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+2.0f);
			}
		}
		
		// Bottom Left
		if (currentX+1 >= gridMap.length || currentY-1 < 0){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+1.0f);
		} else{
			updateMap(gl, gridMap[currentX+1][currentY-1], PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+1.0f);
			if (treasureLoc[currentX+1][currentY-1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT, PKEngine.GRID_ROWSHIFT+1.0f);
			}
		}
		
		// Bottom
		if (currentX+1 >= gridMap.length){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+1.0f);
		} else{
			updateMap(gl, gridMap[currentX+1][currentY], PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+1.0f);
			if (treasureLoc[currentX+1][currentY] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+1.0f, PKEngine.GRID_ROWSHIFT+1.0f);
			}
		}
		
		// Bottom Right
		if (currentX+1 >= gridMap.length || currentY+1 >= gridMap.length){
			updateMap(gl, noMap, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+1.0f);
		} else{
			updateMap(gl, gridMap[currentX+1][currentY+1], PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+1.0f);
			if (treasureLoc[currentX+1][currentY+1] == 1){
				updateTreasure(gl, chest, PKEngine.GRID_COLUMNSHIFT+2.0f, PKEngine.GRID_ROWSHIFT+1.0f);
			}
		}
	}

	private void updateTreasure(GL10 gl, PKImage map, float x, float y) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.25f, 0.25f, 1.0f);
		gl.glTranslatef(x, y, 0.0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		map.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
	}

	private void updateMap(GL10 gl, PKImage map, float x, float y) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.25f, 0.25f, 1.0f);
		gl.glTranslatef(x, y, 0.0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		map.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height); // 	The width and height sent in by SFGameView will represent the width and height of
											//	the device minus the notification bar at the top of the screen.
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		/*gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);*/
		
		//TODO Add textures loading for background images
		
		for (int i=0; i<gridMap.length; i++){
			for (int j=0; j<gridMap[i].length; j++){
				System.out.println("Adding.." + "[" +i+"]["+j+"]");
				gridMap[i][j] = new PKImage();
				gridMap[i][j].loadTexture(gl, PKEngine.map[i][j], PKEngine.context);
			}
		}
		
		// Loads fonts
		font = new TexFont(PKEngine.context, gl);
		try {
			font.LoadFontAlt("visitor.bff", gl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		quiz = new PKMiniGameQuiz();
		
		noMap.loadTexture(gl, PKEngine.NO_MAP, PKEngine.context);
		grid.loadTexture(gl, PKEngine.GRID, PKEngine.context);
		chest.loadTexture(gl, PKEngine.CHEST, PKEngine.context);
		pIndicator.loadTexture(gl, PKEngine.PLAYER_INDICATOR, PKEngine.context);
		quizQns.loadTexture(gl, PKEngine.QUIZ_QUESTION, PKEngine.context);
	}
}