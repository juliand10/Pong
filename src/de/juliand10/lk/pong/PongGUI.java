package de.juliand10.lk.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PongGUI extends JFrame implements KeyListener {
	private long delay = 5;
	private int MAX_STONES = 10;
	// Spielfeldgröße festlegen mit Eckpunkten
	private int courtWidth = 1000;
	private int courtHeight = 600;
	private Point courtLU = new Point(50, 50);
	private Point courtRU = new Point(courtLU.x + courtWidth - 1, courtLU.y);
	private Point courtLL = new Point(courtLU.x, courtLU.y + courtHeight - 1);
	// private Point courtRL = new Point(courtLU.x + courtWidth - 1, courtLU.y +
	// courtHeight - 1);
	// Schlägergröße und Startposition
	private int paddleHeight = courtHeight / 5;
	private Point posPaddle = new Point(courtLU.x + courtWidth / 10, courtLU.y + courtHeight / 2);
	private Point urposPaddle = new Point(posPaddle);
	private int paddleDelta = 5;
	// Ballposition und Hintergrund nachzeichnen
	private Point posBall = new Point(courtLU.x + courtWidth / 2, courtLU.y + courtHeight / 2);
	private Point urposBall = new Point(posBall);
	private int deltaXBall = 1;
	private int deltaYBall = 1;
	// Hindernis Array
	private boolean[][] stones = new boolean[courtWidth - 2][courtHeight - 2];
	// Farben setzen
	private Color colorBackground = Color.black;
	private Color colorCourt = Color.red;
	private Color colorBall = Color.white;
	private Color colorStone = Color.green;
	private Color colorPaddle = Color.blue;
	boolean initialized = false;
	Graphics graphics = null;
	Timer timer;

	public PongGUI() {
		super("PONG");
		addKeyListener(this);
	}

	// Spielfeld zeichen
	private void initCourt(Graphics g) {
		g.setColor(colorBackground);
		g.fillRect(courtLU.x, courtLU.y, courtWidth, courtHeight);
		drawCourt(g);
	}

	// Hindernisse ermitteln
	private void initStones(Graphics g) {
		int randomX, randomY;
		for (int i = 0; i < MAX_STONES; i++) {
			randomX = (int) (Math.random() * (courtWidth - 2));
			randomY = (int) (Math.random() * (courtHeight - 2));
			stones[randomX][randomY] = true;
		}
		drawStones(g);
	}

	// Schläger zeichen
	private void initPaddles(Graphics g) {
		g.setColor(colorPaddle);
		g.drawRect(posPaddle.x, posPaddle.y - paddleHeight / 2, 1, paddleHeight);
	}

	// Ball zeichnen (nur am Anfang)
	private void initBall(Graphics g) {
		g.setColor(colorBall);
		g.drawOval(posBall.x, posBall.y, 5, 5);
	}

	// Spielfeld zeichen (bei Collision)
	private void drawCourt(Graphics g) {
		g.setColor(colorCourt);
		g.drawRect(courtLU.x, courtLU.y, courtWidth, courtHeight);
		g.drawRect(courtLU.x - 1, courtLU.y - 1, courtWidth + 2, courtHeight + 2);
		g.drawRect(courtLU.x - 2, courtLU.y - 2, courtWidth + 4, courtHeight + 4);
	}

	// Ball zeichnen (bis Collision)
	private void drawBall(Graphics g) {
		g.setColor(colorBall);
		g.drawOval(posBall.x, posBall.y, 5, 5);
		g.setColor(colorBackground);
		g.drawOval(urposBall.x, urposBall.y, 5, 5);
		urposBall.x = posBall.x;
		urposBall.y = posBall.y;
		posBall.x += deltaXBall;
		posBall.y += deltaYBall;
	}

	// Hindernisse zeichnen
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

	// Schläger zeichnen (bei Bewegung und Collision)
	private void drawPaddles(Graphics g) {
		if (posPaddle.y != urposPaddle.y) {
			g.setColor(colorBackground);
			g.drawRect(urposPaddle.x, urposPaddle.y - paddleHeight / 2, 1, paddleHeight);
			g.setColor(colorPaddle);
			g.drawRect(posPaddle.x, posPaddle.y - paddleHeight / 2, 1, paddleHeight);
			urposPaddle.x = posPaddle.x;
			urposPaddle.y = posPaddle.y;
		}
	}

	// Auf Collision an Wand prüfen
	private boolean checkCollisionBorder() {
		boolean collision = false;
		// Auf Collision an linker Wand prüfen und den Timer stoppen falls
		// Collision
		if (posBall.x <= courtLU.x) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			// collision = true;
			timer.cancel();
			timer.purge();
			posBall.x = courtLU.x + courtWidth / 2;
			posBall.y = courtLU.y + courtHeight / 2;
			JOptionPane.showMessageDialog(null, "Du hast es zur " + (5 - delay) + "-fachen Geschwindigkeit geschafft!");
		}
		// Auf Collision an oberer Wand prüfen
		if (posBall.y <= courtLU.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// Auf Collision an unterer Wand prüfen
		if (posBall.y >= courtLL.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// Auf Collision an rechter Wand prüfen
		if (posBall.x >= courtRU.x) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			delay *= 0.95;
			collision = true;
			timer.cancel();
			timer.purge();
			startTimer();
		}
		if (collision) {
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaXBall;
			posBall.y += deltaYBall;
		}
		return collision;
	}

	// Auf Collision an Hindernissen prüfen
	private boolean checkCollisionStones() {
		boolean collision = false;
		int stoneX, stoneY;
		stoneX = posBall.x - courtLU.x - 1;
		stoneY = posBall.y - courtLU.y - 1;
		if (stones[stoneX][stoneY]) {
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

	// Auf Collision an Schläger prüfen und wenn ja, Ballrichtung ändern
	private boolean checkCollisionPaddle() {
		boolean collision = false;
		if (posBall.x <= posPaddle.x && posBall.y >= posPaddle.y - paddleHeight / 2
				&& posBall.y <= posPaddle.y + paddleHeight / 2) {
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

	// Erstes Zeichen von Elementen
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

	// Reagieren auf Key Events
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			if (posPaddle.y - paddleHeight / 2 - paddleDelta > courtLU.y) {
				posPaddle.y -= paddleDelta;
			}
		}
		if (key == KeyEvent.VK_S) {
			if (posPaddle.y + paddleHeight / 2 + paddleDelta < courtLL.y) {
				posPaddle.y += paddleDelta;
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

	// Spielbeginn
	public void startTimer() {
		TimerTask task = new TimerTask() {

			public void run() {
				drawPaddles(graphics);
				drawBall(graphics);
				checkCollisionBorder();
				checkCollisionStones();
				checkCollisionPaddle();
				repaint();
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, delay);
	}

	// Fenster anzeigen
	public static void start() {
		PongGUI pong = new PongGUI();
		pong.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pong.setExtendedState(JFrame.MAXIMIZED_BOTH);
		pong.setVisible(true);
	}

	// Startmethode
	public static void main(String[] args) {
		start();
	}
}
