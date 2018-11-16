import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of max fibonacci heap.
 * @author  Nimitbhai Patel
 *          UFID: (7092-9185)
 *          npatel1@ufl.edu
 */
public class MaxFibonacciHeap {

    private Node root;      // root of the fibonacci heap
    private Node max;       // pointer to the node with maximum frequency

    /**
     * Constructor for Max Fibonacci Heap object.
     * The default value of root node is set.
     * The root node will have the word value "root"
     * (for debugging purpose only) and the freequency value
     * set to the maximum value in Integer. Addtionally,
     * the root's child cut value is always FALSE.
     */
    public MaxFibonacciHeap(){
        root = new Node("root", Integer.MAX_VALUE);
        max = null;
    }

    /**
     * This method inserts the node as a child of the root.
     * @param word  -   keyword
     * @param frequency -   frequency of the keyword
     */
    public Node insert(String word, int frequency){
        // create a new node for insertion
        Node newNode = new Node(word, frequency);
        doInsert(this.root, newNode);
        return newNode;
    }

    /**
     * This method inserts the node as a child of the root.
     * @param node  -   node to be inserted into the heap
     */
    public void insert(Node node){
        if(node == null)
            return;
        doInsert(this.root, node);
    }

    /**
     * Inserts the newNode to the circular doubly linked list of the parent.
     * On insertion, parent's degree, child cut, and the circular doubly linked list
     * of children gets updated. If parent is root, then the max pointer may get
     * updated as well.
     * @param parent    -   node to whose child list the newNode is to be inserted
     * @param newNode   -   node to be inserted
     */
    private void doInsert(Node parent, Node newNode){
        /*
            If root has a child then we add the newly created node
            into the child list of the root.
         */
        if(parent.hasChild()){
            // add this newly created node after the child
            Node child = parent.getChild();
            Node left = child;
            Node right = child.getRight();

            // inserts new node between child and the the right sibling
            child.setRight(newNode);
            newNode.setLeft(child);
            newNode.setRight(right);
            right.setLeft(newNode);

        }else{
            /*
                This is the first child of the root.
                To maintain circular doubly linked list
                this creates a loop with one element.
            */
            parent.setChild(newNode);
            newNode.setLeft(newNode);
            newNode.setRight(newNode);
        }

        /*
            Since a node is added to the root's child list,
            we may need to update the max pointer.
         */
        if(isRoot(parent)){
            if(this.max == null){
                this.max = newNode;
            }else if(newNode.getFrequency() > this.max.getFrequency()){
                this.max = newNode;
            }
        }else {
            /*
                For non-root node, the child cut field is by default FALSE.
                But, it the child cut value is TRUE then it should be set
                to false as a new node was added to this parent.
             */
            parent.setChildCut(false);
        }

        // update degree
        parent.incrementDegree();

        // assign parent to this new node
        newNode.setParent(parent);
    }

    /**
     * Removes node from doubly linked list.
     * After removal of the node, parent's
     * child pointer is updated to point to another node
     * in the doubly linked list if any, the degree
     * of the parent node is decremented, parent's
     * child cut is set to TRUE, and node's parent pointer
     * is set to the root.
     *
     * This method is used only by increaseKey. It is not
     * intended for removal of node from thr heap directly.
     *
     * TO DO: make it private after testing.
     * @param node
     */

    /**
     * Removes the node from the heap. If the node to be removed
     * is the max node, then removeMax() method is used otherwise
     * doRemove() method is used.
     * @param node  -   node to be removed from the heap
     */
    private void remove(Node node){
        if(node == null)
            return;

        // if the node to be removed is max, then removeMax is used
        if(node.equals(this.max)){
            removeMax();
        }else {
            doRemove(node);
        }
    }

