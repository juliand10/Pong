package de.juliand10.lk.ttr;

public class TTRRechner  {
	private static final double NACHWUCHSAUSGLEICH = 0.0;
	private static final double AENDERUNGSKONSTANTE = 16.0;

	public TTRRechner() {
	}

	public static int rechnen(int spielwert1, int spielwert2, boolean sieg, double aenderungskonstante) {
		
		int neuerwert = 0;

		double wahrscheinlichkeit = 1.0 / (1.0 + Math.pow(10.0, ((spielwert2 - spielwert1) / 150.0)));
		double resultat = 0.0;

		if (sieg) {
			resultat = 1.0;
		} else {
			resultat = 0.0;
		}

		neuerwert = spielwert1 + (int) (((resultat - wahrscheinlichkeit) * aenderungskonstante) + NACHWUCHSAUSGLEICH);
		
		return neuerwert;
	}
	
	public static int rechnen(int spielwert1, int spielwert2, boolean sieg, boolean m1Jn, boolean u30S, boolean u16, boolean u21) {
		int neuerwert = 0;
		
		double aenderungskonstante = AENDERUNGSKONSTANTE;
		if (m1Jn) aenderungskonstante += 4.0;
		if (u30S) aenderungskonstante += 4.0;
		if (u16) aenderungskonstante += 4.0;
		if (u21) aenderungskonstante += 4.0;
		
		neuerwert = rechnen(spielwert1, spielwert2, sieg, aenderungskonstante);
		
		return neuerwert;
	}
}
