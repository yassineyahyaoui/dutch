package org.example;

import javax.swing.*;
import java.awt.*;

public class QuizApplication extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private AccueilPanel accueilPanel;
    private QuizPanel quizPanel;
    private ScoresPanel scoresPanel;

    public QuizApplication() {
        // Reset database to ensure all questions are loaded
        DatabaseConnection.resetDatabase();

        setTitle("Quiz de Culture Générale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels
        accueilPanel = new AccueilPanel(this);
        quizPanel = new QuizPanel(this);
        scoresPanel = new ScoresPanel(this);

        // Add panels to card layout
        mainPanel.add(accueilPanel, "ACCUEIL");
        mainPanel.add(quizPanel, "QUIZ");
        mainPanel.add(scoresPanel, "SCORES");

        add(mainPanel);
        showAccueil();
    }

    public void showAccueil() {
        cardLayout.show(mainPanel, "ACCUEIL");
    }

    public void startQuiz(String nomJoueur, String difficulte) {
        quizPanel.startNewQuiz(nomJoueur, difficulte);
        cardLayout.show(mainPanel, "QUIZ");
    }

    public void showScores() {
        scoresPanel.loadScores();
        cardLayout.show(mainPanel, "SCORES");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApplication().setVisible(true);
        });
    }
}