    /**
     * Removes the node (other than the max node) from the heap.
     * When a node is removed from the heap, several fields needs to be
     * updated for both the node and the parent of the node. If node is
     * removed from the root's children list then root's degree is
     * decrement. If the node is removed from a parent which is not
     * the root, then we decrement parent's degree as well as set it's
     * child cut field to TRUE. Additionally, if the node being removed
     * is a child pointer of the parent, then we update the child pointer
     * as well. If the node is the only element in the heap, we set the
     * max pointer to null.
     * @param node  -   node other than max node to be removed from the heap.
     */
    private void doRemove(Node node) {

        if(node == null)
            return;

        Node parent = node.getParent();
        /*
            It is possible that the node to be removed is the parent's child
            pointer so, if it is the case we update the child pointer.
         */
        if (node.equals(parent.getChild())) {
            // if parent has only one child (this node) then it's child pointer is set to null
            if (parent.hasOneChild()) {
                parent.setChild(null);

                // update max pointer too
                if(isRoot(parent))
                    this.max = null;

            } else {
                parent.setChild(node.getRight());   // set the child pointer to the right sibling
            }
        }

        // removes the node from the doubly linked list in which it resides
        removeFromList(node);

        // decreases parent degree by 1
        parent.decrementDegree();

        if (isRoot(parent)) {
            // update parent pointer
            node.setParent(null);
        } else {
            // update child cut of parent if it is not the root
            parent.setChildCut(true);

            // update parent pointer
            node.setParent(this.root);
        }
    }

    /**
     * Removes the node from the circular doubly linked list in which it resides
     * if the length of circular doubly linked list is greater than 1.
     * @param node  -   node to be removed from the circular doubly linked list.
     */
    public void removeFromList(Node node){
        if(node == null)
            return;

        Node left = node.getLeft();
        Node right = node.getRight();
        left.setRight(right);
        right.setLeft(left);
    }


    /**
     * Increases the frequency of the node by the amount provided.
     * When node's frequency count is increment it may no longer satisfy
     * the max heap property so, if a violation occurs for a node whose
     * parent is not the root then we fix it by performing a cascading cut.
     * Additionally, if needed, the max pointer is updated.
     *
     * @param node  -   node whose frequency is to be incremented
     * @param frequency -   amount by which the frequency is incremented
     */
    public void increaseKey(Node node, int frequency){
        if(node == null)
            return;

        Node parent = node.getParent();

        // update node's frequency
        node.incrementFrequencyBy(frequency);
        // Cascading cut is only needed if parent's frequency is smaller than this node's frequency.
        if(!isRoot(parent) && parent.getFrequency() < node.getFrequency()) {
            // mark these node to have child cut true and then perform cascading cut
            node.setChildCut(true);
            doCascadingCut(node);
        }

         /*
            Update max pointer if the frequency of this
            node is greater than max frequency. Additionally,
            updating root's child cut is not required.
         */
        if(node.getFrequency() > this.max.getFrequency()) {
            this.max = node;
        }
    }

    /**
     * Performs cascading cut from the given node.
     * Moves upward towards the root of the heap, visiting the child to parent
     * nodes as long as the node's cascading cut value is TRUE, removes the node
     * and insert it as a child of the root. Once a node is reached whose cascading
     * cut is FALSE, stop and set it's cascading cut value to TRUE. The cascading cut
     * value of root is always FALSE so, the loop terminates.
     *
     * @param node - node from where cascading cut needs to be done.
     */
    public void doCascadingCut(Node node){

        if(node == null)
            return;

        Node parent = null;
        boolean nodeHadChildCut = node.hadChildCut();

        //  Removes node along a path towards the root as long as the child cut of parent is TRUE.
        while(nodeHadChildCut){
            // save parent
            parent = node.getParent();
            // save parent's child cut value before the cut
            nodeHadChildCut = parent.hadChildCut();
            // remove this node
            remove(node);
            // insert node as a child of the root
            doInsert(this.root, node);
            // move to the parent
            node = parent;
        }

        // lastly, update the child cut field to true if the node is not the root node.
        if(!isRoot(node))
            node.setChildCut(true);
    }

