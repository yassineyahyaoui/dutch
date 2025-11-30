package org.example;

public class Question {
    private int id;
    private String question;
    private String choix1;
    private String choix2;
    private String choix3;
    private String choix4;
    private int bonneReponse;

    public Question(int id, String question, String choix1, String choix2,
                    String choix3, String choix4, int bonneReponse) {
        this.id = id;
        this.question = question;
        this.choix1 = choix1;
        this.choix2 = choix2;
        this.choix3 = choix3;
        this.choix4 = choix4;
        this.bonneReponse = bonneReponse;
    }

    // Getters
    public int getId() { return id; }
    public String getQuestion() { return question; }
    public String getChoix1() { return choix1; }
    public String getChoix2() { return choix2; }
    public String getChoix3() { return choix3; }
    public String getChoix4() { return choix4; }
    public int getBonneReponse() { return bonneReponse; }
}

