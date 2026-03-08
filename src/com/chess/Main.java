package com.chess;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {

    private static final String USER_FILE = "users.log"; // File to store user data
    private static Map<String, String> users = new HashMap<>(); // Store user data in memory

    // Declare Swing components
    private static JFrame frame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JButton loginButton;
    private static JButton registerButton;
    private static JLabel messageLabel;
    private static JPanel loginPanel;
    private static JPanel menuPanel;
    private static JButton startGameButton;
    private static JButton creditsButton;
    private static JButton exitButton;
    private static BlackWidow gameInstance;

    // Method to load user data from the text file
    private static void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return; // No existing user file, start with an empty list
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                if (tokenizer.countTokens() == 2) {
                    String username = tokenizer.nextToken().trim();
                    String password = tokenizer.nextToken().trim();
                    users.put(username, password);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
            SwingUtilities.invokeLater(() -> messageLabel.setText("Error reading user file."));
        }
    }

    // Method to save user data to the text file
    private static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error writing to user file: " + e.getMessage());
            SwingUtilities.invokeLater(() -> messageLabel.setText("Error writing to user file."));
        }
    }

    // Method to handle user login (now using the in-memory map)
    public static boolean login(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful!");
            SwingUtilities.invokeLater(() -> messageLabel.setText("Login successful!"));
            return true;
        } else {
            System.out.println("Invalid username or password.");
            SwingUtilities.invokeLater(() -> messageLabel.setText("Invalid username or password."));
            return false;
        }
    }

    // Method to register a new user (now using the in-memory map and saving to file)
    public static void register(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            SwingUtilities.invokeLater(() -> messageLabel.setText("Username already exists."));
        } else {
            users.put(username, password);
            saveUsers(); // Save the new user to the file
            System.out.println("User registered successfully!");
            SwingUtilities.invokeLater(() -> messageLabel.setText("User registered successfully!"));
        }
    }

    // Method to display the main menu (Swing version)
    public static void showMenu() {
        loginPanel.setVisible(false);
        menuPanel.setVisible(true);
        frame.setTitle("Main Menu");
    }

    // Method to create and show the GUI
    public static void createAndShowGUI() {
        // Load user data from the file when the application starts
        loadUsers();

        // Create the frame
        frame = new JFrame("Login / Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new CardLayout());

        // --- Login Panel ---
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2, 5, 5));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(messageLabel);

        // --- Menu Panel ---
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 5, 5));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        startGameButton = new JButton("Start Game");
        creditsButton = new JButton("Credits");
        exitButton = new JButton("Exit");

        menuPanel.add(startGameButton);
        menuPanel.add(creditsButton);
        menuPanel.add(exitButton);
        menuPanel.setVisible(false);

        // Add panels to the frame
        frame.add(loginPanel);
        frame.add(menuPanel);

        // --- Event Listeners ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (login(username, password)) {
                    showMenu();
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                register(username, password);
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting Game...");
                startGame(); // Call the startGame method
            }
        });

        creditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("--- Credits ---");
                String creditsText = "Developed by: Vedant Kalla";
                JOptionPane.showMessageDialog(frame, creditsText, "Credits", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting...");
                System.exit(0);
            }
        });

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void startGame() {
        gameInstance = new BlackWidow();
        try {
            gameInstance.main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

