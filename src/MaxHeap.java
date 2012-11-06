// -------------------------------------------------------------------------
/**
 *  This is a MaxHeap; a heap with the largest value at the top.
 *
 *  NOTE: All needed is a method to heapify an array. Simple!
 *
 *  @author Anthony Rinaldi
 *  @author Ryan Merkel
 *  @version November 5, 2012
 * @param <E> Generic
 */
public class MaxHeap<E extends Comparable<? super E>>

{
    int len;

    // ----------------------------------------------------------
    /**
     * Create a new MaxHeap.
     */
    public MaxHeap()
    {
        // Public Constructor
        len = 0;
    }

    // ----------------------------------------------------------
    /**
     * Swaps the maximum value in the heap with the end value.
     */
    public void removemax()
    {
        // TODO Auto-generated method stub

    }

    // ----------------------------------------------------------
    /**
     * Return the length of the MaxHeap
     * @return length of the MaxHeap
     */
    public int length()
    {
        return len;
    }

}
