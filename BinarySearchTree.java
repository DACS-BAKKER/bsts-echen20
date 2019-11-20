/*  Binary Search Tree Class
    Name: Ethan Chen
    Date Started: November 7, 2019
*/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class BinarySearchTree<Value> implements Iterable<Value>{

    // Instance Variables

    Node<Integer> mother; // Effectively the very root node of the tree

    // Constructor

    public BinarySearchTree(Node parent) {
        mother = parent;
    }

    public BinarySearchTree() {

    }

    // Methods...

    //GRAPHICS, DRAWS TREE IN STDDRAW
    public void drawTree() {
        if (isEmpty()) {
            return;
        }

        StdDraw.clear();
        int height = height(mother);
        double yInc = 1.0 / (height + 1);
        double xInc = 0.25;

        drawIt(0.9, 0.5, yInc, xInc, mother);
    }

    private void drawIt(double y, double x, double yInc, double xInc, Node currNode) {
        if (currNode == null) {
            StdDraw.text(x, y, "-");
        } else {
            StdDraw.text(x, y, currNode.toString());
            StdDraw.line(x, y - 0.05, x - xInc, y - yInc + 0.05);
            StdDraw.line(x, y - 0.05, x + xInc, y - yInc + 0.05);
            double x1 = x;
            double y1 = y;
            double xInc2 = xInc;
            drawIt(y - yInc, x - xInc, yInc, xInc / 2, currNode.left); // recursive call that halves distance between items
            drawIt(y1 - yInc, x1 + xInc2, yInc, xInc2 / 2, currNode.right); // goes left and right
        }
    }

    //CHECKS IF BST FOLLOWS RULES OF BST (key values increase from left to right)

    public boolean isBST() {
        return isBST(mother);
    }

    private boolean isBST(Node currNode) {
        if (currNode == null) { // if doesn't exist, technically valid
            return true;
        } else if (currNode.left != null && currNode.left.key > currNode.key) { // left > parent discrepancy
            return false;
        } else if (currNode.right != null && currNode.right.key < currNode.key) { // right < parent discrepancy
            return false;
        } else {
            return isBST(currNode.left) && isBST(currNode.right); // must never be any discrepancies
        }
    }

    //A METHOD THAT ADDS AN IMPOSSIBLE VALUE TO BST

    private void addWrong(Node currNode) {
        if (currNode.left == null) {
            currNode.left = new Node(currNode.key + 10, 0); // adds an impossible value at the very bottom left of the tree (used for testing isBST)
        } else {
            addWrong(currNode.left);
        }
    }

    //A METHOD THAT CHECKS IF KEY VALUE EXISTS IN BST

    public boolean contains(int key) {
        return contains(key, mother);
    }

    private boolean contains(int key, Node cNode) { // helper method for contains
        if (!isEmpty()) {
            Node currNode = cNode;
            if (currNode.key == key) { // if it finds it, it is true
                return true;
            } else {
                if (currNode.left != null && contains(key, currNode.left)) { // if it exists to the left
                    return true;
                }
                if (currNode.right != null && contains(key, currNode.right)) { // if it exists to the right
                    return true;

                }
            }
            return false; // doesn't exist anywhere
        }
        return false; // no items in bst, not possible
    }

    private boolean contains2(int key, Node cNode) { // second method for contains with clearer code
        if (!isEmpty()) {
            Node currNode = cNode;
            if (currNode == null) {
                return false;
            } else if (currNode.key == key) { // if it finds it, it is true
                return true;
            }
            return contains2(key, cNode.left) || contains2(key, cNode.right);
        }
        return false;
    }

    //A METHOD THAT DELETES A VALUE WITH A KEY

    public void delete(int key) {
        if (contains(key)) {
            delete(key, mother);
        }
    }

    private void delete(int key, Node currNode) { // helper method
        if (key == mother.key) {
            Node temp = mother.right;
            mother = mother.left;
            connect(mother, temp); // connects the left side of tree and right side, from mother node
            return;
        }

        if (currNode.left != null) {
            if (currNode.left.key == key) { // found key

                deleteNode(currNode, currNode.left, 1);

            } else {
                delete(key, currNode.left); // recursive call to traverse left
            }
        }
        if (currNode.right != null) {
            if (currNode.right.key == key) { // found key

                deleteNode(currNode, currNode.right, 0);

            } else {
                delete(key, currNode.right); // recursive call to traverse right
            }
        }
    }

    private void deleteNode(Node rootNode, Node currNode, int direction) { // rootNode is the parent of currNode, currNode is the node you want to delete
        if (currNode.left != null && currNode.right == null) { // currNode left open, rigiht null (in cases with one open one not, just set the rootnodes pointer to the open node from currNode)
            if (direction == 1) { // direction denotes which direction currNode came from from rootNode (1 = left, 0 = right)
                rootNode.left = currNode.left;
            } else {
                rootNode.right = currNode.left;
            }
        } else if (currNode.right != null && currNode.left == null) { // opposite of last scenario
            if (direction == 1) {
                rootNode.left = currNode.right;
            } else {
                rootNode.right = currNode.right;
            }
        } else if (currNode.right == null && currNode.left == null) { // both null, set rootNode pointer to null
            if (direction == 1) {
                rootNode.left = null;
            } else {
                rootNode.right = null;
            }
        } else { // set left
            if (direction == 1) {
                rootNode.left = currNode.left; // set rootNode pointer to currNode.left, connect both sides
                connect(rootNode.left, currNode.right);
            } else {
                rootNode.right = currNode.left; // set rootNode pointer to currNode.left, connect both sides
                connect(rootNode.right, currNode.right);
            }
        }
    }

    private void connect(Node parentNode, Node childNode) { // sets right pointer of max of parentNodes tree to the childNode (traverses left side to max, sets right pointer to top node on right side)
        if (parentNode.right == null) {
            parentNode.right = childNode;
        } else {
            connect(parentNode.right, childNode);
        }
    }

    //MR. BAKKER'S DELETE METHOD

    public void delete2(int key) {
        delete2(key, mother);
    }

    // Helper function for delete
    private void delete2(int key, Node currNode) { // helper method (effectively the same as the other delete)

        if (currNode.left != null) {
            if (currNode.left.key == key) {

                deleteNode2(currNode, currNode.left, 1);

            } else {
                delete2(key, currNode.left);
            }
        }
        if (currNode.right != null) {
            if (currNode.right.key == key) {

                deleteNode2(currNode, currNode.right, 0);

            } else {
                delete2(key, currNode.right);
            }
        }

    }

    private void deleteNode2(Node rootNode, Node currNode, int direction) {
        if (currNode.left != null && currNode.right == null) { // all the same so far
            if (direction == 1) {
                rootNode.left = currNode.left;
            } else {
                rootNode.right = currNode.left;
            }
        } else if (currNode.right != null && currNode.left == null) {
            if (direction == 1) {
                rootNode.left = currNode.right;
            } else {
                rootNode.right = currNode.right;
            }
        } else if (currNode.right == null && currNode.left == null) {
            if (direction == 1) {
                rootNode.left = null;
            } else {
                rootNode.right = null;
            }
        } else {
            if (direction == 1) { //
                Node currNode2 = currNode;
                Node minOmax = minomax(currNode2.right); // finds node that points to min of right side
                Node temp = minOmax.left.right; // saves the right node from that position
                minOmax.left.right = currNode.right; // sets the right and left pointers to that of the object you want to delete
                minOmax.left.left = currNode.left;
                rootNode.left = minOmax.left; // puts min value in old nodes location
                minOmax.left = temp; // moves the min values branches up into its position

            } else {
                Node currNode2 = currNode;
                Node minOmax = minomax(currNode2.right);
                Node temp = minOmax.left.right;
                minOmax.left.right = currNode.right;
                minOmax.left.left = currNode.left;
                rootNode.right = minOmax.left;
                minOmax.left = temp;
            }
        }
    }

    private Node minomax(Node currNode) { // finds the node before the minimum node (called minomax because implemented to find the minimum of the greater side of the node you want to delete
        if (!isEmpty()) {
            if (currNode.left.left == null) {
                return currNode;
            } else {
                return minomax(currNode.left);
            }
        }
        return null;
    }


    //A METHOD THAT RETURNS HEIGHT OF TREE

    public int height() {
        return height(mother);
    }

    private int height(Node currNode) {
        if (isEmpty()) {
            return 0;
        }

        if (currNode == null) {
            return 0;
        } else {
            int leftHeight = 1 + height(currNode.left);
            int rightHeight = 1 + height(currNode.right);

            if (leftHeight > rightHeight) { // finds branch path with greatest length
                return leftHeight;
            } else {
                return rightHeight;
            }

        }
    }

    //A METHOD THAT CHECKS IF BST IS EMPTY

    public boolean isEmpty() {
        if (mother == null) { // if no mother, it doesn't exist
            return true;
        }
        return false;
    }

    //A METHOD THAT RETURNS MAX KEY IN BST

    public int max() {
        return max(mother);
    }

    private int max(Node cNode) { // helper method
        if (!isEmpty()) {
            Node currNode = cNode;
            if (currNode.right == null) {
                return currNode.key;
            } else {
                return max(currNode.right); // traverse as far right as possible
            }
        }
        return 0;
    }

    //A METHOD THAT RETURNS MIN KEY IN BST

    public int min() {
        return min(mother);
    }

    private int min(Node currNode) { // helper method
        if (!isEmpty()) {
            if (currNode.left == null) {
                return currNode.key;
            } else {
                return min(currNode.left); // traverse as far left as possible
            }
        }
        return 0;
    }

    //PRINTS LEVEL ORDER

    public void levelorder() {
        levelorder(mother);
    }

    private void levelorder(Node currNode) {
        int loc = 0;
        for (int x = 0; x < height(mother); x++) {
            printLevel(currNode, x, loc);
        }
    }

    private void printLevel(Node currNode, int level, int location) {
        if (!isEmpty()) {
            if (level == location) { // if on correct level, print out the value
                StdOut.print(currNode.key + ", ");
            } else {
                if (currNode.left != null) {
                    printLevel(currNode.left, level, location + 1); // go through whole tree, left to right
                }
                if (currNode.right != null) {
                    printLevel(currNode.right, level, location + 1);
                }
            }
        }
    }

    //INORDER PRINT (for all prints, v2 is actually my original, less efficient solution)

    public void inorder2(Node currNode) { // my initial, not as good method
        if (!isEmpty()) {
            if (currNode.left == null) {
                StdOut.print(currNode.key + ", ");
                if (currNode.right != null) {
                    inorder2(currNode.right);
                }
            } else {
                inorder2(currNode.left);
                StdOut.print(currNode.key + ", ");
                if (currNode.right != null) {
                    inorder2(currNode.right);
                }
            }
        }
    }

    public void inorder() {
        inorder(mother);
    }

    private void inorder(Node currNode) {
        if (currNode == null) {
            return;
        } else {
            inorder(currNode.left); // left
            StdOut.print(currNode.key + ", "); // parent
            inorder(currNode.right); // right
        }
    }

    //POSTORDER PRINT (for all prints, v2 is actually my original, less efficient solution)

    public void postorder2(Node currNode) {
        if (!isEmpty()) {
            if (currNode.left == null) {
                StdOut.print(currNode.key + ", ");
                if (currNode.right != null) {
                    postorder2(currNode.right);
                }
            } else {
                postorder2(currNode.left);
                if (currNode.right != null) {
                    postorder2(currNode.right);
                }
                StdOut.print(currNode.key + ", ");
            }
        }
    }

    public void postorder() {
        postorder(mother);
    }

    private void postorder(Node currNode) {
        if (currNode == null) {
            return;
        } else {
            postorder(currNode.left); // left
            postorder(currNode.right); // right
            StdOut.print(currNode.key + ", "); // parent
        }
    }

    //PREORDER PRINT (for all prints, v2 is actually my original, less efficient solution)

    public void preorder2(Node currNode) {
        if (!isEmpty()) {
            if (currNode.left == null) {
                StdOut.print(currNode.key + ", ");
                if (currNode.right != null) {
                    preorder2(currNode.right);
                }
            } else {
                StdOut.print(currNode.key + ", ");
                preorder2(currNode.left);
                if (currNode.right != null) {
                    preorder2(currNode.right);
                }
            }
        }
    }

    public void preorder() {
        preorder(mother);
    }

    private void preorder(Node currNode) { // helper method
        if (currNode == null) {
            return;
        } else {
            StdOut.print(currNode.key + ", "); // parent
            preorder(currNode.left); // left
            preorder(currNode.right); // right
        }
    }

    //INSERTS A NODE WITH KEY KEY AND VALUE VAL AT CORRECT LOCATION IN BST

    public void put(int key, Value val) {
        put(key, val, mother);
    }

    private void put(int key, Value val, Node currNode) { // helper method

        if (isEmpty()) {
            mother = new Node(key, val);
            return;
        }

        if (currNode.left == null && key < currNode.key) { // node on left is null, and key is less than last key, create node
            currNode.left = new Node(key, val);
        } else if (currNode.right == null && key > currNode.key) { // node on right is null, and key is greater than last key, create node
            currNode.right = new Node(key, val);
        } else if (currNode.left.key == key) { // next key is equal, change value
            Node newNode = new Node(key, val);
            newNode.left = currNode.left.left;
            newNode.right = currNode.left.right;
            currNode.left = newNode;
            return;
        } else if (currNode.right.key == key) {
            Node newNode = new Node(key, val);
            newNode.left = currNode.right.left;
            newNode.right = currNode.right.right;
            currNode.right = newNode;
            return;
        } else { // node is not null, create node and
            if (key > currNode.key) {
                put(key, val, currNode.right);// traverses right
            } else if (key < currNode.key) {
                put(key, val, currNode.left);// traverses left
            }
        }
    }

    //RETURNS NUMBER OF ITEMS IN BST

    public int size() {
        return size(mother);
    }

    private int size(Node currNode) {
        if (!isEmpty()) {
            if (currNode == null) { // base case 1, no node exists
                return 0;
            } else if (currNode.left == null && currNode.right == null) { // base case 2, node pointers empty on either side
                return 1;
            } else {
                return size(currNode.left) + size(currNode.right) + 1; // goes through every item and adds one every time
            }
        }
        return 0;
    }

    // Run Method
    public static void main(String[] args) { // Console Runner to Test Methods

        /* THIS IS ALL MY TESTING FOR METHODS
        Node n = new Node(25, 0);
        BinarySearchTree bst = new BinarySearchTree(n);

        bst.put(15, 0, bst.mother);
        bst.put(50, 0, bst.mother);
        bst.put(10, 0, bst.mother);
        bst.put(22, 0, bst.mother);
        bst.put(4, 0, bst.mother);
        bst.put(12, 0, bst.mother);
        bst.put(18, 0, bst.mother);
        bst.put(24, 0, bst.mother);
        bst.put(35, 0, bst.mother);
        bst.put(70, 0, bst.mother);
        bst.put(31, 0, bst.mother);
        bst.put(44, 0, bst.mother);
        bst.put(66, 0, bst.mother);
        bst.put(90, 0, bst.mother);

        bst.drawTree();

        StdOut.println("Level Order:");
        bst.levelorder(bst.mother);
        StdOut.println(" END");

        StdOut.println();
        StdOut.println("In Order:");
        bst.inorder(bst.mother);
        StdOut.print(" END");

        StdOut.println();
        StdOut.println();
        StdOut.println("Post Order:");
        bst.postorder(bst.mother);
        StdOut.print(" END");

        StdOut.println();
        StdOut.println();
        StdOut.println("Pre Order:");
        bst.preorder(bst.mother);
        StdOut.print(" END");

        StdOut.println();
        StdOut.println();
        StdOut.println("Contains 66?");
        StdOut.println(bst.contains(66, bst.mother));

        StdOut.println();
        StdOut.println("Size?");
        StdOut.println(bst.size(bst.mother));

        StdOut.println();
        StdOut.println("Max?");
        StdOut.println(bst.max(bst.mother));

        StdOut.println();
        StdOut.println("Min?");
        StdOut.println(bst.min(bst.mother));

        StdOut.println();
        StdOut.println("Height?");
        StdOut.println(bst.height(bst.mother));

        StdOut.println();
        StdOut.println("Is BST?");
        StdOut.println(bst.isBST(bst.mother));

        StdOut.println();
        StdOut.println("Deleting 15 and 50");
        bst.delete(15, bst.mother);
        bst.delete(50, bst.mother);
        StdOut.println(bst.height(bst.mother));
        StdOut.println(bst.contains(18, bst.mother));
        bst.inorder(bst.mother);
        StdOut.println();
        bst.postorder(bst.mother);
        StdOut.println();
        bst.preorder(bst.mother);
        StdOut.println();
        bst.levelorder(bst.mother);
        StdOut.println();
        //bst.addWrong(bst.mother);
        StdOut.println(bst.isBST(bst.mother));

        bst.drawTree();

        StdOut.println();
        StdOut.println("Deleting 25");
        bst.delete(25, bst.mother);
        bst.inorder(bst.mother);
         */

        //USER INTERFACE CODE
        BinarySearchTree bst = new BinarySearchTree();

        StdOut.println("Welcome to the Binary Search Tree Console Tester");
        StdOut.println();
        StdOut.println("1: add; 2: delete, 3: height, 4: max and min; 5: print tree; 6: graphics and exit");
        int userInput = StdIn.readInt();
        while (userInput != 0) {

            if (userInput == 1) {
                StdOut.println("What key? ");
                int key = StdIn.readInt();
                StdOut.println("What value? ");
                String value = StdIn.readString();
                bst.put(key, value);
                StdOut.println("You have added key " + key + " with value " + value);

            } else if (userInput == 2) {
                StdOut.println("What key would you like to delete?");
                int deleteKey = StdIn.readInt();

                StdOut.println("How would you like to delete the key? 1: Ethan's Method, 2: Bakker's Method");
                int deleteMethod = StdIn.readInt();

                if (bst.contains(deleteKey)) {
                    StdOut.println("You have deleted key " + deleteKey);

                    if (deleteMethod == 1) {
                        bst.delete(deleteKey);
                    } else {
                        bst.delete2(deleteKey);
                    }
                } else {
                    StdOut.println("This Binary Search Tree does not contain key " + deleteKey);
                }

            } else if (userInput == 3) {
                StdOut.println("The height of the tree is " + bst.height() + " levels");

            } else if (userInput == 4) {
                StdOut.println("The max key is: " + bst.max());
                StdOut.println("The min key is: " + bst.min());

            } else if (userInput == 5) {
                StdOut.println("How would you like to print the tree?");
                StdOut.println("1: Level Order, 2: Preorder, 3: Postorder, 4: Inorder ");
                int order = StdIn.readInt();
                if (order == 1) {
                    bst.levelorder();
                } else if (order == 2) {
                    bst.preorder();
                } else if (order == 3) {
                    bst.postorder();
                } else if (order == 4) {
                    bst.inorder();
                }
                StdOut.println();

            } else if (userInput == 6) {
                bst.drawTree();
            }

            StdOut.println();

            StdOut.println("1: add; 2: delete, 3: height, 4: max and min; 5: print tree; 6: graphics and exit");
            userInput = StdIn.readInt();
            StdOut.println();

        }
    }

    public Iterator<Value> iterator() {
        return new BSTIterator();
    } //UNSUCCESSFUL

    private class BSTIterator implements Iterator<Value> {

        Node<Value> temp1 = (Node<Value>) mother;
        Node<Value> temp2 = (Node<Value>) mother;

        Value data = null;
        int turn = 0;

        public boolean hasNext() {
            if(temp1.right != null || temp1.left != null || temp2.right != null || temp2.left != null) {
                return true;
            }
            return false;
        }

        public void remove() {

        }

        public Value next() {
            if(turn == 3) {
                data = (Value) temp1.left.data;
                turn ++;
            } else if(turn == 0) {
                data = temp1.data;
                temp1 = temp1.left;
                temp2 = temp1.right;
            } else if(turn == 1) {
                data = temp1.data;
                turn++;
            } else if(turn == 2) { // unfortunately, this method only covers the left side of the tree.
                data = temp2.data;
                temp1 = temp1.left;
                temp2 = temp2.left;
                turn++;
            }
            return data;
        }
    }

}
