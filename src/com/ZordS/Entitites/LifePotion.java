package com.ZordS.Entitites;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;

public class LifePotion extends Entity {

	public LifePotion(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		
		if(pickUp() == true)  {
			if(Player.life < Player.maxLife) {
				Player.life+=50;
				if(Player.life>100) {
					Player.life = 100;
				}
			}
			Game.entities.remove(this);
		}
		
	}
	
	public boolean pickUp(){
		Rectangle lifePot = new Rectangle(this.getX(), this.getY(), 8, 7);
		 
		 return lifePot.intersects(Player.player);
	}

}
