package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ScoresPanel extends JPanel {
    private QuizApplication parent;
    private JTable scoresTable;
    private DefaultTableModel tableModel;
    private List<Score> scores;

    public ScoresPanel(QuizApplication parent) {
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title Panel
        JPanel titlePanel = createTitlePanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Buttons Panel
        JPanel buttonPanel = createButtonPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 245, 255));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("TABLEAU DES SCORES", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 51, 102));

        JLabel subtitleLabel = new JLabel("Historique des performances", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 245, 255));
        centerPanel.add(titleLabel, BorderLayout.CENTER);
        centerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        titlePanel.add(centerPanel, BorderLayout.CENTER);

        return titlePanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 255), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Table model with new "Niveau" column
        String[] columnNames = {"ID", "Nom du Joueur", "Score", "Niveau", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class; // Score column is Integer
            }
        };

        scoresTable = new JTable(tableModel);
        scoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scoresTable.setRowHeight(35);
        scoresTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoresTable.setShowGrid(true);
        scoresTable.setGridColor(new Color(230, 230, 230));
        scoresTable.setIntercellSpacing(new Dimension(1, 1));

        // Set column widths
        scoresTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        scoresTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nom
        scoresTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Score
        scoresTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Niveau
        scoresTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Date

        // Style table header
        JTableHeader header = scoresTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Style table
        scoresTable.setBackground(Color.WHITE);
        scoresTable.setForeground(new Color(51, 51, 51));

        // Add alternating row colors and custom rendering
        scoresTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 250, 255));
                    }
                }

                // Center align all columns
                setHorizontalAlignment(SwingConstants.CENTER);

                // Style score column
                if (column == 2 && value instanceof Integer) {
                    int scoreValue = (Integer) value;
                    if (scoreValue >= 8) {
                        setForeground(new Color(0, 128, 0)); // Green for high scores
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (scoreValue >= 5) {
                        setForeground(new Color(255, 140, 0)); // Orange for medium scores
                    } else {
                        setForeground(new Color(220, 0, 0)); // Red for low scores
                    }
                }
                // Style niveau column with colors
                else if (column == 3 && value instanceof String) {
                    String niveau = ((String) value).toLowerCase();
                    switch (niveau) {
                        case "facile":
                            setForeground(new Color(0, 128, 0)); // Green
                            break;
                        case "moyen":
                            setForeground(new Color(255, 140, 0)); // Orange
                            break;
                        case "difficile":
                            setForeground(new Color(220, 0, 0)); // Red
                            break;
                        case "expert":
                            setForeground(new Color(128, 0, 128)); // Purple
                            setFont(getFont().deriveFont(Font.BOLD));
                            break;
                        default:
                            setForeground(new Color(51, 51, 51));
                    }
                } else {
                    setForeground(new Color(51, 51, 51));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 255)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add stats panel
        JPanel statsPanel = createStatsPanel();

        tablePanel.add(statsPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        statsPanel.setBackground(new Color(245, 249, 255));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel statsLabel = new JLabel("📊 Statistiques: ");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsLabel.setForeground(new Color(0, 51, 102));

        JLabel countLabel = new JLabel();
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new Color(102, 102, 102));

        statsPanel.add(statsLabel);
        statsPanel.add(countLabel);

        // Store reference to update later
        statsPanel.putClientProperty("countLabel", countLabel);

        return statsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 245, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton trierBtn = createStyledButton("🏆 Trier par Score", new Color(76, 175, 80));
        JButton gagnantBtn = createStyledButton("👑 Afficher le Gagnant", new Color(255, 152, 0));
        JButton accueilBtn = createStyledButton("🏠 Retour à l'Accueil", new Color(33, 150, 243));

        trierBtn.addActionListener(e -> trierParScore());
        gagnantBtn.addActionListener(e -> afficherGagnant());
        accueilBtn.addActionListener(e -> parent.showAccueil());

        buttonPanel.add(trierBtn);
        buttonPanel.add(gagnantBtn);
        buttonPanel.add(accueilBtn);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
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

    public void loadScores() {
        scores = DatabaseConnection.getScores();
        updateTable();
        updateStats();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Score score : scores) {
            tableModel.addRow(new Object[]{
                    score.getId(),
                    score.getNomJoueur(),
                    score.getScore(),
                    capitalizeFirstLetter(score.getDifficulte()), // Capitalize difficulty
                    score.getDatePartie().toString()
            });
        }
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void updateStats() {
        // Update statistics label
        Component[] components = ((JPanel) getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel statsPanel = (JPanel) comp;
                Component[] statsComponents = statsPanel.getComponents();
                for (Component statComp : statsComponents) {
                    if (statComp instanceof JLabel && statComp != statsPanel.getComponent(0)) {
                        JLabel countLabel = (JLabel) statComp;

                        // Count scores by difficulty
                        long facileCount = scores.stream().filter(s -> "facile".equals(s.getDifficulte())).count();
                        long moyenCount = scores.stream().filter(s -> "moyen".equals(s.getDifficulte())).count();
                        long difficileCount = scores.stream().filter(s -> "difficile".equals(s.getDifficulte())).count();
                        long expertCount = scores.stream().filter(s -> "expert".equals(s.getDifficulte())).count();

                        String statsText = String.format(
                                "%d score(s) total - Facile: %d, Moyen: %d, Difficile: %d, Expert: %d",
                                scores.size(), facileCount, moyenCount, difficileCount, expertCount
                        );
                        countLabel.setText(statsText);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void trierParScore() {
        if (scores.isEmpty()) {
            showNoScoresMessage();
            return;
        }

        scores.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        updateTable();

        // Show confirmation
        JOptionPane.showMessageDialog(this,
                "Scores triés par ordre décroissant!",
                "Tri effectué", JOptionPane.INFORMATION_MESSAGE);
    }

    private void afficherGagnant() {
        if (scores.isEmpty()) {
            showNoScoresMessage();
            return;
        }

        Score gagnant = scores.stream()
                .max((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()))
                .orElse(scores.get(0));

        // Get difficulty color and icon
        String niveauColor = getNiveauColor(gagnant.getDifficulte());
        String niveauIcon = getNiveauIcon(gagnant.getDifficulte());

        // Create a nicer winner dialog
        JPanel winnerPanel = new JPanel(new BorderLayout(10, 10));
        winnerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel("👑", JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));

        JLabel textLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<b style='color: #0066cc; font-size: 16px;'>LE GAGNANT</b><br><br>" +
                        "<span style='color: #ff6600; font-size: 18px; font-weight: bold;'>" + gagnant.getNomJoueur() + "</span><br>" +
                        "avec un score de <span style='color: #009900; font-weight: bold;'>" + gagnant.getScore() + " points</span><br>" +
                        "au niveau <span style='color: " + niveauColor + "; font-weight: bold;'>" +
                        niveauIcon + " " + capitalizeFirstLetter(gagnant.getDifficulte()) + "</span><br>" +
                        "le " + gagnant.getDatePartie() +
                        "</div></html>"
        );
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        winnerPanel.add(iconLabel, BorderLayout.NORTH);
        winnerPanel.add(textLabel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, winnerPanel,
                "Félicitations au Gagnant!", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getNiveauColor(String difficulte) {
        switch (difficulte.toLowerCase()) {
            case "facile": return "#009900";
            case "moyen": return "#ff6600";
            case "difficile": return "#cc0000";
            case "expert": return "#9900cc";
            default: return "#666666";
        }
    }

    private String getNiveauIcon(String difficulte) {
        switch (difficulte.toLowerCase()) {
            case "facile": return "🟢";
            case "moyen": return "🟠";
            case "difficile": return "🔴";
            case "expert": return "🟣";
            default: return "⚪";
        }
    }

    private void showNoScoresMessage() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "📝 Aucun score enregistré<br>" +
                        "<small>Jouez d'abord au quiz pour voir des scores!</small>" +
                        "</div></html>",
                "Aucun Score", JOptionPane.INFORMATION_MESSAGE);
    }
}