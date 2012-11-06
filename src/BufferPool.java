
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * {@code BufferPool} objects utilize a {@link LinkedList} to manage a set
 * number of {@link buffer} objects for the purposes of reading and writing data
 * from and to a given source file without having to make disk accesses. The
 * {@code BufferPool} allows portions of a source file to be kept in main memory
 * for faster I/O operations.
 * <p/>
 * A standard {@link LinkedList} is used to manage in-memory {@link Buffer}
 * objects. The <b>Least Recently Used</b> scheme is used to manage the pool
 * list. Source file I/O requests go through the {@code BufferPool}, which
 * supports direct reading and writing of the source using a
 * {@link RandomAccessFile} through the implementation of {@link Buffer}
 * objects.
 * <p/>
 * @author orionf22
 * @author rinaldi1
 */
public class BufferPool
{

	/**
	 * This {@code BufferPool's} {@link LinkedList}.
	 */
	private LinkedList<Buffer> pool;
	/**
	 * The current number of managed {@link Buffer} objects.
	 */
	private int size;
	/**
	 * This {@code BufferPool's} source.
	 */
	private RandomAccessFile file;
	/**
	 * The maximum number of {@code Buffer} objects this {@code BufferPool} can
	 * manage at one time.
	 */
	private int POOL_COUNT;
	/**
	 * A running count of the number of cache hits.
	 */
	private int CACHE_HITS;
	/**
	 * A running count of the number of cache misses.
	 */
	private int CACHE_MISSES;
	/**
	 * A running count of the number of disk reads.
	 */
	private int DISK_READS;
	/**
	 * A running count of the number of disk writes.
	 */
	private int DISK_WRITES;
	/**
	 * The static size of blocks within the source, in bytes. For Project 3,
	 * this is 4096.
	 */
	private static final int BLOCK_SIZE = 4096;

	/**
	 * Constructs a new {@code BufferPool} with space for {@code numBuffers}
	 * using {@code file}. By default, {@code file} is used to create a new
	 * {@link RandomAccessFile} set to {@code rw} mode.
	 * <p/>
	 * @param numBuffers the number of {@link Buffer Buffers} this pool will
	 *                      manage
	 * @param file       the {@link File} from which to read and write
	 * <p/>
	 * @throws FileNotFoundException
	 */
	public BufferPool(int numBuffers, File file) throws FileNotFoundException
	{
		pool = new LinkedList<>();
		POOL_COUNT = numBuffers;
		this.file = new RandomAccessFile(file, "rw");
		CACHE_HITS = 0;
		CACHE_MISSES = 0;
		DISK_READS = 0;
		DISK_WRITES = 0;
	}

	/**
	 * Retrieves a byte array from the pool's source file, starting at position
	 * {@code start} and ending at position {@code end}.
	 * <p/>
	 * @param start the location within the source from which to start reading
	 *                 bytes
	 * @param end   the location within the source from which to stop reading
	 *                 bytes
	 * <p/>
	 * @return a byte array containing the bytes from the source from
	 *            {@code start} to {@code end}
	 * <p/>
	 * @throws IOException
	 */
	public byte[] get(int start, int end) throws IOException
	{
		byte[] ret = new byte[end - start];
		int retIndex = 0;
		for (int i = start; i < end; i++)
		{
			//determine which Buffer to look at
			int blockNum = i / BLOCK_SIZE;
			Buffer buff = retrieve(blockNum, start, end);
			ret[retIndex] = buff.get(i - (blockNum * BLOCK_SIZE));
			retIndex++;
		}
		return ret;
	}

	/**
	 * Sets {@code bytes} to a {@link Buffer}. This occurs when a change has
	 * been made elsewhere and needs to be stored in the source. When bytes are
	 * assigned to a {@link Buffer}, it is marked as {@code dirty} and will
	 * result in the source's bytes changing to match {@code bytes}.
	 * <p/>
	 * {@code start} and {@code end} denote the location within the source at
	 * which bytes will be overwritten by the information contained within
	 * {@code bytes}. The two parameters are also used to retrieve the proper
	 * {@link Buffer} from the pool.
	 * <p/>
	 * @param bytes the bytes to assign to a {@link Buffer}
	 * @param start the starting index in the source at which to overwrite
	 * @param end   the ending index in the source at which to overwrite
	 * <p/>
	 * @throws IOException
	 */
	public void set(byte[] bytes, int start, int end) throws IOException
	{
		for (int i = start; i < end; i++)
		{
			int blockNum = i / BLOCK_SIZE;
			Buffer buff = retrieve(blockNum, start, end);
			buff.setBytes(bytes);
			buff.makeDirty();
		}
	}

	/**
	 * Retrieves the number of cache hits this {@code BufferPool} generated.
	 * <p/>
	 * @return the cache hit count
	 */
	public int getCacheHits()
	{
		return this.CACHE_HITS;
	}

	/**
	 * Retrieves the number of cache misses this {@code BufferPool} generated.
	 * <p/>
	 * @return the cache miss count
	 */
	public int getCacheMisses()
	{
		return this.CACHE_MISSES;
	}

	/**
	 * Retrieves the number of disk reads this {@code BufferPool} made.
	 * <p/>
	 * @return the disk read count
	 */
	public int getDiskReads()
	{
		return this.DISK_READS;
	}

	/**
	 * Retrieves the number of disk writes this {@code BufferPool} made.
	 * <p/>
	 * @return the disk write count
	 */
	public int getDiskWrites()
	{
		return this.DISK_WRITES;
	}

