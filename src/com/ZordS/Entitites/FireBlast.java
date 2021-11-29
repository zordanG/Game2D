package com.ZordS.Entitites;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;
import com.ZordS.World.Camera;
import com.ZordS.World.World;

public class FireBlast extends Entity{
	
	public BufferedImage fireballLeft;
	public BufferedImage fireballRight;
	public static double damage = 10;
	private double speed;
	private int direct;
	
	public FireBlast(int x, int y, int width, int height, BufferedImage sprite, int speed, int direct) {
		super(x, y, width, height, sprite);
		fireballLeft = Game.spritesheet.getSprite(84,19,10,9);
		fireballRight = Game.spritesheet.getSprite(98,19,10,9);
		this.direct = direct;
		this.speed = speed;
	}
	
	public void tick() {
		
		x += direct*speed;
		
	}

	
	public void render(Graphics g) {
		if(direct == 1) {
			g.drawImage(fireballLeft, this.getX()-Camera.x, this.getY()-Camera.y, 10, 9, null);
		}else{
			g.drawImage(fireballRight, this.getX()-Camera.x, this.getY()-Camera.y, 10, 9, null);
		}
	}
	
}
