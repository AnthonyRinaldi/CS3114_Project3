
// -------------------------------------------------------------------------
/**
 *  This is a MaxHeap; a heap with the largest value at the top.
 *
 *  @author Anthony Rinaldi
 *  @author Ryan Merkel
 *  @version November 5, 2012
 * @param <E> Generic
 */
public class MaxHeap<E extends Comparable<? super E>>
{
    /**
     * Max-Size of the Heap
     */
    private int maxn;
    /**
     * Length of the MaxHeap
     */
    int len;
    /**
     * The HeapArray
     */
    E array[];

    // ----------------------------------------------------------
    /**
     * Create a new MaxHeap.
     * @param inputarray Unsorted input values
     * @param number
     * @param max
     */
    public MaxHeap(E[] inputarray, int number, int max)
    {
        // Public Constructor
        len = number;
        maxn = max;
        // Set array to be inputarray, then heapify.
        array = inputarray;
        heapify();
    }

    /**
     * This helper method heapifies the array.
     */
    private void heapify()
    {
        for (int i = len / 2 - 1; i >= 0; i--)
            siftdown(i);
    }


    /** Put element in its correct place */
    private void siftdown(int posin)
    {
        int pos = posin;
        assert (pos >= 0) && (pos < len) : "Illegal heap position";
        while (!isLeaf(pos))
        {
            int j = leftchild(pos);
            if ((j<(len - 1)) && (array[j].compareTo(array[j+1]) < 0))
                j++; // j is now index of child with greater value
            if (array[pos].compareTo(array[j]) >= 0)
                return;
            swap(array, pos, j);
            pos = j; // Move down
        }
    }

    private void swap(E[] array2, int pos, int j)
    {
        E temp = array2[pos];
        array2[pos]= array2[j];
        array2[j] = temp;
    }

    /** @param pos
     * @return Position for left child of pos */
    public int leftchild(int pos)
    {
        assert pos < len/2 : "Position has no left child";
        return 2*pos + 1;
    }
    /** @param pos
     * @return Position for right child of pos */
    public int rightchild(int pos)
    {
        assert pos < (len-1)/2 : "Position has no right child";
        return 2*pos + 2;
    }
    /** @param pos
     * @return Position for parent */
    public int parent(int pos)
    {
        assert pos > 0 : "Position has no parent";
        return (pos-1)/2;
    }

    private boolean isLeaf(int pos)
    {
        // TODO Auto-generated method stub
        return false;
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
