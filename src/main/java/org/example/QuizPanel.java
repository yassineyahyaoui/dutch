package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizPanel extends JPanel {
    private QuizApplication parent;
    private String nomJoueur;
    private String difficulte;
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    private JLabel questionLabel;
    private JButton[] answerButtons;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JLabel progressLabel;
    private JButton quitButton;
    private Timer feedbackTimer;

    public QuizPanel(QuizApplication parent) {
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header panel with score and progress
        JPanel headerPanel = createHeaderPanel();

        // Main question panel
        JPanel questionPanel = createQuestionPanel();

        // Footer panel with controls
        JPanel footerPanel = createFooterPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(questionPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Timer for automatic question transition
        feedbackTimer = new Timer(2000, e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                nextQuestion();
            } else {
                endQuiz();
            }
        });
        feedbackTimer.setRepeats(false);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 245, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Score label
        scoreLabel = new JLabel("Score: 0/0");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(0, 51, 102));

        // Progress label
        progressLabel = new JLabel("Question: 0/0");
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        progressLabel.setForeground(new Color(102, 102, 102));
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Player info
        JLabel playerLabel = new JLabel("Joueur: " + (nomJoueur != null ? nomJoueur : ""));
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        playerLabel.setForeground(new Color(0, 51, 102));

        headerPanel.add(scoreLabel, BorderLayout.WEST);
        headerPanel.add(progressLabel, BorderLayout.EAST);
        headerPanel.add(playerLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createQuestionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 255), 2),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Question label
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        questionLabel.setForeground(new Color(0, 51, 102));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Answers panel
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        answersPanel.setBackground(Color.WHITE);
        answersPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        answerButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = createAnswerButton();
            final int answerIndex = i + 1;
            answerButtons[i].addActionListener(e -> checkAnswer(answerIndex));
            answersPanel.add(answerButtons[i]);
        }

        // Feedback label
        feedbackLabel = new JLabel("", JLabel.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        mainPanel.add(questionLabel, BorderLayout.NORTH);
        mainPanel.add(answersPanel, BorderLayout.CENTER);
        mainPanel.add(feedbackLabel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JButton createAnswerButton() {
        JButton button = new JButton();
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 51, 102));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 240), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(230, 240, 255));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(240, 245, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Control panel - only quit button now
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        controlPanel.setBackground(new Color(240, 245, 255));

        quitButton = createStyledButton("Retour à l'Accueil", new Color(244, 67, 54));
        quitButton.addActionListener(e -> {
            // Save score before quitting if quiz is in progress
            if (questions != null && currentQuestionIndex > 0) {
                DatabaseConnection.saveScore(nomJoueur, score, difficulte);
            }
            parent.showAccueil();
        });

        controlPanel.add(quitButton);
        footerPanel.add(controlPanel, BorderLayout.CENTER);

        return footerPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    public void startNewQuiz(String nomJoueur, String difficulte) {
        this.nomJoueur = nomJoueur;
        this.difficulte = difficulte;

        this.questions = DatabaseConnection.getQuestions(difficulte);
        this.currentQuestionIndex = 0;
        this.score = 0;

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Aucune question disponible pour ce niveau de difficulté",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            parent.showAccueil();
            return;
        }

        updateHeaderPlayerName();
        updateScoreDisplay();
        updateProgressDisplay();
        displayQuestion();
    }

    private void updateHeaderPlayerName() {
        Component[] components = ((JPanel) getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith("Joueur: ")) {
                    label.setText("Joueur: " + nomJoueur);
                    break;
                }
            }
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            endQuiz();
            return;
        }

        Question question = questions.get(currentQuestionIndex);

        // Format question text with proper HTML wrapping
        String questionText = "<html><div style='text-align: center; padding: 10px;'>" +
                question.getQuestion() + "</div></html>";
        questionLabel.setText(questionText);

        // Set answer buttons text
        answerButtons[0].setText("<html><div style='text-align: center;'>" + question.getChoix1() + "</div></html>");
        answerButtons[1].setText("<html><div style='text-align: center;'>" + question.getChoix2() + "</div></html>");
        answerButtons[2].setText("<html><div style='text-align: center;'>" + question.getChoix3() + "</div></html>");
        answerButtons[3].setText("<html><div style='text-align: center;'>" + question.getChoix4() + "</div></html>");

        // Reset buttons
        for (JButton button : answerButtons) {
            button.setEnabled(true);
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(0, 51, 102));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 200, 240), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        }

        feedbackLabel.setText("");
        feedbackLabel.setForeground(Color.BLACK);

        updateProgressDisplay();
    }

    private void checkAnswer(int selectedAnswer) {
        Question question = questions.get(currentQuestionIndex);

        // Disable all buttons
        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }

        if (selectedAnswer == question.getBonneReponse()) {
            score++;
            feedbackLabel.setText("✓ Bonne réponse!");
            feedbackLabel.setForeground(new Color(0, 150, 0));
            answerButtons[selectedAnswer - 1].setBackground(new Color(220, 255, 220));
            answerButtons[selectedAnswer - 1].setBorder(BorderFactory.createLineBorder(new Color(0, 200, 0), 3));
        } else {
            feedbackLabel.setText("✗ Mauvaise réponse!");
            feedbackLabel.setForeground(new Color(200, 0, 0));
            answerButtons[selectedAnswer - 1].setBackground(new Color(255, 220, 220));
            answerButtons[selectedAnswer - 1].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 3));
            // Highlight correct answer
            answerButtons[question.getBonneReponse() - 1].setBackground(new Color(220, 255, 220));
            answerButtons[question.getBonneReponse() - 1].setBorder(BorderFactory.createLineBorder(new Color(0, 200, 0), 3));
        }

        updateScoreDisplay();
        feedbackTimer.start();
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void endQuiz() {
        // Auto-save score when quiz ends
        saveScore();

        String resultMessage = String.format(
                "<html><div style='text-align: center;'><b>Quiz terminé!</b><br>Score final: %d/%d<br>Niveau: %s<br>",
                score, questions.size(), capitalizeFirstLetter(difficulte)
        );

        if (score == questions.size()) {
            resultMessage += "<font color='green'>Excellent! 🎉</font>";
        } else if (score >= questions.size() * 0.7) {
            resultMessage += "<font color='blue'>Très bien! 👍</font>";
        } else if (score >= questions.size() * 0.5) {
            resultMessage += "<font color='orange'>Pas mal! 😊</font>";
        } else {
            resultMessage += "<font color='red'>Peut mieux faire! 📚</font>";
        }

        resultMessage += "<br><br><small>Score automatiquement enregistré</small>";
        resultMessage += "</div></html>";

        feedbackLabel.setText(resultMessage);

        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }
    }

    // Add this helper method to capitalize the difficulty
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void updateScoreDisplay() {
        scoreLabel.setText(String.format("Score: %d/%d", score, questions.size()));
    }

    private void updateProgressDisplay() {
        progressLabel.setText(String.format("Question: %d/%d",
                currentQuestionIndex + 1, questions.size()));
    }

    private void saveScore() {
        if (nomJoueur != null && !nomJoueur.trim().isEmpty()) {
            DatabaseConnection.saveScore(nomJoueur, score, difficulte);
            System.out.println("Score enregistré: " + nomJoueur + " - " + score + " points - Niveau: " + difficulte);
        }
    }
}