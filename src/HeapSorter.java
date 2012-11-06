
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
		/*
		 MaxHeap H = new MaxHeap(input, input.length(), input.length());
		 for (int i=0; i < input.length(); i++)  // Sort
		 H.removemax(); // Removemax places max at end of heap
		 */
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
     */
    public void sort()
    {
        // TODO Auto-generated method stub


		// Get the initial time
		long startTime = System.currentTimeMillis();
		heapsort.output.println("Start Sort: " + startTime);

		//heapsort.output.println("Now, sort the data...");
		//sorting stuff here
		collection.
		// Get the end time
		long endTime = System.currentTimeMillis();
		// Calculate the total time
		time = endTime - startTime;
		heapsort.output.println("Final time in milliseconds: " + time);
    }
}
