//TODO this part

package SKerns_P3;

/**
 * TODO Class description
 *
 * @author Stewart Kerns
 * @version 1.0
 */
public class GenericStack<T> {
    /**
     * This class creates a node for a linked list stack
     *
     * @author Stewart Kerns
     * @version 1.0
     */
    private class Node{
        //declare value to hold a double
        private T value;
        //declare a Node next
        private Node next;

        /**
         * This constructor sets the parameters of value and next to the
         * entered parameters when a new object is created
         * @param value a double input for the value of the node
         * @param next the next node
         */

        Node (T value, Node next){
            //set the value
            this.value = value;
            //set the next node
            this.next = next;
        }
    }

    //create a Node for top and set it to null
    private Node top = null;

    /**
     * This method checks if the linked list is empty or not and returns a
     * boolean value for if it is or not
     * @return boolean value for if it is empty or not
     */
    public boolean empty(){
        //return true if top is null
        return top == null;
    }

    /**
     * This method pushes a double onto the stack by creating a new Node
     * @param num the double's value to be pushed onto the stack
     */
    public void push (T num){
        //set the top equal to the new Node with value of num and next pointing
        //to the old top
        top = new Node(num, top);
    }

    /**
     * This method checks that the stack is not empty and if so, throws an
     * exception about the stack being empty. If not it pops the top value and
     * returns it
     * @return double value of the top node
     */
    public T pop(){
        //if the stack is empty, throw an error code
        if (empty()){
            throw new IllegalArgumentException(
                    "Stack is empty");
        }

        //if the stack has a value, pop the top and return the value
        else {
            T value = top.value;
            top = top.next;
            return value;
        }
    }

    /**
     * This method checks that the stack isn't empty and if it is, throws an
     * error message.  If it's not, it returns the top value without popping it
     * @return double value of the top node
     */
    public T peek() {
        //if the stack is empty, throw an error code
        if (empty()){
            throw new IllegalArgumentException(
                    "Stack is empty");
        }

        //return the top value of the stack
        else {
            return top.value;
        }
    }
    public String toString() {
        //create a StringBuilder object to build a string to be returned
        StringBuilder builtString = new StringBuilder();

        //loop through each Node and add to the string
        for (GenericStack.Node p = top; p != null; p = p.next){
            builtString.append(p.value + " ");
        }

        return builtString.toString();
    }
}
