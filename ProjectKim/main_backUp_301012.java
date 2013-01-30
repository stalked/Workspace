package projectKim;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.SocketException;
import java.net.URL;

public class main extends Applet implements Runnable, KeyListener {
	
	private Image image, grid, playerLoc, chest, noMap;
	private Graphics second;
	private Minimap mTopLeft, mTop, mTopRight, mLeft, currentLoc, mRight, mBtmLeft, mBtm, mBtmRight;
	private Map mapPiece[][] = new Map[5][5];
	private Image mapArr[][] = new Image[5][5];
	private URL base;
	private int currentX, currentY;
	
	private GameClient client;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setSize(320, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Project Kim");
		
		try{
			base = getDocumentBase();
		} catch (Exception e){
			
		}
		int counter=1;
		
		// Image setups
		/*for (int i=0; i<mapArr.length; i++){
			for (int j=0; j<mapArr.length; j++){
				if (i==0 || i==4 || j==0 || j==4){
					mapArr[i][j] = getImage(base, "data/nomap.png");
				} else{
					mapArr[i][j] = getImage(base, "data/" + counter + ".png");
					counter++;
				}
			}
		}*/
		
		for (int i=0; i<mapArr.length; i++){
			for (int j=0; j<mapArr[i].length; j++){
				mapArr[i][j] = getImage(base, "data/" + counter + ".png");
				counter++;
			}
		}
		
		noMap = getImage(base, "data/nomap.png");
		grid = getImage(base, "data/grid.png");
		playerLoc = getImage(base, "data/location.png");
		chest = getImage(base, "data/chest.png");
		super.init();
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		try {
			client = new GameClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[][] x = client.getTreasureList2D();
		
		for (int i=0; i<mapPiece.length; i++){
			for (int j=0; j<mapPiece.length; j++){
				mapPiece[i][j] = new Map(mapArr[i][j]);
			}
		}
		
		// POV MAP [3x3 grid]
		mTopLeft = new Minimap(10,100);
		mTop = new Minimap(110,100);
		mTopRight = new Minimap(210,100);
		mLeft = new Minimap(10,200);
		currentLoc = new Minimap(110,200);
		mRight = new Minimap(210,200);
		mBtmLeft = new Minimap(10,300);
		mBtm = new Minimap(110,300);
		mBtmRight = new Minimap(210,300);
		
		Thread thread = new Thread(this);
		thread.start();
		super.start();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			int[][] test = new int[5][5];
			
			/*for (int i=0; i<test.length;i++){
				for (int j=0; j<test[i].length;j++){
					test[i][j] = 0;
				}
			}
			
			for (int i=1; i<test.length-1;i++){
				for (int j=1; j<test[i].length-1;j++){
					test[i][j] = client.getTreasureList()[i-1][j-1];
				}
			}*/
			
			for (int i=0; i<mapPiece.length; i++){
				for (int j=0; j<mapPiece[i].length; j++){
					mapPiece[i][j].update(client.getTreasureList2D()[i][j]);
				}
			}
			
			repaint();
			try {
				Thread.sleep(200);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(Graphics g){
		if (image == null){
			image = createImage(this.getWidth(),this.getHeight());
			second = image.getGraphics();
		}
		
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		
		g.drawImage(image, 0, 0, this);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Waiting (Before)...");
			client.mapUpdateEvent(1); //TODO add playerID
			
			System.out.println("Waiting...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentX = client.getPlayerX(1); //TODO add playerID
		currentY = client.getPlayerY(1); //TODO add playerID
		
		System.out.println("Current X: " + currentX + ", Current Y: " + currentY);
			
		paintMap(g, currentX, currentY);
		g.drawImage(grid,10,100,this);
		paintMiniMap(g);
		super.paint(g);
	}
	
	private void paintMiniMap(Graphics g) {
		// TODO Auto-generated method stub
		int x = 180;
		int y = -10;
		for (int i=0; i<mapArr.length; i++){
			for (int j=0; j<mapArr[i].length; j++){
				/*if (i!=0 && i!=4 && j!=0 && j!=4){
					
					if (i == currentX && j == currentY){
						g.drawImage(playerLoc, x, y, x+30, y+30, 0, 0, 100, 100, this);
					}
				}*/
				g.drawImage(mapArr[i][j], x, y, x+30, y+30, 0, 0, 100, 100, this);
				if (i == currentX && j == currentY){
					g.drawImage(playerLoc, x, y, x+30, y+30, 0, 0, 100, 100, this);
				}
				x+=30;
			}
			x=180;
			y+=30;
		}
	}

	private void paintMap(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mTopLeft.getMapX(), mTopLeft.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x-1][y-1].getMapPiece(), mTopLeft.getMapX(), mTopLeft.getMapY(), this);
			if (mapPiece[x-1][y-1].isTreasure()){
				g.drawImage(chest, mTopLeft.getMapX(), mTopLeft.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mTop.getMapX(), mTop.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x-1][y].getMapPiece(), mTop.getMapX(), mTop.getMapY(), this);
			if (mapPiece[x-1][y].isTreasure()){
				g.drawImage(chest, mTop.getMapX(), mTop.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mTopRight.getMapX(), mTopRight.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x-1][y+1].getMapPiece(), mTopRight.getMapX(), mTopRight.getMapY(), this);
			if (mapPiece[x-1][y+1].isTreasure()){
				g.drawImage(chest, mTopRight.getMapX(), mTopRight.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mLeft.getMapX(), mLeft.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x][y-1].getMapPiece(), mLeft.getMapX(), mLeft.getMapY(), this);
			if (mapPiece[x][y-1].isTreasure()){
				g.drawImage(chest, mLeft.getMapX(), mLeft.getMapY(), this);
			}
		}
		g.drawImage(mapPiece[x][y].getMapPiece(), currentLoc.getMapX(), currentLoc.getMapY(), this);
		if (mapPiece[x][y].isTreasure()){
			g.drawImage(chest, currentLoc.getMapX(), currentLoc.getMapY(), this);
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mRight.getMapX(), mRight.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x][y+1].getMapPiece(), mRight.getMapX(), mRight.getMapY(), this);
			if (mapPiece[x][y+1].isTreasure()){
				g.drawImage(chest, mRight.getMapX(), mRight.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mBtmLeft.getMapX(), mBtmLeft.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x+1][y-1].getMapPiece(), mBtmLeft.getMapX(), mBtmLeft.getMapY(), this);
			if (mapPiece[x+1][y-1].isTreasure()){
				g.drawImage(chest, mBtmLeft.getMapX(), mBtmLeft.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mBtm.getMapX(), mBtm.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x+1][y].getMapPiece(), mBtm.getMapX(), mBtm.getMapY(), this);
			if (mapPiece[x+1][y].isTreasure()){
				g.drawImage(chest, mBtm.getMapX(), mBtm.getMapY(), this);
			}
		}
		if (x-1 < 0 || y+1 >= mapPiece.length){
			g.drawImage(noMap, mBtmRight.getMapX(), mBtmRight.getMapY(), this);
		} else{
			g.drawImage(mapPiece[x+1][y+1].getMapPiece(), mBtmRight.getMapX(), mBtmRight.getMapY(), this);
			if (mapPiece[x+1][y+1].isTreasure()){
				g.drawImage(chest, mBtmRight.getMapX(), mBtmRight.getMapY(), this);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Move Up");
			currentX--;
			break;

		case KeyEvent.VK_DOWN:
			System.out.println("Move Down");
			currentX++;
			break;

		case KeyEvent.VK_LEFT:
			//System.out.println("Move Left");
			currentY--;
			break;

		case KeyEvent.VK_RIGHT:
			//System.out.println("Move Right");
			currentY++;
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			break;

		case KeyEvent.VK_DOWN:
			//System.out.println("Stop Move Down");
			break;

		case KeyEvent.VK_LEFT:
			//System.out.println("Stop Move Left");
			break;

		case KeyEvent.VK_RIGHT:
			//System.out.println("Stop Move Right");
			break;

		case KeyEvent.VK_SPACE:
			System.out.println("Stop Jump");
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
