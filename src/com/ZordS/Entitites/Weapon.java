package com.ZordS.Entitites;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;

public class Weapon extends Entity{

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		
		if(pickUp() == true)  {
			 Player.armed = true;
			 Game.entities.remove(this);
		}
			
	}
	
	public boolean pickUp(){
		Rectangle manaPot = new Rectangle(this.getX(), this.getY(), 8, 7);
		 
		 return manaPot.intersects(Player.player);
	}
}
