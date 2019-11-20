/*  Animals Console Runner
    Name: Ethan Chen
    Date Started: November 12, 2019
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.StringTokenizer;

public class Animals {

    static int won = 0;
    static int lost = 0;

    public static void main(String[] args) throws IOException {
        boolean memEntered = false;
        boolean rememberState = true;
        boolean playAgain = true;
        AnimalBT animalTree = new AnimalBT();

        StdOut.println("Welcome to EChenna's Guessing Machine!");
        StdOut.println("Think of an item, and I will guess it");
        StdOut.println("using a series of yes-or-no questions");
        StdOut.println("I can also learn and grow smarter over time");

        while (!memEntered) { // makes sure something is entered correctly
            StdOut.print("Should I wipe all previous data? (Y)es, (N)o: "); // load from memory? setup
            char mem = StdIn.readLine().charAt(0);

            if (mem == 'n' || mem == 'N') {
                memEntered = true;
                rememberState = true;
            } else if (mem == 'y' || mem == 'Y') {
                memEntered = true;
                rememberState = false;
            } else {
                StdOut.println("Please enter Y or N");
            }
        }

        String firstItem;

        if (!rememberState) {
            StdOut.println("What is your initial object? "); // in case they said no, need initial object to add to tree
            firstItem = StdIn.readLine();
            animalTree.put("", firstItem);
        } else {
            animalTree = loadAnimalFile(); // load from txt file
        }

        while (playAgain) {
            StdOut.println();
            StdOut.print("(P)lay, (S)ave, (R)eload, (W)ins, (Q)uit? "); // input to ask what to do
            char mem2 = StdIn.readLine().charAt(0);
            StdOut.println();

            if (mem2 == 'p' || mem2 == 'P') {
                playAnimals(animalTree);
            } else if (mem2 == 'w' || mem2 == 'W') {
                StdOut.println("Games Won by Computer: " + won);
                StdOut.println("Games Won by You: " + lost);
            } else if (mem2 == 'q' || mem2 == 'Q') {
                playAgain = false;
            } else if (mem2 == 's' || mem2 == 'S') {
                updateMemory(animalTree);
                StdOut.println("Save successful. Thank you for making the computer smarter.");
            } else if(mem2 == 'R' || mem2 == 'r') {
                animalTree = loadAnimalFile();
                StdOut.println("You have reloaded the last save state.");
            }

        }
    }

    public static void playAnimals(AnimalBT bt) throws IOException {
        Node currNode = bt.topQ;
        Node lastNode = null;
        String currStringValue = (String) currNode.data;
        boolean winner = false;
        String newQKey = "";
        while(currNode != null && !winner) { // continue as long as the node exists or the person hasn't won
            lastNode = currNode; // need to know the node it reaches before it exits this loop
            currStringValue = (String) currNode.data;
            if(currNode.right == null) { // only need to check one side, because in this structure, one side empty means other is too
                StdOut.print("Is your object: " + currStringValue + "? "); // object
            } else {
                StdOut.print(currStringValue + " "); // question
            }

            char inp = StdIn.readLine().charAt(0);
            if(inp == 'y' || inp == 'Y') {
                if(currNode.right != null) {
                    currNode = currNode.right; // moves right
                    newQKey += "1"; // adds to current key
                } else {
                    winner = true; // end of line, means win
                }
            } else {
                if(currNode.left != null) {
                    currNode = currNode.left; // moves left
                    newQKey += "0"; // adds to current key
                } else {
                    currNode = currNode.left; // reaches null condition -> exits while loop
                }
            }
        }

        if(winner) {
            StdOut.println("Hooray, I win!");
            won = won + 1;
        } else {
            StdOut.print("Drat, I lost. What was your object? ");
            lost = lost + 1;
            String newObject = StdIn.readLine();
            StdOut.print("Type a Y/N question to distinguish " + currStringValue + " from " + newObject + ": "); // gets new object and quseiton
            String newQ = StdIn.readLine();
            StdOut.print("And what is the answer for " + newObject + "? ");
            char answ = StdIn.readLine().charAt(0);

            updateBT(bt, newObject, newQ, answ, newQKey, lastNode); // adds it to the tree

        }

    }

    public static void updateBT(AnimalBT bst, String newObject, String newQ, char answ, String newQKey, Node currNode) { // updates the animal tree with a new question and object

        bst.delete(newQKey); // removes old object
        bst.put(newQKey, newQ); // puts new question in old place

        if(answ == 'y' || answ == 'Y') {
            bst.put(newQKey + "1", newObject); // puts new object on right, old object on left of new question
            bst.put(newQKey + "0", currNode.data);
        } else {
            bst.put(newQKey + "0", newObject); // puts new object on left, old object on right of new question
            bst.put(newQKey + "1", currNode.data);
        }
    }

    public static void updateMemory(AnimalBT bst) throws IOException { // writes to txt file
        FileWriter fw = new FileWriter("/Users/echen20/Desktop/animalQuestions.txt", false);
        //FileWriter fw = new FileWriter("animalQuestions.txt", false); // for Mr. Bakker's use in testing, need to update file path to his computer's
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(String.valueOf(won)); // first two lines are wins and losses
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(lost));
        bw.newLine();
        bw.flush();
        bw.write(String.valueOf(bst.topQ.data)); // third line is the top question in tree
        bw.newLine();
        bw.flush();

        for(int x = 1; x<bst.height(); x++) {
            String s = bst.level(x, 0, ""); // gets the level order print (key and value (question or object) ), separated by / for each object
            // this needs to be level order print so when loaded, it puts things in tree from top down, and doesn't have any errors
            StringTokenizer tok = new StringTokenizer(s, "/"); // separates by /
            while(tok.hasMoreElements()) {
                String node = tok.nextToken();
                bw.write(node); // puts in file object by object
                bw.newLine();
                bw.flush();
            }
        }
    }

    public static AnimalBT loadAnimalFile() throws IOException { // loads a AnimalBT from the txt file
        FileReader fr = new FileReader("/Users/echen20/Desktop/animalQuestions.txt");
        //FileReader fr = new FileReader("animalQuestions.txt"); // for Mr. Bakker's use in testing, need to update file path to his computer's
        BufferedReader buf = new BufferedReader(fr);
        won = Integer.valueOf(buf.readLine()); // line 1 is computer wins
        lost = Integer.valueOf(buf.readLine()); // line 2 is computer losses
        AnimalBT newBT = new AnimalBT();

        String currLine = buf.readLine();
        newBT.put("", currLine); // line 3 is mother/root/topQ node

        while((currLine = buf.readLine()) != null) {
            StringTokenizer tok2 = new StringTokenizer(currLine, ","); // every other line, before comma is key, after is value
            String key = tok2.nextToken();
            String data = tok2.nextToken();
            newBT.put(key, data); // put in tree
        }
        return newBT; // return tree
    }

}
