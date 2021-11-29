package com.ZordS.Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	
	public String[] options = {"novo jogo", "carregar jogo", "sair"};
	
	public int option = 0, maxOption = options.length-1;
	
	public boolean up = false, down = false, enter = false;
	public static boolean pause=false, saveExists=false, saveGame=false;

	public void tick() {
		
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else{
			saveExists = false;
		}
			
		if(up) {
			up = false;
			option--;
			if(option < 0) {
				option = maxOption;
			}
		}
		
		if(down) {
			down = false;
			option++;
			if(option > maxOption) {
				option = 0;
			}
		}
		
		if(enter) {
			if(options[option] == "novo jogo") {
				
				Game.gameState = "NORMAL";
				file = new File("sevt.txt");
				file.delete();
				enter = false;
				
			}else if(options[option] == "carregar jogo") {
				
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
				enter = false;
				
			}else if(options[option] == "sair") {
				
				System.exit(1);
				
			}
		}
		
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i<val.length; i++) {
							val[i] -= encode;
							trans[1]+=val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e) {
					
				}
			}catch(FileNotFoundException e){
				
			}
		}
		return line;
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i<spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				Game.restart("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
		}
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try{
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int j=0; j<value.length; j++) {
				value[j]+=encode;
				current+=value[j];
			}
			try {
				write.write(current);
				if(i < val1.length -1) {
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial",Font.BOLD, 36));
		g.drawString("Teste", (Game.WIDTH*Game.SCALE)/2-70, (Game.HEIGHT*Game.SCALE)/2-170);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD, 26));
		g.drawString("Go", (Game.WIDTH*Game.SCALE)/2-50, (Game.HEIGHT*Game.SCALE)/2-100);
		g.drawString("Load", (Game.WIDTH*Game.SCALE)/2-50, (Game.HEIGHT*Game.SCALE)/2-50);
		g.drawString("Exit", (Game.WIDTH*Game.SCALE)/2-50, (Game.HEIGHT*Game.SCALE)/2);
		
		if(options[option] == "novo jogo") {

			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-90, (Game.HEIGHT*Game.SCALE)/2-100);
			
		}else if(options[option] == "carregar jogo") {

			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-90, (Game.HEIGHT*Game.SCALE)/2-50);
			
		}else if(options[option] == "sair") {

			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-90, (Game.HEIGHT*Game.SCALE)/2);
			
		}
		
	}
	
}
