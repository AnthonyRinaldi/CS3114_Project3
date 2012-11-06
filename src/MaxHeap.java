
/**
 * {@code MaxHeap} objects are heaps following the max-heap principle: the
 * largest value is at the top. They are initially given a certain maximum size;
 * this is the maximum number of individual records the heap is allowed to
 * manage. The heap's length represents the number of records currently managed
 * by the heap and is never allowed to exceed the size.
 * <p/>
 * NOTE: All needed is a method to heapify an array. Simple!
 * <p/>
 * @author rinadli1
 * @author orionf22
 * @param <E> Generic
 */
public class MaxHeap<E extends Comparable<? super E>>
{

	/**
	 * Number of records currently in the heap.
	 */
	private int length;
	/**
	 * Maximum allowable size of the heap, in terms of number of records.
	 */
	private int size;
	/**
	 * Serves as the interface between this class and whatever class is managing
	 * the memory storage of the records.
	 */
	private RecordCollection<E> heap;
	//private BufferPool pool;

	/**
	 * Creates a new {@code MaxHeap} with a specified storage manager
	 * representing the heap ({@code c}). {@code num} denotes the number of
	 * records initially in the heap while {@code max} denotes the maximum
	 * number of records manageable by the heap.
	 * <p/>
	 * An initial call to {@link MaxHeap#buildHeap() buildHeap()} attempts to
	 * make the first heapification of the initial records. If this fails, a
	 * {@link HeapException} is thrown.
	 * <p/>
	 * @param c   the {@link RecordCollection} that manages record storage
	 * @param num the initial number of records in the heap
	 * @param max the maximum amount of records this heap can manage
	 * <p/>
	 * @throws HeapException
	 */
	public MaxHeap(RecordCollection<E> c, int num, int max) throws HeapException
	{
		this.heap = c;
		this.length = num;
		this.size = max;
		buildHeap();
	}
	/*
	public MaxHeap(BufferPool p, int num, int max) throws HeapException
    {
        this.pool = p;
        this.length = num;
        this.size = max;
        buildHeap();
    }*/

	/**
	 * Attempts to build the heap according to max-heap standards.
	 */
	private void buildHeap() throws HeapException
	{
		for (int i = length / 2 - 1; i >= 0; i--)
		{
			siftDown(i);
		}
	}

	/**
	 * Determines if a given position is a leaf.
	 * @param pos the position to analyze
	 * @return {@code true} if the position is a leaf (it has no children),
	 *            {@code false} otherwise
	 */
	private boolean isLeaf(int pos)
	{
		return (pos >= length / 2) && (pos < length);
	}

	/**
	 * Returns the left child of {@code pos}, if it exists.
	 * <p/>
	 * @param pos the position of the parent of the desired left child
	 * <p/>
	 * @return a valid Integer if {@code pos} has a left child, otherwise
	 *            nothing
	 */
	private int leftChild(int pos)
	{
		assert pos < length / 2 : "Position has no left child";
		return 2 * pos + 1;
	}

	/**
	 * Returns the right child of {@code pos}, if it exists.
	 * <p/>
	 * @param pos the position of the parent of the desired right child
	 * <p/>
	 * @return a valid Integer if {@code pos} has a right child, otherwise
	 *            nothing
	 */
	private int rightChild(int pos)
	{
		assert pos < (length - 1) / 2 : "Position has no right child";
		return 2 * pos + 2;
	}

	/**
	 * Returns the parent position of {@code pos}, if it is not the root (at
	 * position {@code 0}).
	 * <p/>
	 * @param pos the position of a child of the desired parent
	 * <p/>
	 * @return a valid Integer if {@code pos} is not the root, otherwise nothing
	 */
	private int parent(int pos)
	{
		assert pos > 0 : "Position has no parent";
		return (pos - 1) / 2;
	}

	/**
	 * Attempts to swap the maximum value in the heap with the end value,
	 * returning that maximum value. If the heap is empty a
	 * {@link HeapException} is thrown.
	 * <p/>
	 * @return the largest value in the heap
	 */
	public E removeMax() throws HeapException
	{
		if (length < 0)
		{
			throw new IllegalHeapStateException("Attempting to operate on an empty heap");
		}
		//swaps the largest value (always at the root) with the last value
		heap.swap(0, --length);
		if (length != 0)
		{
			siftDown(0);
		}
		return heap.get(length);

	}

	/**
	 * Return the length, or current number of stored records, of the heap.
	 * <p/>
	 * @return the length of the heap
	 */
	public int length()
	{
		return length;
	}

	/**
	 * Attempts to sift the element in position {@code pos} down the heap as per
	 * max-heap standards. If {@code pos} is out of bounds, that is, less than
	 * {@code 0} or greater than {@link MaxHeap#size size} then an
	 * {@link IllegalHeapPositionException} is thrown.
	 * <p/>
	 * @param pos the position to sift down
	 * <p/>
	 * @throws IllegalHeapPositionException
	 */
	private void siftDown(int posn) throws IllegalHeapPositionException
	{
	    heapsort.output.println("siftDown");
	    int pos = posn; // You should not be touching the value of input values.
		if ((pos > length) || (pos < 0))
		{
			throw new IllegalHeapPositionException("Illegal Heap position: " + pos);
		}
		while (!isLeaf(pos))
		{
			int j = leftChild(pos);
			if ((j < (length - 1)) && (heap.get(j).compareTo(heap.get(j + 1)) < 0))
			{
				j++; // index of child w/ greater value
			}
			if (heap.get(pos).compareTo(heap.get(j)) >= 0)
			{
				return;
			}
			heap.swap(pos, j);
			pos = j;  // Move down

		}
	}

	/**
	 * Attempts to insert {@code val} into the heap. If the heap is already
	 * full, that is, if the number of items in the heap
	 * ({@link MaxHeap#length length}) is equal to the maximum size the heap can
	 * store ({@link MaxHeap#size size}) then the insert will be rejected.
	 * Otherwise insertion occurs as per max-heap standards.
	 * <p/>
	 * @param val the record to insert
	 * <p/>
	 * @throws IllegalHeapStateException
	 */
	public void insert(E val) throws IllegalHeapStateException
	{
		if (length >= size)
		{
			throw new IllegalHeapStateException("Attempting to insert into a full heap: " + val);
		}
		int curr = length++;
		heap.set(val, curr);
		// Siftup until curr parent's key > curr key
		while ((curr != 0) && (heap.get(curr).compareTo(heap.get(parent(curr))) > 0))
		{
			heap.swap(curr, parent(curr));
			curr = parent(curr);
		}
	}
}