	/**
	 * Get the right {@link Buffer} from the pool given {@code blockNum}. If the
	 * desired {@link Buffer} is not already in the pool, it must be fetched. If
	 * the pool is already holding the maximum number of {@link Buffer Buffers},
	 * as defined by {@link BufferPool#POOL_COUNT POOL_COUNT}, then the
	 * {@link Buffer} at the end of the list is removed and the new
	 * {@link Buffer} added to the front. If the removed {@link Buffer} is
	 * marked as {@code dirty}, bytes in the source are modified.
	 * <p/>
	 * If the desired {@link Buffer} is already in the pool, a {@code cache hit}
	 * occurs. {@link BufferPool#CACHE_HITS CACHE_HITS} is incremented.
	 * <p/>
	 * @param blockNum if the desired {@link Buffer} is already in the pool, the
	 *                    {@link Buffer} with this number will be returned
	 * @param start    the starting index in the source at which to read data
	 * @param end      the ending index in the source at which to read data
	 * <p/>
	 * @return the desired {@link Buffer}
	 * <p/>
	 * @throws IOException
	 * @see BufferPool#setBytesInFile(byte[], int)
	 */
	private Buffer retrieve(int blockNum, int start, int end) throws IOException
	{
		//first search the pool for the right Buffer
		for (Buffer buff : pool)
		{
			if (buff.getNumber() == blockNum)
			{
				LRU(buff);
				CACHE_HITS++;
				return buff;
			}
		}
		//not in the pool, so add it
		Buffer buff = addBuffer(blockNum, start, end);
		return buff;
	}

	/**
	 * Adds a {@link Buffer} not already managed to the pool using the Least
	 * Recently Used scheme. If the pool is not full, then the desired
	 * {@link Buffer} is simply added to the front of the list. Otherwise the
	 * last {@link Buffer} is removed from the list and the new one added to the
	 * front. If the removed {@link Buffer} is marked as {@code dirty}, then
	 * bytes in the source are modified.
	 * <p/>
	 * As this method is only invoked when a desired {@link Buffer} is not in
	 * the pool, a {@code cache miss} occurs.
	 * {@link BufferPool#CACHE_MISSES CACHE_MISSES} is incremented.
	 * <p/>
	 * @param blockNum the number of the desired {@link Buffer}
	 * @param start    the starting index (within the source) of the new
	 *                    {@link Buffer Buffer's} bytes
	 * @param end      the ending index (within the source) of the new
	 *                    {@link Buffer Buffer's} bytes
	 * <p/>
	 * @return the new {@link Buffer} added to the pool
	 * <p/>
	 * @throws IOException
	 * @see BufferPool#setBytesInFile(byte[], int)
	 */
	private Buffer addBuffer(int blockNum, int start, int end) throws IOException
	{
		//create the new Buffer
		Buffer buff = new Buffer(blockNum, getBytesFromFile(start, end));
		//if the pool is full, remove the last Buffer, setting bytes if the
		//Buffer is dirty
		if (size == POOL_COUNT)
		{
			Buffer removed = pool.removeLast();
			if (removed.isDirty())
			{
				setBytesInFile(removed.bytes(), start);
				removed.clean();
			}
			//decrement size, knowing it will be incremented next anyway. This
			//is done so the size is always incremented properly; if the pool is
			//not full then size still needs to be incremented when a new Buffer
			//is added
			size--;
		}
		size++;
		pool.add(buff);
		CACHE_MISSES++;
		return buff;
	}

	/**
	 * Moves {@code buff} to the front of the pool using the Least Recently Used
	 * scheme. As this method is only called when a {@link Buffer} is needed and
	 * that {@link Buffer} is already in the pool, this method simply moves that
	 * {@link Buffer} to the front of the list.
	 * <p/>
	 * @param buff
	 */
	private void LRU(Buffer buff)
	{
		pool.remove(buff);
		pool.add(buff);
	}

	/**
	 * Retrieves a byte array from the source starting at {@code start} and
	 * ending at {@code end}. Data is read from disk in this method as bytes are
	 * accessed directly from the source file (stored on disk).
	 * {@link BufferPool#DISK_READS DISK_READS} is incremented.
	 * <p/>
	 * @param start the starting index at which to acquire bytes from the source
	 * @param end   the ending index at which to acquire bytes from the source
	 * <p/>
	 * @return the acquired bytes from the source
	 * <p/>
	 * @throws IOException
	 */
	private byte[] getBytesFromFile(int start, int end) throws IOException
	{
		//navigate to the right position in the source
		file.seek(start);
		//allocate byte array
		byte[] ret = new byte[end - start];
		int retIndex = 0;
		for (int i = start; i < end; i++)
		{
			ret[retIndex] = file.readByte();
			retIndex++;
		}
		DISK_READS++;
		return ret;
	}

	/**
	 * Modifies the contents of the source starting at {@code start}. The
	 * contents of {@code bytes} are used to overwrite existing information.
	 * Data is written to the source file (stored on disk).
	 * {@link BufferPool#DISK_WRITES} is incremented.
	 * <p/>
	 * @param bytes the bytes to write
	 * @param start the starting index at which to write < p/>
	 * <p/>
	 * @throws IOException
	 */
	private void setBytesInFile(byte[] bytes, int start) throws IOException
	{
		//navigate to proper position
		file.seek(start);
		file.write(bytes);
		DISK_WRITES++;
	}
}
