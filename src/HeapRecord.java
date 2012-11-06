
/**
 * {@code HeapRecord} objects wrap two integers: one is the key, used for
 * sorting, the other is the value, used for determining information.
 * <p/>
 * @author orionf22
 * @author rinaldi1
 */
public class HeapRecord
{

	/**
	 * The key of this {@code HeapRecord}.
	 */
	private int key;
	/**
	 * The value of this {@code HeapRecord}.
	 */
	private int value;

	/**
	 * Constructs a new {@code HeapRecord} with key {@code k} and a value of
	 * {@code v}.
	 * <p/>
	 * @param k the key to use
	 * @param v the value to use
	 */
	public HeapRecord(int k, int v)
	{
		this.key = k;
		this.value = v;
	}

	/**
	 * Returns the key of this {@code HeapRecord}.
	 * 
	 * @return the key
	 */
	public int getKey()
	{
		return this.key;
	}

	/**
	 * Returns the value of this {@code HeapRecord}.
	 * 
	 * @return the value
	 */
	public int getValue()
	{
		return this.value;
	}
}
