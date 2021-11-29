package com.ZordS.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.ZordS.Entitites.Player;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(5, 185, 50, 5);
		g.fillRect(5, 190, 50, 5);
		g.setColor(Color.black);
		g.setColor(Color.red);
		g.fillRect(5, 185, (int)((Player.life/Player.maxLife)*50), 5);
		g.setColor(Color.blue);
		g.fillRect(5, 190, (int)((Player.mana/Player.maxMana)*50), 5);
	}
	
}
