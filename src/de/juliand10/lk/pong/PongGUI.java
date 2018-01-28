package de.juliand10.lk.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class PongGUI extends JFrame implements KeyListener {
	private long DELAY = 1;
	private int MAX_STONES = 10;
	// court
	private int courtWidth = 600;
	private int courtHeight = 400;
	private Point courtLU = new Point(50, 50);
	private Point courtRU = new Point(courtLU.x + courtWidth - 1, courtLU.y);
	private Point courtLL = new Point(courtLU.x, courtLU.y + courtHeight - 1);
	private Point courtRL = new Point(courtLU.x + courtWidth - 1, courtLU.y + courtHeight - 1);
	// paddles
	private int paddleHeight = courtHeight / 5;
	private Point posPaddleL = new Point(courtLU.x + courtWidth / 10, courtLU.y + courtHeight / 2);
	private Point urposPaddleL = new Point(posPaddleL);
	private Point posPaddleR = new Point(courtRU.x - courtWidth / 10, courtLU.y + courtHeight / 2);
	private Point urposPaddleR = new Point(posPaddleR);
	private int paddleDelta = 5;
	// ball
	private Point posBall = new Point(courtLU.x + courtWidth / 2, courtLU.y + courtHeight / 2);
	private Point urposBall = new Point(posBall);
	private int deltaXBall = 1;
	private int deltaYBall = 1;
	// stones
	private boolean[][] stones = new boolean[courtWidth - 2][courtHeight - 2];
	// colors
	private Color colorBackground = Color.black;
	private Color colorCourt = Color.red;
	private Color colorBall = Color.white;
	private Color colorStone = Color.green;
	private Color colorPaddle = Color.blue;
	boolean initialized = false;
	Graphics graphics = null;

	public PongGUI() {
		super("PONG");
		addKeyListener(this);
	}

	private void initCourt(Graphics g) {
		g.setColor(colorBackground);
		g.fillRect(courtLU.x, courtLU.y, courtWidth, courtHeight);
		drawCourt(g);
	}

	private void initStones(Graphics g) {
		int randomX, randomY;
		for (int i = 0; i < MAX_STONES; i++) {
			randomX = (int) (Math.random() * (courtWidth - 2));
			randomY = (int) (Math.random() * (courtHeight - 2));
			stones[randomX][randomY] = true;
		}
		drawStones(g);
	}

	private void initPaddles(Graphics g) {
		g.setColor(colorPaddle);
		g.drawRect(posPaddleL.x, posPaddleL.y - paddleHeight / 2, 1, paddleHeight);
		g.drawRect(posPaddleR.x, posPaddleR.y - paddleHeight / 2, 1, paddleHeight);
	}

	private void initBall(Graphics g) {
		g.setColor(colorBall);
		g.drawRect(posBall.x, posBall.y, 1, 1);
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
		posBall.x += deltaXBall;
		posBall.y += deltaYBall;
	}

	private void drawStones(Graphics g) {
		g.setColor(colorStone);
		for (int i = 0; i < (courtWidth - 2); i++) {
			for (int j = 0; j < (courtHeight - 2); j++) {
				if (stones[i][j] == true) {
					g.drawRect(courtLU.x + i + 1, courtLU.y + j + 1, 1, 1);
				}
			}
		}
	}

	private void drawPaddles(Graphics g) {
		if (posPaddleL.y != urposPaddleL.y) {
			g.setColor(colorBackground);
			g.drawRect(urposPaddleL.x, urposPaddleL.y - paddleHeight / 2, 1, paddleHeight);
			g.setColor(colorPaddle);
			g.drawRect(posPaddleL.x, posPaddleL.y - paddleHeight / 2, 1, paddleHeight);
			urposPaddleL.x = posPaddleL.x;
			urposPaddleL.y = posPaddleL.y;
		}
		if (posPaddleR.y != urposPaddleR.y) {
			g.setColor(colorBackground);
			g.drawRect(urposPaddleR.x, urposPaddleR.y - paddleHeight / 2, 1, paddleHeight);
			g.setColor(colorPaddle);
			g.drawRect(posPaddleR.x, posPaddleR.y - paddleHeight / 2, 1, paddleHeight);
			urposPaddleR.x = posPaddleR.x;
			urposPaddleR.y = posPaddleR.y;
		}
	}

	private boolean checkCollisionBorder() {
		boolean collision = false;
		// collision lower line
		if (posBall.y >= courtLL.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// collision upper line
		if (posBall.y <= courtLU.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// collision left line
		if (posBall.x <= courtLU.x) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			collision = true;
		}
		// collision right line
		if (posBall.x >= courtRU.x) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			collision = true;
		}
		if (collision) {
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaXBall;
			posBall.y += deltaYBall;
		}
		return collision;
	}

	private boolean checkCollisionStones() {
		boolean collision = false;
		int stoneX, stoneY;
		stoneX = posBall.x - courtLU.x - 1;
		stoneY = posBall.y - courtLU.y - 1;
		if (stones[stoneX][stoneY] == true) {
			collision = true;
			deltaXBall *= -1;
			deltaYBall *= -1;
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaXBall;
			posBall.y += deltaYBall;
		}
		return collision;
	}

	private boolean checkCollisionPaddles() {
		boolean collision = false;
		// check left paddle
		if (posBall.x <= posPaddleL.x && posBall.y >= posPaddleL.y - paddleHeight / 2
				&& posBall.y <= posPaddleL.y + paddleHeight / 2) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			collision = true;
		}
		// check right paddle
		if (posBall.x >= posPaddleR.x && posBall.y >= posPaddleR.y - paddleHeight / 2
				&& posBall.y <= posPaddleR.y + paddleHeight / 2) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			collision = true;
		}
		if (collision) {
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaXBall;
			posBall.y += deltaYBall;
		}
		return collision;
	}

	public void paint(Graphics g) {
		graphics = getContentPane().getGraphics();
		if (!initialized) {
			initCourt(graphics);
			initStones(graphics);
			initPaddles(graphics);
			initBall(graphics);
			initialized = true;
		}
		Toolkit.getDefaultToolkit().sync();
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_Q) {
			deltaXBall *= -1;
			deltaYBall *= -1;
		}
		if (key == KeyEvent.VK_W) {
			deltaXBall = 1;
			deltaYBall = 2;
		}
		if (key == KeyEvent.VK_E) {
			deltaXBall = 2;
			deltaYBall = 1;
		}
		if (key == KeyEvent.VK_UP) {
			if (posPaddleR.y - paddleHeight / 2 - paddleDelta > courtLU.y) {
				posPaddleL.y -= paddleDelta;
				posPaddleR.y -= paddleDelta;
			}
		}
		if (key == KeyEvent.VK_DOWN) {
			if (posPaddleR.y + paddleHeight / 2 + paddleDelta < courtLL.y) {
				posPaddleL.y += paddleDelta;
				posPaddleR.y += paddleDelta;
			}
		}

		if (key == KeyEvent.VK_SPACE) {
			startTimer();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void startTimer() {
		TimerTask task = new TimerTask() {
			long counter = 0;

			public void run() {
				drawPaddles(graphics);
				if (counter % 5 == 0) {
					drawBall(graphics);
					checkCollisionBorder();
					checkCollisionStones();
					checkCollisionPaddles();
				}
				repaint();
				counter++;
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, DELAY);
	}

	public static void start() {
		PongGUI pong = new PongGUI();
		pong.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pong.setExtendedState(JFrame.MAXIMIZED_BOTH);
		pong.setVisible(true);
	}

	public static void main(String[] args) {
		start();
	}
}
