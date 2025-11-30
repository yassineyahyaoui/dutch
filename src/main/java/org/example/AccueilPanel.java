package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccueilPanel extends JPanel {
    private QuizApplication parent;
    private JTextField nomField;
    private JComboBox<String> difficulteCombo;

    public AccueilPanel(QuizApplication parent) {
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 30));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        // Title
        JLabel titleLabel = new JLabel("QUIZ DE CULTURE GÉNÉRALE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 70, 140));

        // Form panel - using BoxLayout for better control
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Player name field
        JPanel nomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        nomPanel.setBackground(Color.WHITE);
        JLabel nomLabel = new JLabel("Nom du joueur:");
        nomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nomField = new JTextField(15); // Smaller field
        nomField.setFont(new Font("Arial", Font.PLAIN, 14));
        nomField.setMaximumSize(new Dimension(200, 30));
        nomPanel.add(nomLabel);
        nomPanel.add(nomField);

        // Difficulty combo
        JPanel difficultePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        difficultePanel.setBackground(Color.WHITE);
        JLabel difficulteLabel = new JLabel("Difficulté:");
        difficulteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        String[] difficultes = {"Facile", "Moyen", "Difficile", "Expert"};
        difficulteCombo = new JComboBox<>(difficultes);
        difficulteCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        difficulteCombo.setMaximumSize(new Dimension(150, 30));
        difficultePanel.add(difficulteLabel);
        difficultePanel.add(difficulteCombo);

        formPanel.add(nomPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(difficultePanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton commencerBtn = new JButton("Commencer");
        JButton scoresBtn = new JButton("Voir les Scores");

        // Style buttons
        commencerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        commencerBtn.setPreferredSize(new Dimension(140, 35));
        commencerBtn.setBackground(new Color(70, 130, 180));
        commencerBtn.setForeground(Color.WHITE);

        scoresBtn.setFont(new Font("Arial", Font.BOLD, 14));
        scoresBtn.setPreferredSize(new Dimension(140, 35));
        scoresBtn.setBackground(new Color(100, 149, 237));
        scoresBtn.setForeground(Color.WHITE);

        commencerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText().trim();
                String difficulte = ((String) difficulteCombo.getSelectedItem()).toLowerCase();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(AccueilPanel.this,
                            "Veuillez entrer votre nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                parent.startQuiz(nom, difficulte);
            }
        });

        scoresBtn.addActionListener(e -> parent.showScores());

        buttonPanel.add(commencerBtn);
        buttonPanel.add(scoresBtn);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}