package org.example;

import java.sql.Date;

public class Score {
    private int id;
    private String nomJoueur;
    private int score;
    private String difficulte;
    private Date datePartie;

    public Score(int id, String nomJoueur, int score, String difficulte, Date datePartie) {
        this.id = id;
        this.nomJoueur = nomJoueur;
        this.score = score;
        this.difficulte = difficulte;
        this.datePartie = datePartie;
    }

    // Getters
    public int getId() { return id; }
    public String getNomJoueur() { return nomJoueur; }
    public int getScore() { return score; }
    public String getDifficulte() { return difficulte; }
    public Date getDatePartie() { return datePartie; }
}