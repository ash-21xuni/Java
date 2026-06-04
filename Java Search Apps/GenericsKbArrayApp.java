/*Assignment 1 - Array Search App
 * Ashleigh Gordon
 * GRDASH007
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

public class GenericsKbArrayApp extends JFrame{
    private KnowledgeBase[] knowledgeBase;
    private int size = 0;
    private static final int MAX_SIZE = 110000;
    
        
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

    public GenericsKbArrayApp() {
        super("Array Search App");
        knowledgeBase = new KnowledgeBase[MAX_SIZE];
        
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
         
        inputPanel.add(new JLabel("Enter the statement to search/replace:"));
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
        
        Border border = BorderFactory.createLineBorder(new Color(179, 235, 242), 5);
        getRootPane().setBorder(border);
        loadButton.setBackground(new Color(179, 235, 242));
        addButton.setBackground(new Color(179, 235, 242));
        searchTermButton.setBackground(new Color(179, 235, 242));
        searchTermAndSentenceButton.setBackground(new Color(179, 235, 242));
        quitButton.setBackground(new Color(152, 186, 213));
         
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
     
    public void loadFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String term = parts[0].trim();
                    String sentence = parts[1].trim();
                    double confidenceScore = Double.parseDouble(parts[2].trim());
                    
                    int index = findTermIndex(term);
                    if (index != -1) {
                        knowledgeBase[index].updateStatement(sentence, confidenceScore);
                    } else {
                        knowledgeBase[size++] = new KnowledgeBase(term, sentence, confidenceScore);
                    }
                }
            }
            displayResult("Knowledge base loaded successfully.");
        } catch (Exception e) {
            displayResult("Error loading file: " + e.getMessage());
        }
    }

    public void addStatement(String term, String sentence, double confidenceScore) {
        int index = findTermIndex(term);
        if (index != -1) {
            knowledgeBase[index].updateStatement(sentence, confidenceScore);
            displayResult("Statement updated.");
        } else {
            displayResult("Term not found.");
        }
    }

    public void searchByTerm(String term) {
        int index = findTermIndex(term);
        if (index != -1) {
            KnowledgeBase kb = knowledgeBase[index];
            displayResult("Statement found: " + kb.getStatement() + 
                             " (Confidence score: " + kb.getConfidenceScore() + ")");
        } else {
            displayResult("No statement found for term: " + term);
        }
    }

    public void searchByTermAndSentence(String term, String sentence) {
        int index = findTermIndex(term);
        if (index != -1 && knowledgeBase[index].getStatement().equals(sentence)) {
            displayResult("Statement found. Confidence score: " + 
                              knowledgeBase[index].getConfidenceScore());
        } else {
            displayResult("Statement not found.");
        }
    }

    private int findTermIndex(String term) {
        for (int i = 0; i < size; i++) {
            if (knowledgeBase[i].getTerm().equals(term)) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
         SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new GenericsKbArrayApp();
             }
         });
    }
}