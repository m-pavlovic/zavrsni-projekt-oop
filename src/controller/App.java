package controller;

import view.IndexPage;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IndexPage();

            }
        });
    }

    /*
    1. Omogucit da igru moze igrat samo osoba koja se registrirala
2. Napravit da se generira nova rijec iz kategorije ako igrac pogodi trenutnu rijec
3. Racunanje bodova
4. Spremanje bodova u datoteku
5. Ispis rezultata u highscore tablici
     */
}
