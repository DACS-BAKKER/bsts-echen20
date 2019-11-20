/*  Successful Animal Graphics Runner
    Name: Ethan Chen
    Date Started: November 18, 2019
*/

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.StringTokenizer;

public class AnimalForm extends JFrame { // controls graphics for successful UI runner for animal!!!

    private JPanel contentPane;
    private JLabel questionLabel;
    private JLabel winsAndLosses;
    private JButton play;
    private JButton save;
    private JTextField inputField;
    private JButton yes;
    private JButton no;
    private JButton submit;
    private JButton reload;
    private int currSubmitStep; // describes if submitting question or animal
    private int won;
    private int lost;
    AnimalBT tree;
    Node currNode;
    String newQ;
    String newObj;
    String currKey;
    Node lastObj;



    public AnimalForm() throws IOException {
        tree = loadAnimalFile();
        setupMainPanel();
        setupLabels();
        setupField();
        setupButtons();
        setVisible(true);
        doPopUps();

        resetAll();
    }

    private AnimalBT loadAnimalFile() throws IOException { // file taken from initial Animals.java
        FileReader fr = new FileReader("/Users/echen20/Desktop/animalQuestions.txt");
        BufferedReader buf = new BufferedReader(fr);
        won = Integer.valueOf(buf.readLine());
        lost = Integer.valueOf(buf.readLine());
        AnimalBT newBT = new AnimalBT();

        String currLine = buf.readLine();
        newBT.put("", currLine);

        while((currLine = buf.readLine()) != null) {
            StringTokenizer tok2 = new StringTokenizer(currLine, ",");
            String key = tok2.nextToken();
            String data = tok2.nextToken();
            newBT.put(key, data);
        }
        return newBT;
    }

    private void setupMainPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(43, 179, 1));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }

    private void doPopUps() { // used for initial states, for example load data or not
        int reply = JOptionPane.showConfirmDialog(null, "Would you like to load the previous memory state?", "Load", JOptionPane.YES_NO_OPTION);
        if (reply != JOptionPane.YES_OPTION) {
            String firstObj = JOptionPane.showInputDialog("First object");
            tree = new AnimalBT(new Node("", firstObj));
            won = 0;
            lost = 0;
        }
    }

    private void setupLabels() {
        questionLabel = new JLabel("Welcome to Animals");
        questionLabel.setBounds(50, 30, 400, 50);
        contentPane.add(questionLabel);

        winsAndLosses = new JLabel("Computer Wins: " + won + " , Your Wins: " + lost);
        winsAndLosses.setBounds(50, 400, 300, 50);
        contentPane.add(winsAndLosses);
    }

    private void setupField() {
        inputField = new JTextField();
        inputField.setBounds(50, 100, 200, 200);
        contentPane.add(inputField);
    }

    private void setupButtons() {
        play = new JButton();
        play.setText("PLAY");
        play.setBounds(300, 400, 100, 50);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionLabel.setText((String) currNode.data);
                yes.setEnabled(true);
                no.setEnabled(true);
                submit.setEnabled(false); // makes it impossible to hit these buttons at this state
                inputField.setEnabled(false);
                play.setEnabled(false);
                save.setEnabled(false);
                reload.setEnabled(false);
            }
        });

        save = new JButton();
        save.setText("SAVE");
        save.setBounds(300, 325, 100, 50);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateMemory();
                    questionLabel.setText("Saved.");
                    resetAll();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        reload = new JButton();
        reload.setText("RELOAD");
        reload.setBounds(300, 250, 100, 50);
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tree = loadAnimalFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                questionLabel.setText("Reloaded.");
                resetAll();
            }
        });

        yes = new JButton();
        yes.setText("YES");
        yes.setBounds(300, 175, 100, 50);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currSubmitStep == 0) {
                    if (currNode.right != null) {
                        currNode = currNode.right;
                        currKey = currKey + "1";
                        questionLabel.setText((String) currNode.data);
                    } else {
                        questionLabel.setText("I won!");
                        won += 1;
                        resetAll();
                    }
                } else {
                    updateBST(newObj, newQ, 'y', currKey, lastObj);
                    questionLabel.setText("Try again?");
                    lost += 1;
                    resetAll();
                }
            }
        });

        no = new JButton();
        no.setText("NO");
        no.setBounds(300, 100, 100, 50);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currSubmitStep == 0) {
                    if (currNode.left != null) {
                        currNode = currNode.left;
                        currKey = currKey + "0";
                        questionLabel.setText((String) currNode.data);
                    } else {
                        lastObj = currNode;
                        questionLabel.setText("What was your object?");
                        no.setEnabled(false);
                        yes.setEnabled(false);
                        inputField.setEnabled(true);
                        submit.setEnabled(true);
                    }
                } else {
                    updateBST(newObj, newQ, 'n', currKey, lastObj);
                    questionLabel.setText("I lost. Try again?");
                    lost += 1;
                    resetAll();
                }
            }
        });

        submit = new JButton();
        submit.setText("SUBMIT");
        submit.setBounds(50, 325, 100, 50);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFieldText = inputField.getText();
                if(currSubmitStep == 0) {
                    newObj = inputFieldText;
                    questionLabel.setText("Question to discern " + newObj + " from " + lastObj.data + "?");
                    currSubmitStep = 1;
                } else if(currSubmitStep == 1) {
                    newQ = inputFieldText;
                    questionLabel.setText("Answer to that question?");
                    inputField.setEnabled(false);
                    submit.setEnabled(false);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                }
            }
        });


        contentPane.add(play);
        contentPane.add(save);
        contentPane.add(yes);
        contentPane.add(no);
        contentPane.add(submit);
        contentPane.add(reload);
    }

    private void resetAll() { // puts everything back into initial game state, happens at 'end' of game
        currKey = "";
        currNode = tree.topQ;
        newObj = "";
        newQ = "";
        currSubmitStep = 0;

        winsAndLosses.setText("Computer Wins: " + won + " , Your Wins: " + lost);
        reload.setEnabled(true);
        play.setEnabled(true);
        yes.setEnabled(false);
        no.setEnabled(false);
        submit.setEnabled(false);
        save.setEnabled(true);
        inputField.setEnabled(false);
    }

    private void updateMemory() throws IOException { // writes to external txt file
        FileWriter fw = new FileWriter("/Users/echen20/Desktop/animalQuestions.txt", false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(String.valueOf(won));
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(lost));
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(tree.topQ.data));
        bw.newLine();
        bw.flush();

        for(int x = 1; x<tree.height(); x++) {
            String s = tree.level(x, 0, "");
            StringTokenizer tok = new StringTokenizer(s, "/");
            while(tok.hasMoreElements()) {
                String node = tok.nextToken();
                bw.write(node);
                bw.newLine();
                bw.flush();
            }
        }
    }

    private void updateBST(String newObject, String newQ, char answ, String newQKey, Node currNode) { // updates the tree, taken from Animals.java

        tree.delete(newQKey);
        tree.put(newQKey, newQ);

        if(answ == 'y' || answ == 'Y') {
            tree.put(newQKey + "1", newObject);
            tree.put(newQKey + "0", currNode.data);
        } else {
            tree.put(newQKey + "0", newObject);
            tree.put(newQKey + "1", currNode.data);
        }
    }

}