    /**
     * Meld operation combines two doubly linked list into one.
     * There are four possible cases to consider
     *
     *  i)   Parent and otherNode has no child : meld is not needed.
     *  ii)  Parent has children but otherNode doesn't : meld is not needed.
     *  iii) Parent doesn't have children but otherNode does: meld is done.
     *       The child list of otherNode becomes child list of the parent
     *       and the parent pointer of otherNode's child is set to the root.
     *  iV)  Parent and otherNode has children: meld is done.
     *
     * Additionally, after meld is done, the parent degree is incremented
     * by the degree of the otherNode.
     *
     * @param parent    -   node which becomes parent after meld operation
     * @param otherNode -   node whose child list is to be melded
     */
    public void meld(Node parent, Node otherNode){

        // there is nothing to meld
        if(!parent.hasChild() && !otherNode.hasChild())
            return;

        // parent has child but otherNode has no child so, meld does not occur
        if(!otherNode.hasChild())
            return;

        // parent doesn't have any child but other node does so meld occurs
        if(!parent.hasChild()){
            parent.setChild(otherNode.getChild());
            otherNode.getChild().setParent(this.root);
        }else {
            // both has child so meld them
            Node firstListNode = parent.getChild();
            Node secondListNode = otherNode.getChild();
            Node firstListNodeRight = firstListNode.getRight();
            Node secondListNodeLeft = secondListNode.getLeft();


            // melding two doubly linked list
            firstListNode.setRight(secondListNode);
            secondListNode.setLeft(firstListNode);
            firstListNodeRight.setLeft(secondListNodeLeft);
            secondListNodeLeft.setRight(firstListNodeRight);
       }

        // update parent degree after melding
        parent.incrementDegreeBy(otherNode.getDegree());
    }

    /**
     * Removes the max node from the heap.
     * To remove the max node, we first remove the max node using
     * doRemove() method, then we meld the root's child list with the
     * max node's child list, and finally do pairwise combine.
     *
     * @return  -   the node with the max frequency if exist, null otherwise.
     */
    public Node removeMax(){
        if(this.max == null)
            return null;

        Node maxNode = this.max;

        // remove max node from the heap
        doRemove(maxNode);
        // meld the root's child list with the max node's child list
        meld(this.root, maxNode);
        // pairwise combine the root's child list
        doPairwiseCombine(this.root.getChild());
        // clear all node fields except word and frequency to make later insertion cleaner
        maxNode.clear();

        return maxNode;
    }

    /**
     * Performs pairwise combine for root's child. To perform
     * pairwise combined we use a hash table to keep track of a degree
     * and a node associated with that degree. Whenever, two nodes have
     * the same degree, they are combined in such a way that the node
     * with larger frequency become parent of the node with the smaller
     * frequency. This process is carried out until there is
     * no two node with the same degree.
     *
     * @param node  -   node whose parent is the root
     */
    public void doPairwiseCombine(Node node){

        if(node == null)
            return;

        // to keep track of node with the same frequency
        Map<Integer, Node> degreeToNode = new HashMap<>();
        int degreeCount = node.getParent().getDegree();
        Node next = null;
        Node otherNode = null;

        // pairwise combined occurs only when max is removed so, max pointer is updated
        this.max = node;

        while(degreeCount > 0){
            /*
                Set parent pointer for node before pairwise combine.
                This is needed as after meld operation the root's child
                may not have root as it's parent.
             */
            node.setParent(this.root);

            // update max pointer
            if(node.getFrequency() > this.max.getFrequency())
                this.max = node;

            // save next node in the list
            next = node.getRight();

            // stores the node that has same degree if any, null otherwise
            otherNode = degreeToNode.getOrDefault(node.getDegree(), null);

            // pairwise combine is done until there is no two node that has same degree
            while(otherNode != null){

                // remove this entry from the map
                degreeToNode.remove(node.getDegree());

                // if other node has larger frequency then it becomes parent
                if(otherNode.getFrequency() > node.getFrequency()){
                    doRemove(node);
                    doInsert(otherNode, node);
                    node = otherNode;       // store otherNode as node
                }else{
                    doRemove(otherNode);
                    doInsert(node, otherNode);
                }
                // see if there is otherNode with same degree in the map
                otherNode = degreeToNode.getOrDefault(node.getDegree(), null);
            }

            degreeToNode.put(node.getDegree(), node);
            node = next;
            --degreeCount;
        }

        // clear the map
        degreeToNode.clear();
    }


    public Node getRoot(){
        return this.root;
    }

    public Node getMax(){
        return max;
    }

    public boolean isRoot(Node node){
        return this.root.equals(node);
    }

}