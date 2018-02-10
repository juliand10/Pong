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
	private float delay = 10;
	private int BALLSIZE = 15;
	private int MAX_STONES = 10;
	// Spielfeldgröße festlegen mit Eckpunkten
	private int courtWidth = 600;
	private int courtHeight = 400;
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
	boolean timerIsOn = false;

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
		g.fillRect(posPaddle.x, (posPaddle.y - paddleHeight / 2), 3, paddleHeight);
	}

	// Ball zeichnen (nur am Anfang)
	private void initBall(Graphics g) {
		g.setColor(colorBall);
		g.drawOval(posBall.x, posBall.y, BALLSIZE, BALLSIZE);
		long direction = Math.round(Math.random());
		if (direction == 0) {
			deltaXBall *= 1;
			deltaYBall *= 1;
		}
		if (direction == 1) {
			deltaXBall *= 1;
			deltaYBall *= -1;
		}
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
		g.drawOval(posBall.x, posBall.y, BALLSIZE, BALLSIZE);
		g.setColor(colorBackground);
		g.drawOval(urposBall.x, urposBall.y, BALLSIZE, BALLSIZE);
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
					g.drawRect(courtLU.x + i + 1, courtLU.y + j + 1, 3, 3);
				}
			}
		}
	}

	// Schläger zeichnen (bei Bewegung und Collision)
	private void drawPaddle(Graphics g) {
		if (posPaddle.y != urposPaddle.y) {
			g.setColor(colorBackground);
			g.fillRect(urposPaddle.x, (urposPaddle.y - paddleHeight / 2), 3, paddleHeight);
			g.setColor(colorPaddle);
			g.fillRect(posPaddle.x, (posPaddle.y - paddleHeight / 2), 3, paddleHeight);
			urposPaddle.x = posPaddle.x;
			urposPaddle.y = posPaddle.y;
		}
	}

	// Auf Collision an Wand prüfen
	private boolean checkCollisionBorder() {
		boolean collision = false;
		// Auf Collision an linker Wand prüfen, Timer stoppen und Game
		// Over-Meldung rausgeben
		if ((posBall.x) <= courtLU.x) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			timer.cancel();
			timer.purge();
			timerIsOn = false;
			posBall.x = courtLU.x + courtWidth / 2;
			posBall.y = courtLU.y + courtHeight / 2;
			JOptionPane.showMessageDialog(null, "Du hast es bis zu einem Delay von " + delay + " geschafft!");
		}
		// Auf Collision an oberer Wand prüfen
		if ((posBall.y) <= courtLU.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// Auf Collision an unterer Wand prüfen
		if ((posBall.y + BALLSIZE) >= courtLL.y) {
			deltaXBall *= 1;
			deltaYBall *= -1;
			collision = true;
		}
		// Auf Collision an rechter Wand prüfen
		if ((posBall.x + BALLSIZE) >= courtRU.x) {
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
		if (posBall.x <= (posPaddle.x + 2) && posBall.y >= posPaddle.y - paddleHeight / 2
				&& posBall.y <= posPaddle.y + paddleHeight / 2) {
			deltaXBall *= -1;
			deltaYBall *= 1;
			delay *= 0.9;
			collision = true;
			timer.cancel();
			timer.purge();
			timerIsOn = false;
			startTimer();
			timerIsOn = true;
		}
		if (collision) {
			posBall.x = urposBall.x;
			posBall.y = urposBall.y;
			posBall.x += deltaXBall;
			posBall.y += deltaYBall;
		}
		return collision;
	}

	// Erstes Zeichnen von Elementen
	public void paint(Graphics g) {
		graphics = getContentPane().getGraphics();
		if (!initialized) {
			initCourt(graphics);
			initStones(graphics);
			initPaddles(graphics);
			initBall(graphics);
			initialized = true;
		}
		// Synchronisierung fuer Linux
		Toolkit.getDefaultToolkit().sync();
	}

	// Reagieren auf Key Events
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			if (timerIsOn) {
				if (posPaddle.y - paddleHeight / 2 - paddleDelta > courtLU.y) {
					posPaddle.y -= paddleDelta;
				}
			}
		}
		if (key == KeyEvent.VK_S) {
			if (timerIsOn) {
				if (posPaddle.y + paddleHeight / 2 + paddleDelta < courtLL.y) {
					posPaddle.y += paddleDelta;
				}
			}
		}

		if (key == KeyEvent.VK_SPACE) {
			if (!timerIsOn) {
				startTimer();
				timerIsOn = true;
			}
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
				drawPaddle(graphics);
				drawBall(graphics);
				checkCollisionBorder();
				checkCollisionStones();
				checkCollisionPaddle();
				repaint();
			}
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, (long) delay);
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