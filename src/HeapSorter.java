
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
	    heapsort.output.println("New HeapSorter");
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
     * @throws HeapException
     */
    public void sort() throws HeapException
    {
		// Get the initial time
		long startTime = System.currentTimeMillis();
		heapsort.output.println("Start Sort: " + startTime);
		heapsort.output.println("File Length: " + collection.getLength());
		//heapsort.output.println("Now, sort the data...");
		//sorting stuff here
		/*long count;
		for (count = 0; count < collection.getLength(); count++);
		{
		    heapsort.output.println(count);
		}*/

		MaxHeap<HeapRecord> H = new MaxHeap<HeapRecord>(collection, collection.getLength(), collection.getLength());
        for (int i=0; i < collection.getLength(); i++)  // Sort
        H.removeMax(); // Removemax places max at end of heap

		// Get the end time
		long endTime = System.currentTimeMillis();
		// Calculate the total time
		time = endTime - startTime;
		heapsort.output.println("Final time in milliseconds: " + time);
    }
}
