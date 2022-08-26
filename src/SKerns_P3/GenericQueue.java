/*
 * Stewart Kerns
 * CPSC 5002, Seattle University
 * This is free and unencumbered software released into the public domain.
 */

package SKerns_P3;


/**
 * This class creates the building blocks for a generic queue that can be used
 * with any data type. It includes the methods copy, empty, dequeue, enqueue,
 * toString, append, and peek.
 *
 * @author Stewart Kerns
 * @version 1.1
 */
public class GenericQueue<T> {


    /**
     * This class creates a doubly linked list Node for use in a linked list
     * queue
     *
     * @author Stewart Kerns
     * @version 1.0
     */
    private class Node {

        //declare a Node prev to point to the previous Node
        private Node prev;
        //declare a Node prev to point to the next Node
        private Node next;
        //declare a T value that will hold a value
        private T value;


        /**
         * This constructor creates sets the value for the node, creates the
         * pointer to the next Node, and creates the pointer to the previous
         * Node
         *
         * @param value the T value that the Node will hold
         * @param prev a pointer to the previous Node
         * @param next a pointer to the next Node
         */
        public Node(T value, Node prev, Node next) {

            //set the value to the T value
            this.value = value;
            //set the previous node to prev
            this.prev = prev;
            //set the next node to next
            this.next = next;
        }
    }

    //create a private Node front and set it to null
    private Node head = null;
    //create a private Node rear and set it to null
    private Node tail = null;


    /**
     * This method creates a copy of a RenderQueue and returns that copy
     *
     * @return RenderQueue copy of the original instance it's called on
     */
    public GenericQueue<T> copy() {

        //create a new GenericQueue object to hold the copy
        GenericQueue<T> copyGQ = new GenericQueue<>();
        //create a Node p that will be used to traverse through the queue
        Node p = this.head;

        //traverse through the queue
        while (p != null) {
            //add each value of the original queue to the copy
            copyGQ.enqueue(p.value);
            p = p.next;
        }

        //return the copy of the GenericQueue
        return copyGQ;
    }


    /**
     * This method checks if the queue is empty and returns a boolean of if it
     * is or not
     *
     * @return boolean value of if the queue is empty or not
     */
    public boolean empty() {

        //return true if the queue is empty
        return head == null;
    }


    /**
     * This method checks that the queue is not empty and if so, throws an
     * exception. If it's not empty, it removes the front value and returns it
     *
     * @return T value at the top of the queue
     * @throws IllegalArgumentException if queue is empty and accessed
     */
    public T dequeue() throws IllegalArgumentException{

        //if the queue is empty, throw an exception
        if (empty())
            throw new IllegalArgumentException(
                    "Queue is empty");

            //if the queue isn't empty, remove the front value and return it
        else {
            //create a T object to hold the value of the front node
            T valueT = head.value;
            //move the front pointer to the next node in the list
            head = head.next;

            //if front isn't null, set the previous node to null
            if (head != null) {
                head.prev = null;
            }

            //if front is null, the list is empty and set rear to null
            if (head == null) {
                tail = null;
            }

            //return the front value
            return valueT;
        }
    }


    /**
     * String representation of the queue contents.
     *
     * @return the string representation
     */
    public String toString() {

        //create a StringBuilder object to build a string to be returned
        StringBuilder builtString = new StringBuilder();

        //loop through each Node and add to the string
        for (Node p = head; p != null; p = p.next){
            builtString.append(p.value + " ");
        }

        //return the String that was built
        return builtString.toString();
    }


    /**
     * This method adds a T object to the end of the queue and sets
     * the front to the rear if the queue is currently empty
     *
     * @param value a T object to add to the end of the queue
     */
    public void enqueue(T value){

        //if the queue has Nodes already, add the new Node to the end
        if (tail != null) {
            //add the new Node to the end
            tail.next = new Node(value, tail, null);
            //update rear to correctly point to the correct Node
            tail = tail.next;
        }

        //if the queue is empty, add the Node and set the front to the rear
        else {
            tail = new Node(value, null, null);
            head = tail;
        }
    }


    /**
     * This method appends a GenericQueue that's entered as a parameter to the
     * end of the GenericQueue it's called on
     *
     * @param genericQueue2 the GenericQueue to be added to the end
     */
    public void append(GenericQueue<T> genericQueue2){

        //create a Node to traverse through the GenericQueue that was passed in
        Node p = genericQueue2.head;

        //traverse through the GenericQueue
        while(p != null){
            //add the value of Node p to the end of the current GenericQueue
            this.enqueue(p.value);
            //set p to the next node
            p = p.next;
        }
    }


    /**
     * This method peeks at the head value of the generic queue and checks if
     * it is empty before. If it's empty, it throws an exception
     *
     * @return return the value at the head
     * @throws IllegalArgumentException if the queue is empty and accessed
     */
    public T peek(){

        //if the queue is empty, throw an error code
        if (empty()){
            throw new IllegalArgumentException(
                    "Queue is empty");
        }

        //return the value at the head
        return head.value;
    }
}
