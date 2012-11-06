
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

	// ----------------------------------------------------------
	/**
	 * Place a description of your method here.
	 * <p/>
	 * @param input
	 */
	public HeapSorter(RecordCollection<HeapRecord> collection)
	{
		// Get the initial time
		long startTime = System.currentTimeMillis();
		/*
		 MaxHeap H = new MaxHeap(input, input.length(), input.length());
		 for (int i=0; i < input.length(); i++)  // Sort
		 H.removemax(); // Removemax places max at end of heap
		 */
		// Get the end time
		long endTime = System.currentTimeMillis();
		// Calculate the total time
		time = endTime - startTime;

	}

	// ----------------------------------------------------------
	/**
	 * Returns the last sort's calculation time in milliseconds. Negative
	 * implies no sort time.
	 * <p/>
	 * @return The sort time
	 */
	public long lastSortTime()
	{
		return time;
	}


    public void sort()
    {
        // TODO Auto-generated method stub

    }
}
