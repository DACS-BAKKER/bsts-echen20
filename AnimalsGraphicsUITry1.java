/*  Animal Graphics Unsuccessful Attempt
    Name: Ethan Chen
    Date Started: November 16, 2019
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.StringTokenizer;

public class AnimalsGraphicsUITry1 extends JFrame {

    //We talked about all the problems in this, but it pretty much comes down to me always having my code running at all
    // times. Using my for loop, I treated this like a GraphicsProgram from ACM in AP Comp Sci, but it is not action
    // oriented and doesn't work, unless run in debugger with breakpoints after every while loop that represents a
    // wait-for-click. However, the logic is technically correct.

    static JFrame f;
    static JPanel p;
    static JPanel p2;
    public static JLabel label;
    public static JTextField input;
    public static JButton play;
    public static JButton save;
    public static JButton wins;
    public static JButton reload;
    public static JButton quit;
    public static JButton submit;
    public static JButton yes;
    public static JButton no;

    public static int STATE = 100;

    static int won = 0;
    static int lost = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        f = new JFrame();
        f.setSize(500, 500);
        f.setVisible(true);

        p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        f.add(p);

        p2 = new JPanel(new BorderLayout());
        p2.setOpaque(true);
        p.add(p2, BorderLayout.CENTER);

        label = new JLabel();
        //  label.setBounds(100, 100, 300, 100);
        label.setText("Welcome to 21 Questions. I will guess any object you think of using yes-no questions. I can also get smarter over time. Would you like to wipe all previous data stored in my memory?");
        p2.add(label, BorderLayout.CENTER);
        Thread.sleep(100);

        input = new JTextField();
        // input.setBounds(125, 200, 300, 100);

        play = new JButton();
        //   play.setBounds(100, 225, 50, 50);
        play.setText("PLAY");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 3;
            }
        });

        wins = new JButton();
        //  wins.setBounds(175, 225, 50, 50);
        wins.setText("WINS");
        wins.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 4;
            }
        });


        save = new JButton();
        //  save.setBounds(225, 225, 50, 50);
        save.setText("SAVE");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 5;
            }
        });

        reload = new JButton();
        //  reload.setBounds(275, 225, 50, 50);
        reload.setText("RELOAD");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 6;
            }
        });


        quit = new JButton();
        //  quit.setBounds(325, 225, 75, 50);
        quit.setText("QUIT");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 7;
            }
        });

        submit = new JButton();
        //  submit.setBounds(200, 200, 300, 100);
        submit.setText("SUBMIT");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 2;
            }
        });


        yes = new JButton();
        //    yes.setBounds(100, 200, 150, 100);
        yes.setText("YES");
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 0;
            }
        });
        p.add(yes, BorderLayout.WEST);


        no = new JButton();
//        no.setBounds(250, 200, 150, 100);
        no.setText("NO");
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                STATE = 1;
            }
        });
        p.add(no, BorderLayout.EAST);

        Thread.sleep(1000);

        boolean memEntered = false;
        boolean rememberState = true;
        boolean playAgain = true;
        AnimalBT animalTree = new AnimalBT();

        while (STATE != 0 && STATE != 1) {

        }

        Thread.sleep(1000);

        if (STATE == 0) {
            label.setText("What would you like your first item to be?");
            p.removeAll();
            p.add(p2, BorderLayout.CENTER);
            p.add(label, BorderLayout.NORTH);
            p2.add(input, BorderLayout.CENTER);
            p.add(submit, BorderLayout.SOUTH);
            Thread.sleep(1000);
            while (STATE != 2) {

            }
            Thread.sleep(1000);
            STATE = 100;
            String firstItem = input.getText();
            animalTree.put("", firstItem);
        } else {
            animalTree = loadAnimalFile();
        }

        p.removeAll();
        p2.removeAll();

        Thread.sleep(1000);

        label.setText("");
        p.add(p2, BorderLayout.CENTER);
        p2.add(play, BorderLayout.CENTER);
        p.add(wins, BorderLayout.WEST);
        p.add(quit, BorderLayout.EAST);
        p.add(reload, BorderLayout.SOUTH);
        p.add(save, BorderLayout.NORTH);
        p2.add(label, BorderLayout.NORTH);

        Thread.sleep(1000);

        while (playAgain) {

            if (STATE == 3) {
                playAnimals(animalTree);
                STATE = 100;
            } else if (STATE == 4) {
                label.setText("Games won by you: " + lost + "  Games won by computer: " + won);
                STATE = 100;
            } else if (STATE == 7) {
                playAgain = false;
                STATE = 100;
            } else if (STATE == 5) {
                updateMemory(animalTree);
                label.setText("Thank you for making the computer smarter");
                STATE = 100;
            } else if (STATE == 6) {
                animalTree = loadAnimalFile();
                label.setText("You have reloaded the last save state.");
                STATE = 100;
            }

        }
    }


    public static void playAnimals(AnimalBT bt) throws IOException, InterruptedException {
        p.removeAll();
        p2.removeAll();
        p.add(p2, BorderLayout.CENTER);
        p.add(yes, BorderLayout.WEST);
        p.add(no, BorderLayout.EAST);
        p2.add(label, BorderLayout.CENTER);

        Node currNode = bt.topQ;
        Node lastNode = null;
        String currStringValue = (String) currNode.data;
        boolean winner = false;
        String newQKey = "";
        while (currNode != null && !winner) {
            lastNode = currNode;
            currStringValue = (String) currNode.data;
            if (currNode.right == null) {
                label.setText("Is your object: " + currStringValue + "? ");

            } else {
                label.setText(currStringValue + " ");

            }

            while (STATE != 0 && STATE != 1) {

            }

            if (STATE == 0) {
                if (currNode.right != null) {
                    currNode = currNode.right;
                    newQKey += "1";
                } else {
                    winner = true;
                }
            } else {
                if (currNode.left != null) {
                    currNode = currNode.left;
                    newQKey += "0";
                } else {
                    currNode = currNode.left;
                }
            }
        }
        STATE = 100;

        if (winner) {
            label.setText("Hooray, I win!");
            won = won + 1;
            p.removeAll();
            p2.removeAll();
            p.add(p2, BorderLayout.CENTER);
            p2.add(play, BorderLayout.CENTER);
            p.add(wins, BorderLayout.WEST);
            p.add(quit, BorderLayout.EAST);
            p.add(reload, BorderLayout.SOUTH);
            p.add(save, BorderLayout.NORTH);
            p2.add(label, BorderLayout.NORTH);
        } else {
            p.removeAll();
            p2.removeAll();
            p.add(p2, BorderLayout.CENTER);
            p.add(label, BorderLayout.NORTH);
            p.add(input, BorderLayout.CENTER);
            p.add(submit, BorderLayout.SOUTH);

            label.setText("Drat, I lost. What was your object? ");
            lost = lost + 1;

            while (STATE != 2) {

            }

            STATE = 100;
            String newObject = input.getText();
            label.setText("Type a Y/N question to distinguish " + currStringValue + " from " + newObject + ": ");

            while (STATE != 2) {

            }

            STATE = 100;
            String newQ = input.getText();
            p.remove(input);
            p.remove(submit);
            p.remove(label);
            p.add(yes, BorderLayout.WEST);
            p.add(no, BorderLayout.EAST);
            p2.removeAll();
            p2.add(label, BorderLayout.CENTER);

            label.setText("And what is the answer for " + newObject + "? ");


            while (STATE != 0 && STATE != 1) {

            }

            char answ;
            if (STATE == 0) {
                answ = 'y';
            } else {
                answ = 'n';
            }

            STATE = 100;
            p.removeAll();
            p2.removeAll();
            p.add(p2, BorderLayout.CENTER);
            p2.add(play, BorderLayout.CENTER);
            p.add(wins, BorderLayout.WEST);
            p.add(quit, BorderLayout.EAST);
            p.add(reload, BorderLayout.SOUTH);
            p.add(save, BorderLayout.NORTH);
            p2.add(label, BorderLayout.NORTH);
            label.setText("You have added " + newObject + " to the computer's memory");

            updateBST(bt, newObject, newQ, answ, newQKey, lastNode);

        }
    }

    public static void updateBST(AnimalBT bst, String newObject, String newQ, char answ, String newQKey, Node currNode) {

        bst.delete(newQKey);
        bst.put(newQKey, newQ);

        if (answ == 'y' || answ == 'Y') {
            bst.put(newQKey + "1", newObject);
            bst.put(newQKey + "0", currNode.data);
        } else {
            bst.put(newQKey + "0", newObject);
            bst.put(newQKey + "1", currNode.data);
        }
    }

    public static void updateMemory(AnimalBT bst) throws IOException {
        FileWriter fw = new FileWriter("/Users/echen20/Desktop/animalQuestions.txt", false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(String.valueOf(won));
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(lost));
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(bst.topQ.data));
        bw.newLine();
        bw.flush();

        for (int x = 1; x < bst.height(); x++) {
            String s = bst.level(x, 0, "");
            StringTokenizer tok = new StringTokenizer(s, "/");
            while (tok.hasMoreElements()) {
                String node = tok.nextToken();
                bw.write(node);
                bw.newLine();
                bw.flush();
            }
        }
    }

    public static AnimalBT loadAnimalFile() throws IOException {
        FileReader fr = new FileReader("/Users/echen20/Desktop/animalQuestions.txt");
        BufferedReader buf = new BufferedReader(fr);
        won = Integer.valueOf(buf.readLine());
        lost = Integer.valueOf(buf.readLine());
        AnimalBT newBT = new AnimalBT();

        String currLine = buf.readLine();
        newBT.put("", currLine);

        while ((currLine = buf.readLine()) != null) {
            StringTokenizer tok2 = new StringTokenizer(currLine, ",");
            String key = tok2.nextToken();
            String data = tok2.nextToken();
            newBT.put(key, data);
        }
        return newBT;
    }

}
