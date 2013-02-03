package com.cs3283.projectkim;

//import java.awt.Image;

public class Map {
	
	//private Image mapPiece;
	private boolean isTreasure;
	private boolean hasWalked;
	
	public Map(/*Image image*/) {
		// TODO Auto-generated constructor stub
		//mapPiece = image;
		isTreasure = false;
		hasWalked = false;
	}
	
	public void update(int i) {
		// TODO Auto-generated method stub
		if (i == 1){
			isTreasure = true;
		} else{
			isTreasure = false;
		}
	}

	/*public Image getMapPiece() {
		return mapPiece;
	}*/

	public boolean isTreasure() {
		return isTreasure;
	}

	/*public void setMapPiece(Image mapPiece) {
		this.mapPiece = mapPiece;
	}*/

	public void setTreasure(boolean isTreasure) {
		this.isTreasure = isTreasure;
	}

	public boolean isHasWalked() {
		return hasWalked;
	}

	public void setHasWalked(boolean hasWalked) {
		this.hasWalked = hasWalked;
	}

}
