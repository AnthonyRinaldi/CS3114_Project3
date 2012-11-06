
/**
 * This is a MaxHeap; a heap with the largest value at the top.
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
	private int len;
	/**
	 * Maximum allowable size of the heap, in terms of number of records.
	 */
	private int size;
	/**
	 * Serves as the interface between this class and whatever class is managing
	 * the memory storage of the records.
	 */
	private RecordCollection<E> heap;

	// ----------------------------------------------------------
	/**
	 * Create a new MaxHeap.
	 */
	public MaxHeap(RecordCollection<E> c, int num, int max) throws IllegalHeapPositionException
	{
		this.heap = c;
		this.len = num;
		this.size = max;
		buildHeap();
	}

	private void buildHeap() throws IllegalHeapPositionException
	{
		for (int i = len / 2 - 1; i >= 0; i--)
		{
			siftDown(i);
		}
	}

	private boolean isLeaf(int pos)
	{
		return (pos >= len / 2) && (pos < len);
	}

	private int leftChild(int pos)
	{
		assert pos < len / 2 : "Position has no left child";
		return 2 * pos + 1;
	}

	private int rightChild(int pos)
	{
		assert pos < (len - 1) / 2 : "Position has no right child";
		return 2 * pos + 2;
	}

	private int parent(int pos)
	{
		assert pos > 0 : "Position has no parent";
		return (pos - 1) / 2;
	}

	// ----------------------------------------------------------
	/**
	 * Swaps the maximum value in the heap with the end value.
	 */
	private E removemax() throws IllegalHeapStateException
	{
		if (len < 0)
		{
			throw new IllegalHeapStateException("Attempting to operate on an empty heap");
		}
		DSutil.swap(Heap, 0, --len);
		if (len != 0)
		{
			siftDown(0);
		}
		return heap.get(len);

	}

	// ----------------------------------------------------------
	/**
	 * Return the length of the MaxHeap
	 * <p/>
	 * @return length of the MaxHeap
	 */
	public int length()
	{
		return len;
	}

	private void siftDown(int pos) throws IllegalHeapPositionException
	{
		if ((pos > len) || (pos < 0))
		{
			throw new IllegalHeapPositionException("Illegal Heap position: " + pos);
		}
		while (!isLeaf(pos))
		{
			int j = leftChild(pos);
			if ((j < (len - 1)) && (heap.get(j).compareTo(heap.get(j + 1)) < 0))
			{
				j++; // index of child w/ greater value
			}
			if (heap.get(pos).compareTo(heap.get(j)) >= 0)
			{
				return;
			}
			DSutil.swap(Heap, pos, j);
			pos = j;  // Move down

		}
	}
}
