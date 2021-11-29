package com.ZordS.Entitites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;
import com.ZordS.World.Camera;

public class Entity {
		
	public static BufferedImage LIFE_POTION = Game.spritesheet.getSprite(7*16+1,0,16,16);
	public static BufferedImage WEAPON = Game.spritesheet.getSprite(5*16+1,0,16,16);
	public static BufferedImage MANA_POTION = Game.spritesheet.getSprite(6*16+1,0,16,16);
	public static BufferedImage ENEMY = Game.spritesheet.getSprite(33, 34, 16, 16);
	
	protected double x,  y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void render(Graphics g) {
		
		g.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		
	}
	
	public void tick() {
		
	}
	
}
