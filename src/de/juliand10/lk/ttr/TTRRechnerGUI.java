package de.juliand10.lk.ttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TTRRechnerGUI extends JFrame implements ActionListener {
	private static final int SPIELTAGE_MAX = 10;
	private JLabel spieler1Anz, spieler2Anz;
	private JTextField spieler1Eing, spieler2Eing;
	private JButton best;
	private JRadioButton sieg, niederlage;
	private JCheckBox lSjahrher, u30Ej, u21, u16;
	private boolean gezeichnet = false;
	private int spieltage = 0;
	private int ttrWertStart = 0;
	private Graphics graph;

	public TTRRechnerGUI() {
		this.setSize(1000, 550);
		this.setLayout(null);
		this.setTitle("TTR Rechner");
		ButtonGroup buttongroup = new ButtonGroup();
		spieler1Anz = new JLabel("Spieler 1(Du)");
		spieler1Anz.setFont(new Font("Arial", Font.PLAIN, 22));
		spieler1Anz.setBounds(15, 5, 135, 50);
		spieler1Eing = new JTextField(20);
		spieler1Eing.setFont(new Font("Arial", Font.PLAIN, 24));
		spieler1Eing.setBounds(150, 5, 115, 50);
		spieler2Anz = new JLabel("Spieler 2");
		spieler2Anz.setFont(new Font("Arial", Font.PLAIN, 22));
		spieler2Anz.setBounds(15, 55, 135, 50);
		spieler2Eing = new JTextField(20);
		spieler2Eing.setFont(new Font("Arial", Font.PLAIN, 24));
		spieler2Eing.setBounds(150, 55, 115, 50);
		sieg = new JRadioButton("Sieg");
		sieg.setBounds(15, 105, 125, 50);
		sieg.setSelected(true);
		niederlage = new JRadioButton("Niederlage");
		niederlage.setBounds(140, 105, 125, 50);
		niederlage.setSelected(false);
		lSjahrher = new JCheckBox("Ein Jahr lang nicht mehr gespielt");
		lSjahrher.setBounds(15, 155, 250, 50);
		lSjahrher.setSelected(false);
		u30Ej = new JCheckBox("Unter 30 Einzel jemals gespielt");
		u30Ej.setBounds(15, 205, 250, 50);
		u30Ej.setSelected(false);
		u21 = new JCheckBox("Unter 21 Jahre alt");
		u21.setBounds(15, 255, 250, 50);
		u21.setSelected(false);
		u16 = new JCheckBox("Unter 16 Jahre alt");
		u16.setBounds(15, 305, 250, 50);
		u16.setSelected(false);
		best = new JButton("Bestaetigen");
		best.setBounds(15, 355, 250, 50);
		best.addActionListener(this);
		best.setFont(new Font("Arial", Font.PLAIN, 24));

		this.add(spieler1Anz);
		this.add(spieler1Eing);
		this.add(spieler2Anz);
		this.add(spieler2Eing);
		this.add(sieg);
		this.add(niederlage);
		buttongroup.add(sieg);
		buttongroup.add(niederlage);
		this.add(best);
		this.add(lSjahrher);
		this.add(u30Ej);
		this.add(u21);
		this.add(u16);

	}

	public void actionPerformed(ActionEvent ae) {
		double aenderungskonstante = 16.0;
		boolean istsieg = false;
		final int startX = 350;
		final int startY = 50;
		final int height = 400;
		final int width = 60 * SPIELTAGE_MAX;
		int ttrWertNeu = 0;
		int ttrWertAlt = 0;
		int ttrWertGegner = 0;

		if (ae.getSource() == this.best) {
			if (spieltage < SPIELTAGE_MAX) {
				spieltage++;
				if (sieg.isSelected()) {
					istsieg = true;
				} else {
					istsieg = false;
				}

				if (lSjahrher.isSelected()) {
					aenderungskonstante += 4;
				}
				if (u30Ej.isSelected()) {
					aenderungskonstante += 4;
				}
				if (u21.isSelected()) {
					aenderungskonstante += 4;
				}
				if (u16.isSelected()) {
					aenderungskonstante += 4;
				}

				try {
					ttrWertAlt = Integer.parseInt(spieler1Eing.getText());
					ttrWertGegner = Integer.parseInt(spieler2Eing.getText());
					ttrWertNeu = TTRRechner.rechnen(ttrWertAlt, ttrWertGegner, istsieg, aenderungskonstante);
					spieler1Eing.setText(String.valueOf(ttrWertNeu));

					if (!gezeichnet) {
						ttrWertStart = ttrWertAlt;
						graph = this.getContentPane().getGraphics();
						graph.setColor(Color.black);
						graph.drawRect(startX, startY, width, height);
						graph.drawLine(startX - 50, 0, startX, startY);
						graph.drawString("Spieltage", startX - 20, 20);
						graph.drawString("Punktzahl", startX - 70, 40);
						graph.drawString("ST5", startX + width / 2, 35);
						graph.drawString("ST10", startX + width, 35);
						graph.drawString(String.valueOf(ttrWertStart + height / 2), startX - 30, startY);
						graph.drawString(String.valueOf(ttrWertStart), startX - 30, startY + height / 2);
						graph.drawString(String.valueOf(ttrWertStart - height / 2), startX - 30, startY + height);
						gezeichnet = true;
					}

					graph.drawLine(startX + (60 * (spieltage - 1)), startY + height / 2 + ttrWertStart - ttrWertAlt,
							startX + (60 * spieltage), startY + height / 2 + ttrWertStart - ttrWertNeu);
					graph.drawString(String.valueOf(ttrWertNeu), startX + (60 * spieltage) - 15,
							startY + height / 2 + ttrWertStart - ttrWertNeu - 15);

				}

				catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Formatfehler");
					spieltage--;
				}
			} else {
				JOptionPane.showMessageDialog(null, "max. Spieltage erreicht");
			}
		}
		spieler1Eing.setEnabled(false);

	}

	public static void start() {
		TTRRechnerGUI ttrrechner;
		ttrrechner = new TTRRechnerGUI();
		ttrrechner.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ttrrechner.setVisible(true);
	}

}
