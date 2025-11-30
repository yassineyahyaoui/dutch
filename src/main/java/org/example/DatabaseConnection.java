package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:./data/quizdb;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    static {
        initializeDatabase();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create scores table
            String createScoresTable = """
                CREATE TABLE IF NOT EXISTS scores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom_joueur VARCHAR(100) NOT NULL,
                    score INT NOT NULL,
                    difficulte VARCHAR(20) NOT NULL,
                    date_partie DATE NOT NULL
                )
            """;
            stmt.execute(createScoresTable);

            // Create questions table
            String createQuestionsTable = """
                CREATE TABLE IF NOT EXISTS questions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    question TEXT NOT NULL,
                    choix1 VARCHAR(255) NOT NULL,
                    choix2 VARCHAR(255) NOT NULL,
                    choix3 VARCHAR(255) NOT NULL,
                    choix4 VARCHAR(255) NOT NULL,
                    bonne_reponse INT NOT NULL,
                    difficulte VARCHAR(20) NOT NULL
                )
            """;
            stmt.execute(createQuestionsTable);

            // Insert sample questions if table is empty
            if (isTableEmpty(conn, "questions")) {
                insertSampleQuestions(conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    private static void insertSampleQuestions(Connection conn) throws SQLException {
        String insertQuestions = """
            INSERT INTO questions (question, choix1, choix2, choix3, choix4, bonne_reponse, difficulte) VALUES
            -- FACILE (30 questions)
            ('Quelle est la capitale de la France?', 'Londres', 'Berlin', 'Paris', 'Madrid', 3, 'facile'),
            ('Combien de planètes dans notre système solaire?', '7', '8', '9', '10', 2, 'facile'),
            ('Quel est le plus grand océan du monde?', 'Atlantique', 'Indien', 'Arctique', 'Pacifique', 4, 'facile'),
            ('Quelle est la plus grande planète du système solaire?', 'Terre', 'Mars', 'Jupiter', 'Saturne', 3, 'facile'),
            ('Quel est le symbole chimique de loxygène?', 'Ox', 'Og', 'O', 'Oxy', 3, 'facile'),
            ('Qui a écrit "Roméo et Juliette"?', 'Molière', 'Victor Hugo', 'William Shakespeare', 'Charles Dickens', 3, 'facile'),
            ('Quelle est la couleur du ciel par temps clair?', 'Rouge', 'Vert', 'Bleu', 'Jaune', 3, 'facile'),
            ('Combien de jours y a-t-il dans une année bissextile?', '365', '366', '364', '367', 2, 'facile'),
            ('Quel animal est appelé "le roi de la jungle"?', 'Éléphant', 'Lion', 'Tigre', 'Girafe', 2, 'facile'),
            ('Quelle est la langue officielle du Brésil?', 'Espagnol', 'Portugais', 'Français', 'Anglais', 2, 'facile'),
            ('Quel est le plus grand mammifère du monde?', 'Éléphant', 'Girafe', 'Baleine bleue', 'Rhinocéros', 3, 'facile'),
            ('Combien de côtés a un triangle?', '2', '3', '4', '5', 2, 'facile'),
            ('Quel est le contraire de "chaud"?', 'Tiède', 'Froid', 'Brûlant', 'Doux', 2, 'facile'),
            ('Qui a peint la Joconde?', 'Van Gogh', 'Picasso', 'Léonard de Vinci', 'Monet', 3, 'facile'),
            ('Quelle est la capitale de lItalie?', 'Paris', 'Madrid', 'Rome', 'Berlin', 3, 'facile'),
            ('Combien de doigts a une main humaine?', '4', '5', '6', '7', 2, 'facile'),
            ('Quel fruit est jaune et courbé?', 'Pomme', 'Orange', 'Banane', 'Raisin', 3, 'facile'),
            ('Quel est le plus grand désert du monde?', 'Sahara', 'Gobi', 'Antarctique', 'Kalahari', 3, 'facile'),
            ('Quelle est la monnaie des États-Unis?', 'Euro', 'Livre', 'Dollar', 'Yen', 3, 'facile'),
            ('Combien dheures y a-t-il dans une journée?', '12', '24', '36', '48', 2, 'facile'),
            ('Quel est le plus long fleuve de France?', 'Seine', 'Rhône', 'Loire', 'Garonne', 3, 'facile'),
            ('Quel animal miaule?', 'Chien', 'Chat', 'Oiseau', 'Vache', 2, 'facile'),
            ('Quelle est la couleur des feuilles en été?', 'Rouge', 'Jaune', 'Vert', 'Marron', 3, 'facile'),
            ('Combien de continents y a-t-il sur Terre?', '5', '6', '7', '8', 3, 'facile'),
            ('Quel est le premier mois de lannée?', 'Mars', 'Janvier', 'Février', 'Avril', 2, 'facile'),
            ('Qui a inventé le téléphone?', 'Thomas Edison', 'Alexander Graham Bell', 'Nikola Tesla', 'Albert Einstein', 2, 'facile'),
            ('Quelle est la planète appelée "planète rouge"?', 'Vénus', 'Mars', 'Jupiter', 'Saturne', 2, 'facile'),
            ('Combien de lettres y a-t-il dans lalphabet français?', '24', '25', '26', '27', 3, 'facile'),
            ('Quel est le plus petit pays du monde?', 'Monaco', 'Vatican', 'Saint-Marin', 'Liechtenstein', 2, 'facile'),
            ('Quelle est la couleur du sang?', 'Bleu', 'Vert', 'Rouge', 'Jaune', 3, 'facile'),

            -- MOYEN (30 questions)
            ('Quel est lélément chimique avec le symbole Au?', 'Argent', 'Cuivre', 'Or', 'Aluminium', 3, 'moyen'),
            ('Dans quel pays se trouve la ville de Pompéi?', 'Grèce', 'Égypte', 'Italie', 'Turquie', 3, 'moyen'),
            ('Quel théorème relie les côtés dun triangle rectangle?', 'Théorème de Thalès', 'Théorème de Pythagore', 'Théorème dArchimède', 'Théorème de Euler', 2, 'moyen'),
            ('Qui a écrit "Les Misérables"?', 'Victor Hugo', 'Émile Zola', 'Gustave Flaubert', 'Alexandre Dumas', 1, 'moyen'),
            ('Quelle est la capitale de lAustralie?', 'Sydney', 'Melbourne', 'Canberra', 'Perth', 3, 'moyen'),
            ('Quel est le plus grand lac dAfrique?', 'Lac Victoria', 'Lac Tanganyika', 'Lac Malawi', 'Lac Turkana', 1, 'moyen'),
            ('En quelle année a été signée la Déclaration des Droits de lHomme?', '1776', '1789', '1799', '1815', 2, 'moyen'),
            ('Quel est le gaz le plus abondant dans latmosphère terrestre?', 'Oxygène', 'Dioxyde de carbone', 'Azote', 'Hydrogène', 3, 'moyen'),
            ('Qui a découvert lAmérique en 1492?', 'Vasco de Gama', 'Christophe Colomb', 'Marco Polo', 'Fernand de Magellan', 2, 'moyen'),
            ('Quelle est la langue la plus parlée au monde?', 'Anglais', 'Espagnol', 'Mandarin', 'Hindi', 3, 'moyen'),
            ('Quel est le plus haut sommet dAfrique?', 'Mont Kenya', 'Kilimandjaro', 'Mont Stanley', 'Ras Dashan', 2, 'moyen'),
            ('Qui a peint "La Nuit étoilée"?', 'Picasso', 'Monet', 'Van Gogh', 'Renoir', 3, 'moyen'),
            ('Quel est le plus long fleuve dEurope?', 'Danube', 'Rhin', 'Volga', 'Seine', 3, 'moyen'),
            ('En quelle année a eu lieu la Révolution française?', '1776', '1789', '1799', '1812', 2, 'moyen'),
            ('Quel est le symbole chimique du fer?', 'Fr', 'Fe', 'Fi', 'F', 2, 'moyen'),
            ('Qui a écrit "1984"?', 'Aldous Huxley', 'George Orwell', 'Ray Bradbury', 'H.G. Wells', 2, 'moyen'),
            ('Quelle est la capitale du Canada?', 'Toronto', 'Vancouver', 'Ottawa', 'Montréal', 3, 'moyen'),
            ('Combien de chromosomes possède lêtre humain?', '23', '46', '48', '52', 2, 'moyen'),
            ('Quel est le plus grand désert chaud du monde?', 'Gobi', 'Sahara', 'Kalahari', 'Arabie', 2, 'moyen'),
            ('Qui a composé la "Symphonie du Nouveau Monde"?', 'Mozart', 'Beethoven', 'Dvořák', 'Bach', 3, 'moyen'),
            ('Quel est le plus petit os du corps humain?', 'Fémur', 'Étrier', 'Marteau', 'Enclume', 2, 'moyen'),
            ('Quelle est la vitesse du son dans lair?', '300 m/s', '340 m/s', '400 m/s', '500 m/s', 2, 'moyen'),
            ('Qui a fondé Microsoft?', 'Steve Jobs', 'Bill Gates', 'Mark Zuckerberg', 'Larry Page', 2, 'moyen'),
            ('Quel est le plus grand pays dAmérique du Sud?', 'Argentine', 'Brésil', 'Chili', 'Colombie', 2, 'moyen'),
            ('Quelle planète a des anneaux visibles?', 'Jupiter', 'Mars', 'Saturne', 'Uranus', 3, 'moyen'),
            ('Qui a écrit "Le Petit Prince"?', 'Jules Verne', 'Marcel Proust', 'Antoine de Saint-Exupéry', 'Albert Camus', 3, 'moyen'),
            ('Quel est le plus grand os du corps humain?', 'Humérus', 'Fémur', 'Tibia', 'Bassin', 2, 'moyen'),
            ('Quelle est la monnaie du Japon?', 'Yuan', 'Won', 'Yen', 'Ringgit', 3, 'moyen'),
            ('Qui a peint "La Cène"?', 'Michel-Ange', 'Raphaël', 'Léonard de Vinci', 'Botticelli', 3, 'moyen'),
            ('Quel est le plus grand archipel du monde?', 'Japon', 'Philippines', 'Indonésie', 'Malaisie', 3, 'moyen'),

            -- DIFFICILE (25 questions)
            ('Quel est le plus long fleuve du monde?', 'Amazone', 'Nil', 'Mississippi', 'Yangtsé', 1, 'difficile'),
            ('Qui a découvert la pénicilline?', 'Marie Curie', 'Alexander Fleming', 'Louis Pasteur', 'Albert Einstein', 2, 'difficile'),
            ('Quelle est la température débullition de leau au niveau de la mer?', '90°C', '100°C', '110°C', '120°C', 2, 'difficile'),
            ('Qui a écrit "LÉtranger"?', 'Jean-Paul Sartre', 'Albert Camus', 'André Gide', 'François Mauriac', 2, 'difficile'),
            ('Quel est le plus grand canyon du monde?', 'Grand Canyon', 'Gorge du Verdon', 'Canyon de Colca', 'Canyon de Yarlung Tsangpo', 1, 'difficile'),
            ('En quelle année a été créé lONU?', '1919', '1945', '1950', '1939', 2, 'difficile'),
            ('Quel est le plus grand mammifère terrestre?', 'Éléphant dAfrique', 'Girafe', 'Rhinocéros', 'Hippopotame', 1, 'difficile'),
            ('Qui a formulé la théorie de la relativité?', 'Isaac Newton', 'Niels Bohr', 'Albert Einstein', 'Stephen Hawking', 3, 'difficile'),
            ('Quelle est la capitale de la Mongolie?', 'Oulan-Bator', 'Astana', 'Bichkek', 'Douchambe', 1, 'difficile'),
            ('Combien de côtes possède le corps humain?', '12 paires', '13 paires', '14 paires', '15 paires', 1, 'difficile'),
            ('Quel est le plus grand télescope optique du monde?', 'Hubble', 'Keck', 'Gran Telescopio Canarias', 'Very Large Telescope', 3, 'difficile'),
            ('Qui a composé "Les Quatre Saisons"?', 'Mozart', 'Vivaldi', 'Beethoven', 'Haydn', 2, 'difficile'),
            ('Quel est le point le plus profond des océans?', 'Fosse des Mariannes', 'Fosse de Porto Rico', 'Fosse des Kermadec', 'Fosse de Java', 1, 'difficile'),
            ('En quelle année a eu lieu la bataille de Waterloo?', '1805', '1815', '1825', '1830', 2, 'difficile'),
            ('Quel est le plus grand volcan actif dEurope?', 'Vésuve', 'Etna', 'Stromboli', 'Piton de la Fournaise', 2, 'difficile'),
            ('Qui a écrit "Cent ans de solitude"?', 'Jorge Luis Borges', 'Gabriel García Márquez', 'Pablo Neruda', 'Isabel Allende', 2, 'difficile'),
            ('Quelle est la vitesse de la lumière dans le vide?', '300 000 km/s', '150 000 km/s', '450 000 km/s', '600 000 km/s', 1, 'difficile'),
            ('Quel est le plus grand désert dAsie?', 'Désert de Gobi', 'Désert dArabie', 'Désert du Thar', 'Désert du Karakoum', 1, 'difficile'),
            ('Qui a peint "Guernica"?', 'Salvador Dalí', 'Pablo Picasso', 'Joan Miró', 'Diego Rivera', 2, 'difficile'),
            ('Quel est le plus long pont du monde?', 'Pont de Danyang-Kunshan', 'Pont de Hong Kong-Zhuhai-Macao', 'Pont du lac Pontchartrain', 'Viaduc de Millau', 1, 'difficile'),
            ('Quelle est la plus grande île de la Méditerranée?', 'Sicile', 'Sardaigne', 'Chypre', 'Crète', 1, 'difficile'),
            ('Qui a découvert les rayons X?', 'Marie Curie', 'Wilhelm Röntgen', 'Henri Becquerel', 'Max Planck', 2, 'difficile'),
            ('Quel est le plus grand parc national du monde?', 'Parc national du Northeast Greenland', 'Parc national de la Réunion', 'Parc national de Yellowstone', 'Parc national de Banff', 1, 'difficile'),
            ('Quelle est la monnaie de la Suisse?', 'Euro', 'Livre suisse', 'Franc suisse', 'Couronne suisse', 3, 'difficile'),
            ('Qui a écrit "À la recherche du temps perdu"?', 'Victor Hugo', 'Marcel Proust', 'Gustave Flaubert', 'Émile Zola', 2, 'difficile'),

            -- EXPERT (25 questions)
            ('Quel est le nom scientifique de lêtre humain moderne?', 'Homo erectus', 'Homo habilis', 'Homo sapiens', 'Homo neanderthalensis', 3, 'expert'),
            ('Qui a formulé le principe dincertitude?', 'Albert Einstein', 'Niels Bohr', 'Werner Heisenberg', 'Erwin Schrödinger', 3, 'expert'),
            ('Quelle est la température du zéro absolu?', '-273°C', '-459°F', '-273 K', '0 K', 1, 'expert'),
            ('Quel est le plus grand nombre premier connu en 2024?', '2^82,589,933 − 1', '2^74,207,281 − 1', '2^77,232,917 − 1', '2^57,885,161 − 1', 1, 'expert'),
            ('Qui a prouvé le dernier théorème de Fermat?', 'Andrew Wiles', 'Grigori Perelman', 'Terence Tao', 'John Nash', 1, 'expert'),
            ('Quelle est la particule élémentaire responsable de la masse?', 'Électron', 'Neutrino', 'Boson de Higgs', 'Quark', 3, 'expert'),
            ('Quel est le plus grand cratère dimpact visible sur Terre?', 'Chicxulub', 'Vredefort', 'Sudbury', 'Popigai', 2, 'expert'),
            ('Qui a développé la première théorie de lévolution?', 'Charles Darwin', 'Jean-Baptiste Lamarck', 'Alfred Russel Wallace', 'Gregor Mendel', 2, 'expert'),
            ('Quelle est la profondeur de la fosse des Mariannes?', '8 848 m', '10 911 m', '10 984 m', '11 034 m', 4, 'expert'),
            ('Quel est le plus ancien texte littéraire connu?', 'Code de Hammurabi', 'Épopée de Gilgamesh', 'Livre des Morts', 'Iliade', 2, 'expert'),
            ('Qui a découvert la structure de lADN?', 'Watson et Crick', 'Mendel', 'Franklin', 'Pauling', 1, 'expert'),
            ('Quelle est la vitesse de rotation de la Terre à léquateur?', '465 m/s', '1 000 m/s', '1 674 km/h', '2 000 km/h', 1, 'expert'),
            ('Quel est le plus grand nombre de décimales de π calculé?', '31 400 milliards', '50 000 milliards', '62 800 milliards', '100 000 milliards', 3, 'expert'),
            ('Qui a peint "Le Baiser"?', 'Gustav Klimt', 'Edvard Munch', 'Auguste Rodin', 'Pierre-Auguste Renoir', 1, 'expert'),
            ('Quelle est la température à la surface du Soleil?', '1 500°C', '5 500°C', '15 000°C', '5 500 K', 2, 'expert'),
            ('Quel est le plus ancien langage de programmation encore utilisé?', 'FORTRAN', 'COBOL', 'Lisp', 'C', 1, 'expert'),
            ('Qui a composé "LArt de la Fugue"?', 'Jean-Sébastien Bach', 'Georg Friedrich Haendel', 'Antonio Vivaldi', 'Wolfgang Amadeus Mozart', 1, 'expert'),
            ('Quelle est la distance de la Terre à la Lune?', '238 855 km', '384 400 km', '405 400 km', '363 300 km', 2, 'expert'),
            ('Quel est le plus grand nombre de dimensions prouvées en physique?', '3', '4', '10', '11', 4, 'expert'),
            ('Qui a écrit "Ulysse"?', 'Virginia Woolf', 'James Joyce', 'T.S. Eliot', 'Samuel Beckett', 2, 'expert'),
            ('Quelle est la pression au centre de la Terre?', '1 million datm', '3,6 millions datm', '10 millions datm', '100 millions datm', 2, 'expert'),
            ('Quel est le plus vieil organisme vivant sur Terre?', 'Séquoia', 'Bactérie', 'Posidonie', 'Éponge', 3, 'expert'),
            ('Qui a développé la théorie des jeux?', 'Alan Turing', 'John von Neumann', 'John Nash', 'Claude Shannon', 2, 'expert'),
            ('Quelle est la vitesse déchappement de la Terre?', '7,9 km/s', '11,2 km/s', '16,7 km/s', '42,1 km/s', 2, 'expert'),
            ('Quel est le plus grand nombre de chromosomes chez un organisme?', '46', '1260', '1042', '78', 2, 'expert')
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertQuestions);
        }
    }

    public static List<Question> getQuestions(String difficulte) {
        List<Question> questions = new ArrayList<>();

        // First, get ALL questions for this difficulty level
        String query = "SELECT * FROM questions WHERE difficulte = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, difficulte);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question question = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("choix1"),
                        rs.getString("choix2"),
                        rs.getString("choix3"),
                        rs.getString("choix4"),
                        rs.getInt("bonne_reponse")
                );
                questions.add(question);
            }

            // Shuffle the list randomly
            Collections.shuffle(questions);

            // Take only 10 questions (or all if less than 10) - FIXED LINE
            int maxQuestions = Math.min(10, questions.size());
            questions = new ArrayList<>(questions.subList(0, maxQuestions)); // Create new ArrayList

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Alternative method using SQL RAND() with better approach
    public static List<Question> getQuestionsAlternative(String difficulte) {
        List<Question> questions = new ArrayList<>();

        // Use a more reliable random approach
        String query = "SELECT * FROM questions WHERE difficulte = ? " +
                "ORDER BY RAND() * (id * 0.1 + 1) LIMIT 10";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, difficulte);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question question = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("choix1"),
                        rs.getString("choix2"),
                        rs.getString("choix3"),
                        rs.getString("choix4"),
                        rs.getInt("bonne_reponse")
                );
                questions.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to Java randomization
            return getQuestions(difficulte);
        }
        return questions;
    }

    public static void saveScore(String nomJoueur, int score, String difficulte) {
        String query = "INSERT INTO scores (nom_joueur, score, difficulte, date_partie) VALUES (?, ?, ?, CURRENT_DATE())";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomJoueur);
            stmt.setInt(2, score);
            stmt.setString(3, difficulte);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Score> getScores() {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM scores ORDER BY date_partie DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("id"),
                        rs.getString("nom_joueur"),
                        rs.getInt("score"),
                        rs.getString("difficulte"),
                        rs.getDate("date_partie")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    // Debug method to check how many questions are available per difficulty
    public static void printQuestionStats() {
        String[] difficultes = {"facile", "moyen", "difficile", "expert"};

        try (Connection conn = getConnection()) {
            for (String difficulte : difficultes) {
                String query = "SELECT COUNT(*) FROM questions WHERE difficulte = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, difficulte);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        System.out.println("Questions " + difficulte + ": " + rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add this method to DatabaseConnection class
    public static void resetDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Drop and recreate tables
            stmt.execute("DROP TABLE IF EXISTS scores");
            stmt.execute("DROP TABLE IF EXISTS questions");

            // Reinitialize
            initializeDatabase();

            System.out.println("Database reset successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}