
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code IntegerCollection} class implements the {@link RecordCollection}
 * interface and handles all record I/O requests from any class making use of
 * the interface. This allows said classes to be free from any ties to the type
 * and implementation of the record data structure, in this case a
 * {@link BufferPool}.
 * <p/>
 * Any class making use of the interface will use the
 * {@link RecordCollection#get(int) get(int)} and
 * {@link RecordCollection#set(Object, int) set(Object, int)} methods to get and
 * set records; this class will then query its {@link BufferPool} to satisfy all
 * requests.
 * <p/>
 * {@link HeapRecord} objects are managed by this class.
 * <p/>
 * @author orionf22
 * @author rinaldi1
 */
public class IntegerCollection
		implements RecordCollection<HeapRecord>
{

	/**
	 * The {@link BufferPool} utilized by this {@code IntegerCollection}.
	 */
	private BufferPool pool;
	/**
	 * The static size of managed records, in bytes. For Project 3, this is 4.
	 */
	private static final int RECORD_SIZE = 4;

	/**
	 * Constructs a new {@code IntegerCollection} given a
	 * {@link BufferPool} {@code p}. The size of the pool is not determined
	 * here; it is determined higher up the hierarchy.
	 * <p/>
	 * @param p the {@link BufferPool} to use
	 */
	public IntegerCollection(BufferPool p)
	{
		this.pool = p;
	}

	@Override
	public HeapRecord get(int index)
	{
		//calculate the starting and ending access indeces
		int start = index * RECORD_SIZE;
		int end = start + RECORD_SIZE;
		//if an error occurs whil trying to read bytes, this method will return 
		//a zero-sized array; otherwise it will return the desired bytes
		byte[] got = new byte[0];
		try
		{
			got = pool.get(start, end);
		}
		catch (IOException ex)
		{
			Logger.getLogger(IntegerCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
		//decode the bytes first
		return decode(got);
	}

	@Override
	public void set(HeapRecord element, int recordNum)
	{
		//calculate the starting and ending access indeces
		int start = recordNum * RECORD_SIZE;
		int end = start + RECORD_SIZE;
		//encode element
		byte[] setMe = encode(element);
		try
		{
			pool.set(setMe, start, end);
		}
		catch (IOException ex)
		{
			Logger.getLogger(IntegerCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Decodes {@code bytes} into a new {@link HeapRecord}.
	 * <p/>
	 * @param bytes the byte array to decode
	 * <p/>
	 * @return the decoded {@link HeapRecord}
	 */
	private HeapRecord decode(byte[] bytes)
	{
		//if there was an error retrieving bytes, then the byte array will be 
		//zero-sized and thus does not contain a valid HeapRecord
		if (bytes.length < 1)
		{
			return new HeapRecord(-1, -1);
		}
		//bit shifting; yay!
		int key = bytes[1] + (bytes[0] << 8);
		int value = bytes[4] + (bytes[3] << 8);
		return new HeapRecord(key, value);
	}

	/**
	 * Encodes {@code record} into a byte array for future storage.
	 * <p/>
	 * @param record the {@link HeapRecord} to encode
	 * <p/>
	 * @return the encoded record
	 */
	private byte[] encode(HeapRecord record)
	{
		byte[] ret = new byte[4];
		if (record != null)
		{
			int key = record.getKey();
			int val = record.getValue();
			//the first half is simply the key shifted 8 places
			ret[0] = (byte) (key >> 8);
			//the next half is masked
			ret[1] = (byte) (key & 0xff);
			ret[2] = (byte) (val >> 8);
			ret[3] = (byte) (val & 0xff);
		}
		return ret;
	}
}
