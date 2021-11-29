package com.ZordS.Main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.ZordS.Entitites.Enemy;
import com.ZordS.Entitites.Entity;
import com.ZordS.Entitites.FireBlast;
import com.ZordS.Entitites.LifePotion;
import com.ZordS.Entitites.Player;
import com.ZordS.Graphics.Spritesheet;
import com.ZordS.Graphics.UI;
import com.ZordS.World.World;

public class Game extends Canvas implements Runnable, KeyListener{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isRunning, saveGame=false;
	private Thread thread;
	public static JFrame frame;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 200;
	public static int SCALE = 3;
	private BufferedImage image;
	private int lvl, maxLvl = 2;
	public UI ui;
	public static String gameState = "MENU";
	public boolean gameOver = true, restartGame = false;
	public int overTicks = 0;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<LifePotion> lifePot;
	public static List<FireBlast> fireShoot;
	public static Spritesheet spritesheet;
	
	public static Player player;
	public static World world;
	public Menu menu;
			
	public Game() {
		
		addKeyListener(this);
		ui = new UI();
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		fireShoot = new ArrayList<FireBlast>();
		spritesheet = new Spritesheet("/spritesheet.png");
		lvl = 0;

	    player = new Player(0,0,16,16,spritesheet.getSprite(33, 0, 16, 16));
		entities.add(player);
		world = new World("/Mapsheet.png");
		menu = new Menu();
		
	}
	
	public void initFrame() {

		frame = new JFrame("Zeld");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		
		if(gameState == "NORMAL") {
			if(this.saveGame) {
				saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.lvl};
				Menu.saveGame(opt1,opt2,10);
			}
			restartGame = false;
			for(int i=0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			if(Player.life<=0) {
				gameState = "OVER";
			}
			for(int i = 0 ; i < fireShoot.size(); i++) {
				fireShoot.get(i).tick();
			}
			/*if(enemies.size()==0) {
				lvl++;
				if(lvl>maxLvl) {
				
				}
				String newWorld = "level"+lvl+".png";
			}*/
		}else if(gameState == "OVER") {
			overTicks++;
			if(overTicks >= 30) {
				overTicks=0;
				if(gameOver) {
					gameOver = false;
				}else {
					gameOver = true;
				}
			}
			if(restartGame) {
				String w = "mapsheet";
				restart(w);
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(19,19,19));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.render(g);
		for(int i=0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0 ; i < fireShoot.size(); i++) {
			fireShoot.get(i).render(g);
		}
		ui.render(g);
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		if(gameState == "OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH*SCALE)/2-90, (HEIGHT*SCALE)/2);
			g.setFont(new Font("arial", Font.BOLD, 26));
			if(gameOver) {
				g.drawString("Press ENTER to restart", (WIDTH*SCALE)/2-130, (HEIGHT*SCALE)/2+60);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}

	@Override
	public void run() {
		
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			player.stoped = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			player.stoped = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			player.stoped = false;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			player.stoped = false;
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(player.mana>0 && player.armed == true) {
				player.mana-=10;
				Player.shooter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}else if(gameState == "NORMAL") {
				gameState = "MENU";
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_B) {
			if(gameState=="NORMAL") {
				this.saveGame = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			player.stoped = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
			player.stoped = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			player.stoped = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			player.stoped = true;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public static void restart(String map) {
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		fireShoot = new ArrayList<FireBlast>();
		spritesheet = new Spritesheet("/spritesheet.png");

    	player = new Player(0,0,16,16,spritesheet.getSprite(33, 0, 16, 16));
		entities.add(player);
		world = new World("/"+map+".png");
		gameState = "NORMAL";
		
	}
}
