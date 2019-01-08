/**
 * Node structure for max fibonacci heap
 * @author  Nimit Patel
 *          npatel1@ufl.edu
 */
public class Node {

    private int degree;         // number of child of this node
    private Node child;         // pointer to any one of its child
    private Node parent;        // pointer to its parent
    private Node left;          // pointer to its left sibling
    private Node right;         // pointer to its right sibling
    private boolean hadChildCut;// flag to indicate child cut
    private String word;        // word/key for the node
    private int frequency;      // frequency/value for the key

    public Node(String word, int frequency){
        // initialization
        this.word = word;
        this.frequency = frequency;

        this.degree = 0;
        this.parent = null;
        this.child = null;
        this.left = null;
        this.right = null;
        this.hadChildCut = false;
    }


    public boolean hasChild(){
        return this.child != null;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public void setChild(Node child){
        this.child = child;
    }

    public void setLeft(Node left){
        this.left = left;
    }

    public void setRight(Node right){
        this.right = right;
    }

    public Node getChild(){
        return this.child;
    }

    public Node getLeft(){
        return this.left;
    }

    public Node getRight(){
        return this.right;
    }

    public String getWord(){
        return this.word;
    }

    public int getFrequency(){
        return this.frequency;
    }

    public Node getParent(){ return this.parent; }

    public void decrementDegree() {
        --this.degree;
    }

    public int getDegree() {
        return this.degree;
    }

    public boolean hasOneChild(){
        return this.degree == 1;
    }

    public void setChildCut(boolean childCut) {
        this.hadChildCut = childCut;
    }

    public boolean hadChildCut(){
        return this.hadChildCut;
    }

    public void incrementDegree() {
        ++this.degree;
    }

    public void incrementFrequencyBy(int frequency) {
        this.frequency += frequency;
    }

    public void incrementDegreeBy(int degree) {
        this.degree += degree;
    }

    public void clear(){
        this.degree = 0;
        this.child = null;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.hadChildCut = false;
    }
}
