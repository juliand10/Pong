package de.juliand10.lk.ttr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TTRSimulatorGUI extends JFrame implements ActionListener {
	private JLabel lblAnzSpieler, lblAnzSpieltage, lblStartTTR;
	private JTextField fldAnzSpieler, fldAnzSpieltage, fldStartTTR;
	private JButton btnSimulation, btnGrafik;
	private JCheckBox cbx1Jahrher, cbxU30Ej, cbxU21, cbxU16;
	private Graphics graph;

	private int[][] ttrWerte;

	public TTRSimulatorGUI() {
		this.setSize(1000, 650);
		this.setLayout(null);
		this.setTitle("TTR Simulator");
		lblAnzSpieler = new JLabel("Anzahl Spieler");
		lblAnzSpieler.setFont(new Font("Arial", Font.PLAIN, 22));
		lblAnzSpieler.setBounds(15, 35, 175, 50);
		fldAnzSpieler = new JTextField(20);
		fldAnzSpieler.setFont(new Font("Arial", Font.PLAIN, 24));
		fldAnzSpieler.setBounds(190, 35, 100, 50);
		lblAnzSpieltage = new JLabel("Anzahl Spieltage");
		lblAnzSpieltage.setFont(new Font("Arial", Font.PLAIN, 22));
		lblAnzSpieltage.setBounds(15, 85, 175, 50);
		fldAnzSpieltage = new JTextField(20);
		fldAnzSpieltage.setFont(new Font("Arial", Font.PLAIN, 24));
		fldAnzSpieltage.setBounds(190, 85, 100, 50);
		lblStartTTR = new JLabel("TTR Startwert");
		lblStartTTR.setFont(new Font("Arial", Font.PLAIN, 22));
		lblStartTTR.setBounds(15, 155, 175, 50);
		fldStartTTR = new JTextField(20);
		fldStartTTR.setFont(new Font("Arial", Font.PLAIN, 24));
		fldStartTTR.setBounds(190, 155, 100, 50);
		cbx1Jahrher = new JCheckBox("Ein Jahr lang nicht mehr gespielt");
		cbx1Jahrher.setBounds(15, 205, 250, 50);
		cbx1Jahrher.setSelected(false);
		cbxU30Ej = new JCheckBox("Unter 30 Einzel jemals gespielt");
		cbxU30Ej.setBounds(15, 255, 250, 50);
		cbxU30Ej.setSelected(false);
		cbxU21 = new JCheckBox("Unter 21 Jahre alt");
		cbxU21.setBounds(15, 305, 250, 50);
		cbxU21.setSelected(false);
		cbxU16 = new JCheckBox("Unter 16 Jahre alt");
		cbxU16.setBounds(15, 355, 250, 50);
		cbxU16.setSelected(false);
		btnSimulation = new JButton("Werte generieren");
		btnSimulation.setBounds(15, 420, 250, 50);
		btnSimulation.addActionListener(this);
		btnSimulation.setFont(new Font("Arial", Font.PLAIN, 24));
		btnGrafik = new JButton("Werte zeichnen");
		btnGrafik.setBounds(15, 500, 250, 50);
		btnGrafik.addActionListener(this);
		btnGrafik.setFont(new Font("Arial", Font.PLAIN, 24));

		this.add(lblAnzSpieler);
		this.add(fldAnzSpieler);
		fldAnzSpieler.setText("3");
		this.add(lblAnzSpieltage);
		this.add(fldAnzSpieltage);
		fldAnzSpieltage.setText("10");
		this.add(lblStartTTR);
		this.add(fldStartTTR);
		fldStartTTR.setText("1200");
		this.add(cbx1Jahrher);
		this.add(cbxU30Ej);
		this.add(cbxU21);
		this.add(cbxU16);
		this.add(btnSimulation);
		this.add(btnGrafik);
	}

	public void actionPerformed(ActionEvent event) {
		int spieler = Integer.parseInt(fldAnzSpieler.getText());
		int spieltage = Integer.parseInt(fldAnzSpieltage.getText());
		int startwert = Integer.parseInt(fldStartTTR.getText());

		// Werte generieren
		if (event.getSource() == btnSimulation) {

			ttrWerte = new int[spieltage + 1][spieler];

			for (int sp = 0; sp < spieler; sp++) {
				for (int st = 0; st < spieltage + 1; st++) {
					if (st == 0) {
						ttrWerte[st][sp] = startwert;
					} else {
						ttrWerte[st][sp] = TTRRechner.rechnen(ttrWerte[st - 1][sp], (int) (Math.random() * 500) + 1000,
								(Math.random() > 0.5), cbx1Jahrher.isSelected(), cbxU30Ej.isSelected(),
								cbxU21.isSelected(), cbxU16.isSelected());
					}
				}
			}
			JOptionPane.showMessageDialog(null, "TTR-Werte wurden generiert");
		}

		// Werte zeichnen
		if (event.getSource() == btnGrafik) {
			// Rahmen zeichnen
			final double startX = 350.;
			final double startY = 50.;
			final double height = 400.;
			final double width = 600.;
			graph = this.getContentPane().getGraphics();
			graph.setColor(Color.black);
			graph.clearRect(300, 0, 700, 650);
			graph.drawRect((int) startX, (int) startY, (int) width, (int) height);
			graph.drawLine((int) (startX - 50.), 0, (int) startX, (int) startY);
			graph.drawString("Spieltage", (int) (startX - 20.), 20);
			graph.drawString("TTR  " + String.valueOf((int) (startwert + height / 2.)), (int) (startX - 60.),
					(int) startY);
			graph.drawString("TTR  " + String.valueOf(startwert), (int) (startX - 60.), (int) (startY + height / 2.));
			graph.drawString("TTR  " + String.valueOf((int) (startwert - height / 2.)), (int) (startX - 60.),
					(int) (startY + height));

			// Werte zeichnen
			double deltaY = width / spieltage;
			for (int sp = 0; sp < spieler; sp++) {
				for (int st = 1; st < spieltage + 1; st++) {
					if (sp % 3 == 0)
						graph.setColor(Color.red);
					if (sp % 3 == 1)
						graph.setColor(Color.black);
					if (sp % 3 == 2)
						graph.setColor(Color.blue);
					graph.drawLine((int) (startX + (deltaY * (st - 1))),
							(int) (startY + height / 2. + startwert - ttrWerte[st - 1][sp]),
							(int) (startX + (deltaY * (st))),
							(int) (startY + height / 2. + startwert - ttrWerte[st][sp]));

					graph.drawString(String.valueOf(ttrWerte[st][sp]), (int) (startX + (deltaY * st) - 20.),
							(int) (startY + height / 2. + startwert - ttrWerte[st][sp] - 20.));
					try {
						Thread.sleep(100);
					} catch (Exception ex) {
						;
					}
				}

			}

		}

	}

	public static void start() {
		TTRSimulatorGUI ttrSimulator;
		ttrSimulator = new TTRSimulatorGUI();
		ttrSimulator.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ttrSimulator.setVisible(true);
	}
	
	public static void main(String[] args) {
		TTRSimulatorGUI.start();
	}

}
