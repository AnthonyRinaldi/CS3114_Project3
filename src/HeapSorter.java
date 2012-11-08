
// -------------------------------------------------------------------------
/**
 * This program is an implementation of Heapsort.
 * <p/>
 * @author Anthony Rinaldi
 * @author Ryan Merkel
 * @version November 5, 2012
 */
public class HeapSorter
{

	/**
	 * The time it took for the last sort in milliseconds.
	 */
	private long time = -1;
	/**
	 * This {@code HeapSorter's} {@link RecordCollection}, which interfaces
	 * between this class and a storage medium, such as a specialized array or
	 * buffer pool.
	 */
	private RecordCollection<HeapRecord> collection;

	// ----------------------------------------------------------
	/**
	 * Constructor for the HeapSorter
	 * <p/>
	 * @param collection
	 */
	public HeapSorter(RecordCollection<HeapRecord> collection)
	{
		this.collection = collection;
	}

	// ----------------------------------------------------------
	/**
	 * Returns the last sort's calculation time in milliseconds. Negative
	 * implies no sort time.
	 * <p/>
	 * @return The sort time
	 */
	public long getSortTime()
	{
		return time;
	}

	// ----------------------------------------------------------
	/**
	 * Sorts the heap.
	 * <p/>
	 * @throws HeapException
	 */
	public void sort() throws HeapException
	{
		// Get the initial time
		long startTime = System.currentTimeMillis();

		MaxHeap<HeapRecord> H = new MaxHeap<>(collection, collection.getLength(), collection.getLength());
		for (int i = 0; i < collection.getLength(); i++)
		{
			//removeMax places max at end of heap
			H.removeMax();
		}

		// Get the end time
		long endTime = System.currentTimeMillis();
		// Calculate the total time
		time = endTime - startTime;
		heapsort.output.println("Final time in ms: " + time);
	}
}
