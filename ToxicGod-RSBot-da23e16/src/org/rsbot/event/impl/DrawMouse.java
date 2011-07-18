package org.rsbot.event.impl;

import org.rsbot.bot.Bot;
import org.rsbot.client.Client;
import org.rsbot.client.input.Mouse;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.methods.Methods;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawMouse implements PaintListener {
	private final Client client;
	private int ang = 0;
	final Object lock = new Object();
	final ArrayList<Particle> particles = new ArrayList<Particle>();

	public DrawMouse(Bot bot) {
		client = bot.getClient();
	}

	public void onRepaint(final Graphics render) {
		Mouse mouse = client.getMouse();
		if (mouse != null) {
			int mouse_x = mouse.getX();
			int mouse_y = mouse.getY();
			int alph = 200;
			int s = 4;
			ang += 25;
			int color = Methods.random(0, 3);
			if (mouse.isPressed()) {
				synchronized (lock) {
					for (int i = 0; i < 50; i++, particles
							.add(new Particle(mouse_x, mouse_y, color)))
						;
				}
			}
			synchronized (lock) {
				Iterator<Particle> piter = particles.iterator();
				while (piter.hasNext()) {
					Particle part = piter.next();
					if (!part.handle(render)) {
						piter.remove();
					}
				}
			}
			render.setColor(new Color(0, 0, 0, alph));
			for (int i = 0; i < 2; i++, render.drawArc(mouse_x - s, mouse_y - s, s * 2, s * 2,
					0 + ang, 300), s++)
				;
			render.setColor(new Color(255, 255, 255, alph));
			for (int i = 0; i < 2; i++, render.drawArc(mouse_x - s, mouse_y - s, s * 2, s * 2,
					90 - ang, 300), s++)
				;
			render.setColor(new Color(0, 153, 51));
			for (int i = 0; i < 2; i++, render.drawArc(mouse_x - s, mouse_y - s, s * 2, s * 2,
					180 + ang, 300), s++)
				;
			render.setColor(new Color(0, 0, 0, alph));
			for (int i = 0; i < 2; i++, render.drawArc(mouse_x - s, mouse_y - s, s * 2, s * 2,
					270 - ang, 300), s++)
				;
		}
	}
	
	private static class Particle {

		private double posX;
		private double posY;
		private double movX;
		private double movY;
		private int alpha = 255, color = -1;
		java.util.Random generator = new java.util.Random();

		Particle(int pos_x, int pos_y, int color) {
			posX = (double) pos_x;
			posY = (double) pos_y;
			movX = ((double) generator.nextInt(40) - 20) / 16;
			movY = ((double) generator.nextInt(40) - 20) / 16;
			this.color = color;
		}

		public boolean handle(Graphics page) {
			alpha -= Methods.random(1, 7);
			if (alpha <= 0)
				return false;
			switch (color) {
			case 0:
				page.setColor(new Color(0, 0, 0, alpha));
				break;
			case 1:
				page.setColor(new Color(0, 153, 51));
				break;
			case 2:
				page.setColor(new Color(255, 255, 255, alpha));
				break;
			}
			page.drawLine((int) posX, (int) posY, (int) posX, (int) posY);
			posX += movX;
			posY += movY;
			return true;
		}
	}
}