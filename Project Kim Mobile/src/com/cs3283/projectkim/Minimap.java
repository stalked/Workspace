package com.cs3283.projectkim;

public class Minimap {
	
	private int mapX, mapY;
	private boolean isTreasureChest;

	public Minimap(int i, int j) {
		// TODO Auto-generated constructor stub
		mapX = i;
		mapY = j;
		isTreasureChest = false;
	}

	public void update(int i) {
		// TODO Auto-generated method stub
		if (i == 1){
			isTreasureChest = true;
		} else{
			isTreasureChest = false;
		}
	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}

	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	public void setMapY(int mapY) {
		this.mapY = mapY;
	}

	public boolean isTreasureChest() {
		return isTreasureChest;
	}

	public void setTreasureChest(boolean isTreasureChest) {
		this.isTreasureChest = isTreasureChest;
	}

}
