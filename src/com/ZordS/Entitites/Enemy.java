package com.ZordS.Entitites;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;
import com.ZordS.World.Camera;
import com.ZordS.World.World;

public class Enemy extends Entity{
	
	private double speed = 0.8;
	
	private boolean right=true,left=false;
	
	private int ticks=0, maxTicks=5, index=0, maxIndex = 2;
	public int life;
	
	public BufferedImage[] rightEnemy;
	public BufferedImage[] leftEnemy;
	public Rectangle currentEnemy;
	
	private Rectangle enemy;
	private Rectangle fireBlast;
	private int indexShoot;
	
	public void setLife(int damage){
		this.life -= damage;
	}
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[3];
		leftEnemy = new BufferedImage[3];
		life = 10;
		
		for(int i=0; i<3; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(34 + (i*16),34,12,16);
			leftEnemy[i] = Game.spritesheet.getSprite(34 + (i*16), 51, 12, 16);
		}
	}
	
	public void tick() {
		
		if(playerCollide() == false) {
			if((int)x < Game.player.getX()  &&
				World.isFree((int)(x+speed), this.getY()) &&
				!isColliding((int)(x+speed), this.getY())) {
					x+=speed;
					left = false;
					right = true;
				}else if((int)x > Game.player.getX() &&
					World.isFree((int)(x-speed), this.getY()) &&
					!isColliding((int)(x-speed), this.getY())) {
						x-=speed;
						left = true;
						right = false;
				}
			
				if((int)y < Game.player.getY() &&
						World.isFree(this.getX(), (int)(y+speed)) &&
						!isColliding(this.getX(), (int)(y+speed))) {
						y+=speed;
			}else if((int)y > Game.player.getY() &&
				World.isFree(this.getX(), (int)(y-speed)) &&
				!isColliding(this.getX(), (int)(y-speed))) {
					y-=speed;
			}
		}else {
			Game.player.life-=1;
		}

		ticks++;
		if(ticks==maxTicks) {
			ticks = 0;
			index++;
			if(index>maxIndex) {
				index = 0;
			}
		}
		
		if(shootCollide()) {
			Game.fireShoot.remove(indexShoot);
			life -= FireBlast.damage;
			if(life<=0) {
				Game.entities.remove(this);
				Game.enemies.remove(this);
			}
		}

	}
	
	public void render(Graphics g){
		
		if(right == true) {
			g.drawImage(rightEnemy[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}else if(left == true) {
			g.drawImage(leftEnemy[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
		
	}
	
	public boolean playerCollide() {

		currentEnemy = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE_X+1, World.TILE_SIZE_Y);
		
		return currentEnemy.intersects(Player.player);
	}
	
	public boolean isColliding(int xNext, int yNext) {
		
		currentEnemy = new Rectangle(xNext, yNext, World.TILE_SIZE_X+1, World.TILE_SIZE_Y);
		
		for(int i=0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;				
			}
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE_X+1, World.TILE_SIZE_Y);
			if(currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean shootCollide() {

		enemy = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE_X+1, World.TILE_SIZE_Y);
		for(int i=0; i<Game.fireShoot.size(); i++) {
			Entity e = Game.fireShoot.get(i);
			fireBlast = new Rectangle(e.getX(), e.getY(), 10, 9);
			indexShoot = i;
			if(fireBlast.intersects(enemy)) {
				return true;
			}
		}
		return false;
	}

}
