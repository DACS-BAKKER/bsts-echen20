/*  Node Class
    Name: Ethan Chen
    Date Started: November 7, 2019
*/

// Class contains a value and a pointer to two other nodes

public class Node<Value> { // generic node class with pointers to left and right (for tree structure)

    public int key; // keys for BinarySearchTree class
    public String stringKey; // keys for AnimalBT class
    public Value data;
    public Node right;
    public Node left;

    public Node(int key, Value data) {
        this.data = data;
        this.key = key;
    }

    public Node(String key, Value data) {
        this.data = data;
        this.stringKey = key;
    }

    @Override
    public String toString() {
        return String.valueOf(key);
    }
}
