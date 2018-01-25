package de.juliand10.lk.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class PongGUI3 extends JFrame implements KeyListener {
	private int DELAY = 20;
	private int courtWidth = 600;
	private int courtHeight = 400;
	private Point courtLU = new Point(50, 50);
	private Point courtRU = new Point(courtLU.x + courtWidth - 1, courtLU.y);
	private Point courtLL = new Point(courtLU.x, courtLU.y + courtHeight -1);
	private Point courtRL = new Point(courtLU.x + courtWidth - 1, courtLU.y + courtHeight -1);
	private Point posBall = new Point((courtLU.x + courtWidth) / 2, (courtLU.y + courtHeight) / 2);
	private Point urposBall = new Point(posBall);
	private Color colorBackground = Color.black;
	private Color colorCourt = Color.red;
	private Color colorBall = Color.white;
	private int deltaX = 1;
	private int deltaY = 1;
	boolean initialized = false;

	public PongGUI3() {
		super("PONG");
		addKeyListener(this);
	}

	private void initCourt(Graphics g) {
		g.setColor(colorBackground);
		g.fillRect(courtLU.x, courtLU.y, courtWidth, courtHeight);
		drawCourt(g);
	}

	private void drawCourt(Graphics g) {
		g.setColor(colorCourt);
		g.drawRect(courtLU.x, courtLU.y, courtWidth, courtHeight);
		g.drawRect(courtLU.x - 1, courtLU.y - 1, courtWidth + 2, courtHeight + 2);
		g.drawRect(courtLU.x - 2, courtLU.y - 2, courtWidth + 4, courtHeight + 4);
	}

	private void drawBall(Graphics g) {
		g.setColor(colorBall);
		g.drawRect(posBall.x, posBall.y, 1, 1);
		g.setColor(colorBackground);
		g.drawRect(urposBall.x, urposBall.y, 1, 1);
		urposBall.x = posBall.x;
		urposBall.y = posBall.y;
		posBall.x += deltaX;
		posBall.y += deltaY;
	}

	private boolean checkCollision() {
		boolean collision = false;
		// collision lower line 
		if (posBall.y >= courtLL.y) {
			deltaX *= 1;
			deltaY *= -1;
			collision = true;
		}
		// collision upper line
		if (posBall.y <= courtLU.y) {
			deltaX *= 1;
			deltaY *= -1;
			collision = true;
		}
		// collision left line
		if (posBall.x <= courtLU.x) {
			deltaX *= -1;
			deltaY *= 1;
			collision = true;
		}
		// collision right line
		if (posBall.x >= courtRU.x) {
			deltaX *= -1;
			deltaY *= 1;
			collision = true;
		}
		if (collision) {
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaX;
			posBall.y += deltaY;
		}
		return collision;
	}

	public void paint(Graphics g) {
		if (!initialized) {
			initCourt(g);
			initialized = true;
		}
		drawBall(g);
		if (checkCollision()) {
			drawCourt(g);
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_Q) {
			deltaX *= -1;
			deltaY *= -1;
		}
		if (key == KeyEvent.VK_W) {
			deltaX = 1;
			deltaY = 2;
		}
		if (key == KeyEvent.VK_E) {
			deltaX = 2;
			deltaY = 1;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void startTimer() {
		TimerTask task = new TimerTask() {
			public void run() {
				repaint();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, DELAY);
	}

	public static void start() {
		PongGUI3 pong = new PongGUI3();
		pong.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pong.setExtendedState(JFrame.MAXIMIZED_BOTH);
		pong.setVisible(true);
		pong.startTimer();
	}

	public static void main(String[] args) {
		start();
	}
}