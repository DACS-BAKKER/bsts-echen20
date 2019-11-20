/*  Node Class
    Name: Ethan Chen
    Date Started: November 12, 2019
*/

import edu.princeton.cs.algs4.StdOut;

public class AnimalBT<Value> { // this is just a modified Binary Search Tree specific for Animals

    //Keys are now a string such as "10011", which translates, in orders of questions to "yes-no-no-yes-yes"
    // or alternatively for a tree, "right-left-left-right-right"
    // this tree only works under assumption that nodes that are required to exist for a key do exist
    // for example, key "1001" must exist for "10011" to exist

    Node<String> topQ;
    private String levelString = ""; // used to aid in printing out level order without printing to console, later exported to external file

    public AnimalBT(Node parent) {
        topQ = parent;
    }

    public AnimalBT() {

    }

    public boolean isEmpty() {
        if(topQ == null) {
            return true;
        }
        return false;
    }

    public void delete(String key) { // new key structure removes need for recursive call

        Node currNode = topQ;
        Node lastNode = null;
        char[] keyArray = key.toCharArray();

        if(key == "") {
            topQ = null;
            return;
        }

        for(char x : keyArray) { // this process makes currNode the node to delete
            lastNode = currNode; // want to save one before to set pointer to null, not object
            if(x == '1') {
                currNode = currNode.right;
            } else {
                currNode = currNode.left;
            }
        }

        if(keyArray[keyArray.length-1] == '1') { // checks to see where the last pointer points to
            lastNode.right = null;
        } else {
            lastNode.left = null;
        }

    }

    public int height() {
        return height(topQ);
    } // same as from BST

    private int height(Node currNode) { // helper
        if (isEmpty()) {
            return 0;
        }

        if (currNode == null) {
            return 0;
        } else {
            int leftHeight = 1 + height(currNode.left);
            int rightHeight = 1 + height(currNode.right);

            if (leftHeight > rightHeight) {
                return leftHeight;
            } else {
                return rightHeight;
            }

        }
    }

    public String level(int level, int location, String nodes) {
        return level(topQ, level, location, nodes);
    } // same as BST, except puts in instance variable levelString instead of printing to console

    private String level(Node currNode, int level, int location, String nodes) { // helper
        printLevel(currNode, level, location, nodes);
        String temp = levelString;
        levelString = ""; // resetting instance variable
        return temp;
    }

    private void printLevel(Node currNode, int level, int location, String nodes) {
        if (!isEmpty()) {
            if (level == location) {
                levelString += currNode.stringKey + "," + currNode.data + "/";
            } else {
                if (currNode.left != null) {
                    printLevel(currNode.left, level, location + 1, nodes);
                }
                if (currNode.right != null) {
                    printLevel(currNode.right, level, location + 1, nodes);
                }
            }
        }
    }

    public void put(String key, Value val) { // traverses based on key sequence and puts at end, same traversal as delete

        if(key == "") {
            topQ = new Node(key, val);
            return;
        }

        Node currNode = topQ;
        Node lastNode = null;
        char[] keyArray = key.toCharArray();

        for(char x : keyArray) {
            lastNode = currNode;

            if(x == '1') {
                currNode = currNode.right;
            } else {
                currNode = currNode.left;
            }
        }

        if(keyArray[keyArray.length-1] == '1') {
            lastNode.right = new Node(key, val);
        } else {
            lastNode.left = new Node(key, val);
        }
    }

    public static void main(String[] args) {
        AnimalBT a = new AnimalBT(new Node("", "top"));
        a.put("0", "l");
        a.put("1", "r");
        a.put("00", "ll");
        a.put("10", "rl");
        a.put("01", "lr");
        a.put("11", "rr");
        a.put("000", "lll");

        StdOut.println(a.level(a.topQ, 0, 0, ""));
        StdOut.println(a.level(a.topQ, 1, 0, ""));
        StdOut.println(a.level(a.topQ, 2, 0, ""));
        StdOut.println(a.level(a.topQ, 3, 0, ""));
    }

}
