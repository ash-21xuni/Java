/*Assignment 1 - Array Search App
 * Ashleigh Gordon
 * GRDASH007
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
 
public class GenericsKbBSTApp extends JFrame {
 
    private class TreeNode {
        KnowledgeBase data;
        TreeNode left;
        TreeNode right;
         
        TreeNode(KnowledgeBase data) {
            this.data = data;
        }
    }
     
    private JTextField fileNameField;
    private JTextField termField;
    private JTextField statementField;
    private JTextField confidenceScoreField;
    private JTextArea resultArea;
    private JButton loadButton;
    private JButton addButton;
    private JButton searchTermButton;
    private JButton searchTermAndSentenceButton;
    private JButton quitButton;
     
    private TreeNode root;
     
    public GenericsKbBSTApp() {
        super("Binary Search Tree App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(new TitledBorder("Please fill in the following fields: "));
         
        inputPanel.add(new JLabel("Enter File Name: "));
        fileNameField = new JTextField(20);
        inputPanel.add(fileNameField);
         
        inputPanel.add(new JLabel("Enter the term to search/replace:"));
        termField = new JTextField(20);
        inputPanel.add(termField);
        
        inputPanel.add(new JLabel("Enter the sentence to search/replace:"));
        statementField = new JTextField(20);
        inputPanel.add(statementField);
         
        inputPanel.add(new JLabel("Enter the confidence score:"));
        confidenceScoreField = new JTextField(20);
        inputPanel.add(confidenceScoreField);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBorder(new TitledBorder("Operations: "));
         
        loadButton = new JButton("1. Load a knowledge base from a file");
        addButton = new JButton("2. Add a new statement to the knowledge base");
        searchTermButton = new JButton("3. Search for a statement in the knowledge base by term");
        searchTermAndSentenceButton = new JButton("4. Search for a statement in the knowledge base by term and sentence");
        quitButton = new JButton("5. Quit");
         
        buttonPanel.add(loadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(searchTermButton);
        buttonPanel.add(searchTermAndSentenceButton);
        buttonPanel.add(quitButton);
         
        Border border = BorderFactory.createLineBorder(new Color(255, 209, 220), 5);
        getRootPane().setBorder(border);
        loadButton.setBackground(new Color(255, 209, 220));
        addButton.setBackground(new Color(255, 209, 220));
        searchTermButton.setBackground(new Color(255, 209, 220));
        searchTermAndSentenceButton.setBackground(new Color(255, 209, 220));
        quitButton.setBackground(new Color(255, 170, 165));
         
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("Results: "));
        
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        add(mainPanel);

        eventListeners();

        setVisible(true);
    }
     
    private void eventListeners() {
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = fileNameField.getText().trim();
                if (fileName.isEmpty()) {
                    displayResult("Please enter a file name.");
                    return;
                }
                loadFile(fileName);
            }
        });
         
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = termField.getText().trim();
                String sentence = statementField.getText().trim();
                String confidenceStr = confidenceScoreField.getText().trim();
                 
                if (term.isEmpty() || sentence.isEmpty() || confidenceStr.isEmpty()) {
                     displayResult("Please enter the following fields first: \nTerm\nStatement\nConfidence Score");
                     return;
                }
                 
                try {
                    double confidence = Double.parseDouble(confidenceStr);
                    addStatement(term, sentence, confidence);
                } catch (NumberFormatException ex) {
                    displayResult("Invalid confidence score. Please enter a valid number.");
                }
            }
        });
         
        searchTermButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = termField.getText().trim();
                if (term.isEmpty()) {
                    displayResult("Please enter the following fields first: \nTerm");
                    return;
                }
                searchByTerm(term);
            }
        });
         
        searchTermAndSentenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String term = termField.getText().trim();
                String statement = statementField.getText().trim();
                 
                if (term.isEmpty() || statement.isEmpty()) {
                    displayResult("Please enter the following fields first: \nTerm\nStatement");
                    return;
                }
                searchByTermAndSentence(term, statement);
            }
        });
         
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
     
    private void displayResult(String message) {
        resultArea.setText(message);
    }
     
    private void insert(KnowledgeBase data) {
        root = insertRecursive(root, data);
    }
     
    private TreeNode insertRecursive(TreeNode node, KnowledgeBase data) {
        if (node == null) return new TreeNode(data);
         
        int comparison = data.getTerm().compareTo(node.data.getTerm());
         
        if (comparison < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (comparison > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            if (data.getConfidenceScore() > node.data.getConfidenceScore()) {
                node.data = data;
            }
        }
         
        return node;
    }
     
    private KnowledgeBase search(String term) {
        return searchRecursive(root, term);
    }
     
    private KnowledgeBase searchRecursive(TreeNode node, String term) {
        if (node == null) return null;
         
        int comparison = term.compareTo(node.data.getTerm());
         
        if (comparison == 0) return node.data;
        if (comparison < 0) return searchRecursive(node.left, term);
        return searchRecursive(node.right, term);
    }
     
    public void loadFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    insert(new KnowledgeBase(
                        parts[0].trim(), 
                        parts[1].trim(), 
                        Double.parseDouble(parts[2].trim())
                    ));
                    count++;
                }
            }
            displayResult("Knowledge base loaded successfully. ");
        } catch (FileNotFoundException e) {
            displayResult("Error: File not found - " + fileName);
        } catch (Exception e) {
            displayResult("Error loading file: " + e.getMessage());
        }
    }
     
    public void addStatement(String term, String sentence, double confidenceScore) {
        KnowledgeBase kb = search(term);
         
        if (kb != null) {
            kb.updateStatement(sentence, confidenceScore);
            displayResult("Statement for term: " + term + "has been updated.");
        } else {
            insert(new KnowledgeBase(term, sentence, confidenceScore));
            displayResult("New statement " + term + " has been added.");
        }
    }
     
    public void searchByTerm(String term) {
        KnowledgeBase kb = search(term);
        if (kb != null) {
            displayResult("Statement found: " + kb.getStatement() + 
                         " (Confidence score: " + kb.getConfidenceScore() + ")");
        } else {
            displayResult("No statement found for term: " + term);
        }
    }
     
    public void searchByTermAndSentence(String term, String sentence) {
        KnowledgeBase kb = search(term);
        if (kb != null && kb.getStatement().equals(sentence)) {
            displayResult("Statement found with confidence score: " + kb.getConfidenceScore());
        } else {
            displayResult("Statement not found.");
        }
    }
     
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GenericsKbBSTApp();
            }
        });
    } 
}