package com.ZordS.Entitites;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ZordS.Main.Game;
import com.ZordS.World.Camera;
import com.ZordS.World.World;

public class Player extends Entity {

	public static boolean right;
	public boolean up;
	public boolean left;
	public boolean down;
	public double speed = 1.5;
	public static boolean shooter = false;
	
	private static int spd;
	protected int maxFrames = 5, maxIndex = 2;
	private static  int right_dir = 0;
	private static int left_dir=1;
	private static  int dir = right_dir;
	private int frames = 0, index = 0;
	public boolean stoped = true;
	private boolean moved = false;
	public static Rectangle player;
	public static boolean armed=false;
	
	public static double life = 100, maxLife = 100;
	public static double mana = 0, maxMana = 100;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightArmed;
	private BufferedImage[] leftArmed;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		rightArmed = new BufferedImage[3];
		leftArmed = new BufferedImage[3];
		life = 100;
		armed = false;
		mana = 0;
		
		for(int i=0; i<3; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(34 + (i*16),0,10,16);
			leftPlayer[i] = Game.spritesheet.getSprite(34 + (i*16), 17, 10, 16);
			rightArmed[i] = Game.spritesheet.getSprite(83+ (i*16), 34, 12, 16);
			leftArmed[i] = Game.spritesheet.getSprite(80+ (i*16), 51, 12, 16);
		}
	}
	
	public void tick(){
		
		moved = false;
		if(right && World.isFree((int)(this.getX()+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(this.getX()-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(), (int)(this.getY()-speed))) {
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(), (int)(this.getY()+speed))) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			
			frames++;
			if(frames==maxFrames) {
				frames = 0;
				index++;
				if(index>maxIndex) {
					index = 0;
				}
			}
			
		}


		player = new Rectangle(Game.player.getX(), Game.player.getY(), 11, 16);
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY()-(Game.HEIGHT/2), 0, World.WIDTH*16 - Game.HEIGHT);
		
		if(shooter == true) {
			int direct = 1;
			spd = 4;
			if(dir == right_dir) {
				direct = 1;
			}else if (dir == left_dir){
				direct = -1;
			}
			FireBlast  fireShoot= new FireBlast((int)this.getX(),(int)this.getY(), 10, 9, null, spd, direct);
			Game.fireShoot.add(fireShoot);
			shooter = false;
		}
		
	}
	
	public void render(Graphics g) {
		if(armed == false) {
			if(dir == right_dir) {
				if(stoped == true) {
					g.drawImage(rightPlayer[0], this.getX()-Camera.x, this.getY()-Camera.y, null);
				}else {
					g.drawImage(rightPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
				}
			}else if(dir == left_dir) {
				if(stoped == true) {
					g.drawImage(leftPlayer[2], this.getX()-Camera.x, this.getY()-Camera.y, null);
				}else {
					g.drawImage(leftPlayer[index],this.getX()-Camera.x, this.getY()-Camera.y, null );
				}
			}
			
		}else {
			if(dir == right_dir) {
				if(stoped == true) {
					g.drawImage(rightArmed[0], this.getX()-Camera.x, this.getY()-Camera.y, null);
				}else {
					g.drawImage(rightArmed[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
				}
			}else if(dir == left_dir) {
				if(stoped == true) {
					g.drawImage(leftArmed[2], this.getX()-Camera.x, this.getY()-Camera.y, null);
				}else {
					g.drawImage(leftArmed[index],this.getX()-Camera.x, this.getY()-Camera.y, null );
				}
			}
			
		}
	}

}
