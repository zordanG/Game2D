package com.ZordS.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ZordS.Entitites.*;
import com.ZordS.Main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE_Y = 16, TILE_SIZE_X = 11, TILE_SIZE = 16;
	
	public World(String path) {
		
		final int floor = 0xFF000000; //Preto
		final int wall = 0xFFFFFFFF; //Branco
		final int player = 0xFF05FF11; // Verde
		final int manaPotion = 0xFF00F6FF; //Azul 
		final int lifePotion = 0xFFFFEE00; //Amarelo 
		final int enemy = 0xFFFF0800; //Vermelho 
		final int weapon = 0xFFB200FF; //Roxo
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			tiles = new Tile[map.getWidth()* map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx< map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if(pixelAtual == floor) {
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						
					}else if(pixelAtual == wall) {
						
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
						
					}else if(pixelAtual == player) {
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						
					}else if(pixelAtual == lifePotion) {
						
						LifePotion life = new LifePotion(xx*16,yy*16,8,7,Entity.LIFE_POTION);
						Game.entities.add(life);
						
					}else if(pixelAtual == weapon) {
						
						Game.entities.add(new Weapon(xx*16,yy*16,16,16,Entity.WEAPON));
						
					}else if(pixelAtual == manaPotion) {
						
						Game.entities.add(new ManaPotion(xx*16,yy*16,16,16,Entity.MANA_POTION));
						
					}else if(pixelAtual == enemy) {
						
						Enemy en = new Enemy(xx*TILE_SIZE,yy*TILE_SIZE,TILE_SIZE,TILE_SIZE,Entity.ENEMY);
						Game.entities.add(en);
						Game.enemies.add(en);
						
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		
		int x1 = xNext/TILE_SIZE;
		int y1 = yNext/TILE_SIZE;
		
		int x2 = (xNext+TILE_SIZE_X)/TILE_SIZE;
		int y2 = yNext/TILE_SIZE;
		
		int x3 = xNext/TILE_SIZE;
		int y3 = (yNext+TILE_SIZE_Y)/TILE_SIZE;
		
		int x4 = (xNext+TILE_SIZE_X)/TILE_SIZE;
		int y4 = (yNext+TILE_SIZE_Y)/TILE_SIZE;
		
		return !((tiles[x1+(y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2+(y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3+(y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4+(y4*World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		
		int xStart = Camera.x>>4;
		int yStart = Camera.y>>4;
		int xFinal = xStart+(Game.WIDTH>>4);
		int yFinal = yStart+1+(Game.HEIGHT>>4);
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx<0 || yy<0 || xx>=WIDTH || yy>=HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
		
	}

}
