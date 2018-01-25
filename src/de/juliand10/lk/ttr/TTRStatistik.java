package de.juliand10.lk.ttr;

public class TTRStatistik
{
    private int[][] werte;
    private final int SPIELTAGE = 22;
    private final int SPIELER = 10;
    private final int STARTWERT = 1000;

    public TTRStatistik()
    {
        werte = new int[SPIELTAGE][SPIELER];
    }

    private void leeren()
    {
        // restliche Spieltage leeren
        for(int spieler=0;spieler<SPIELER;spieler++) {
            for(int spieltage=0;spieltage<SPIELTAGE;spieltage++) {   
                werte[spieltage][spieler] = 0;
            }

        }
    }

    public int rechnen(int spielwert1, int spielwert2, boolean sieg)
    {
        // TTRneu = TTRalt + Rundung auf ganze Zahlen[{(Resultat – Gewinnwahrscheinlichkeit) x Änderungskonstante} + Nachwuchsausgleich]
        // Resultat: 1 bei Sieg, 0 bei Niederlage
        // Gewinnwahrscheinlichkeit = 1/[1 + 10 hoch{(TTR_B – TTR_A)/150}]
        final double AENDERUNGSKONSTANTE = 16.0;
        final double NACHWUCHSAUSGLEICH = 0.0;
        int neuerwert = 0;

        double wahrscheinlichkeit = 1.0 / (1.0 + Math.pow(10.0, ((spielwert2 - spielwert1) / 150.0)));
        double resultat = 0.0;
        if (sieg)
            resultat = 1.0;

        neuerwert = spielwert1 + (int) (((resultat - wahrscheinlichkeit) * AENDERUNGSKONSTANTE) + NACHWUCHSAUSGLEICH);

        return neuerwert;
    }

    private void ausgebenSaison()
    {

        // Spaltenüberschrift ausgeben
        System.out.print("         \t|\t");
        for(int spieltage=0;spieltage<SPIELTAGE;spieltage++) {
            System.out.print("ST" + (spieltage+1) + "\t");
        }
        System.out.println("");
        // Striche ausgeben
        System.out.print("---------\t|\t");
        for(int spieltage=0;spieltage<SPIELTAGE;spieltage++) {
            System.out.print("----\t");
        }
        System.out.println("");
        // alle Spieltage ausgeben
        for(int spieler=0;spieler<SPIELER;spieler++) {
            System.out.print("Spieler " + (spieler+1) + "\t|\t");
            for(int spieltage=0;spieltage<SPIELTAGE;spieltage++) {
                System.out.print(werte[spieltage][spieler]);
                System.out.print("\t");
            }
            System.out.println("");
        }
    }

    private void ausgebenSieger()
    {
        int bestesErgebnis = 0;
        int sieger = 0;
        for(int spieler=0;spieler<SPIELER;spieler++) {
            if(werte[SPIELTAGE-1][spieler] > bestesErgebnis){
                bestesErgebnis = werte[SPIELTAGE-1][spieler];
                sieger = spieler+1;
            }
        }
        System.out.println("");
        System.out.print("Der Sieger ist Spieler " + sieger);
        System.out.println(" mit einem TTR-Wert von " + bestesErgebnis);

    }
    
    public void spielen()
    {
        
        leeren();
        
        int alterwert = 0;
        for(int spieler=0;spieler<SPIELER;spieler++){
            for(int spieltage=0;spieltage<SPIELTAGE;spieltage++) {
                if (spieltage == 0) {
                    alterwert = STARTWERT;
                }
                else {
                    alterwert = werte[spieltage-1][spieler];
                }
                werte[spieltage][spieler] = rechnen(alterwert, (int) (Math.random()*500)+1000, (Math.random() > 0.5));
            }
        }

        ausgebenSaison();

        ausgebenSieger();
    }


}
