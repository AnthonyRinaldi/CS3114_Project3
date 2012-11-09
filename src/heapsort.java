
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
/**
 * This project implements the heapsort algorithm using the max-heap standard to
 * sort a collection of records by key. To keep memory usage low and program
 * efficiency high, a {@link BufferPool} is used to access data from disk and
 * keep it in memory without keeping the entire contents of the unsorted
 * collection in memory. This collection is specified by a binary file, the
 * first argument on the command line. All data is read from this file and the
 * sorted collection is written back to the file, overwriting anything already
 * in the file.
 * <p/>
 * Up to a certain point, the more {@link Buffer Buffers} managed by the
 * {@link BufferPool} the faster the program will execute (due to minimized disk
 * accesses). This number is the second argument on the command line.
 * <p/>
 * Finally, program execution statistics are collected and stored in a new file
 * specified by the third and final command line argument. This argument is
 * expected to be a valid abstract pathname which will be used to create a new
 * file.
 * <p/>
 * @author orionf22
 * @author rinaldi1
 */
public class heapsort
{

	/**
	 * The data file containing the initially unsorted collection of records.
	 */
	private static File dataFile;
	/**
	 * The statistics file; statistics are always appended to the file rather
	 * than overwriting the existing contents.
	 */
	private static File statsFile;
	/**
	 * The number of {@link Buffer Buffers} that the {@link BufferPool} is
	 * allowed to manage.
	 */
	private static int buffers;
	/**
	 * The main sorting "brains".
	 */
	private static HeapSorter sorter;
	/**
	 * The {@link IntegerCollection} serving as the interface between the pool
	 * and the sorter.
	 */
	private static IntegerCollection collection;
	/**
	 * The {@link BufferPool} mediating disk accesses.
	 */
	private static BufferPool pool;
	/**
	 * A publicly available {@link PrintWriter}. All program output comes
	 * through this writer.
	 */
	public static PrintWriter output;

	/**
	 * @param args the command line arguments
	 * <p/>
	 * @throws HeapException
	 */
	public static void main(String[] args) throws IOException, HeapException
	{
		output = new PrintWriter(System.out, true);
		output.println("Start Program");
		//parse the command line arguments. the program cannot operate if any 
		//arguments are invalid
		if (!parseArgs(args))
		{
			output.println("Program initialization failed.");
		}
		else
		{
			pool = new BufferPool(buffers, dataFile);
			output.println("Input File Size: " + dataFile.length());
			collection = new IntegerCollection(pool, dataFile.length());
			sorter = new HeapSorter(collection);
			//SORT!
			sorter.sort();
			//flush the pool prior to writing stats
			pool.flush();
			writeStats();
			reportBlockLeaders();
			pool.closeSourceStream();
		}
		output.println("End Program");
	}

	/**
	 * Writes program execution statistics to {@code statsFile}. Right
	 * justification is required with respect to cache hits and disk reads, and
	 * also with respect to cache misses and disk writes. These two sets of
	 * values are written "one on top of another" meaning one value appears on
	 * the next line. Example:
	 * <p/>
	 * Cache hits: 123,456 Cache misses: 12,345 Disk Reads: 34,567 Disk Writes:
	 * 1,234,567
	 * <p/>
	 * @throws IOException
	 */
	private static void writeStats() throws IOException
	{
		long time = sorter.getSortTime();
		long numBlocks = dataFile.length() / BufferPool.BLOCK_SIZE;
		try (FileWriter writer = new FileWriter(statsFile, true))
		{
			DecimalFormat formatter = new DecimalFormat("#,###");

			String cacheHits = formatter.format(pool.getCacheHits());
			String cacheMisses = formatter.format(pool.getCacheMisses());
			String diskReads = formatter.format(pool.getDiskReads());
			String diskWrites = formatter.format(pool.getDiskWrites());

			//determine how many digits are in each stat
			int cacheHitLength = cacheHits.length();
			int diskReadLength = diskReads.length();
			int cacheMissLength = cacheMisses.length();
			int diskWriteLength = diskWrites.length();

			//String-ify stats
			String cacheHitStats = "Cache hits:";
			String cacheMissStats = "Cache misses:";
			String diskReadStats = "Disk Reads:";
			//disk writes needs an extra space because it does not line evenly with 
			//cache misses (it is one character off):
			//     Cache misses:
			//     Disk writes:
			String diskWriteStats = "Disk Writes: ";

			//these if statements check to see which stat has more digits
			if (cacheHitLength > diskReadLength)
			{
				diskReadStats += " " + padInteger(diskReads, cacheHitLength) + "  ";
				cacheHitStats += " " + cacheHits + "  ";
			}
			else
			{
				diskReadStats += " " + diskReads + "  ";
				cacheHitStats += " " + padInteger(cacheHits, diskReadLength) + "  ";
			}

			if (cacheMissLength > diskWriteLength)
			{
				cacheMissStats += " " + cacheMisses + "  \n";
				diskWriteStats += " " + padInteger(diskWrites, cacheMissLength)
						+ "  \n";
			}
			else
			{
				cacheMissStats += " " + padInteger(cacheMisses, diskWriteLength)
						+ "  \n";
				diskWriteStats += " " + diskWrites + "  \n";
			}

			try (BufferedWriter bWriter = new BufferedWriter(writer))
			{
				bWriter.write(dataFile.getName() + ", with " + numBlocks
						+ " blocks and " + buffers + " buffers\n");
				bWriter.write(cacheHitStats + cacheMissStats + diskReadStats
						+ diskWriteStats + "Time: " + time + "\n\n");
			}
		}
	}

	/**
	 * Fetches the block leading {@link HeapRecords} from the pool via the
	 * {@link IntegerCollection}. Each record is right justified so all values
	 * in the key and value columns line up. Each record's values are separated
	 * from the next record by two spaces and a newline is inserted after every
	 * eigth record.
	 */
	private static void reportBlockLeaders()
	{
		HeapRecord[] leaders = collection.getBlockLeaders();
		//determines when eigth records have been printed to a line so the next
		//eigth records will be printed on a new line
		int eigth = 1;
		for (int i = 0; i < leaders.length; i++)
		{
			HeapRecord curr = leaders[i];
			output.printf("%d %d  ", curr.getKey(), curr.getValue());
			eigth++;
			if (eigth == 8)
			{
				output.println();
				eigth = 0;
			}
		}
	}

	/**
	 * Adds {@code padTo} blanks to pad {@code val}.
	 * <p/>
	 * @param val   the number to pad
	 * @param padTo the number of blanks to insert before {@code val}
	 * <p/>
	 * @return a String representation of the padded value
	 */
	private static String padInteger(String val, int padTo)
	{
		//determine how many digits are in val
		int numDecimals = val.length();
		String ret = "";
		//add padding blanks
		for (int i = numDecimals; i < padTo; i++)
		{
			ret += " ";
		}
		//add val at the end
		return ret += val;
	}

	private static boolean parseArgs(String[] args)
	{
		if (args == null || args.length < 1)
		{
			output.println("No arguments found. Call as heapsort "
					+ "<data-file-path> <num-buffers> <stats-file-path>");
			return false;
		}
		else
		{
			boolean goodToGo = true;
			try
			{
				dataFile = new File(args[0]);
			}
			catch (Exception e)
			{
				output.println("Error creating data-file. Verify first "
						+ "parameter is a valid String path to a binary file.");
				goodToGo = false;
			}
			try
			{
				buffers = Integer.parseInt(args[1]);
			}
			catch (Exception e)
			{
				output.println("Error determining number of buffers to allow. "
						+ "Verify second parameter is valid integer in range "
						+ "[1, 20].");
				goodToGo = false;
			}
			try
			{
				statsFile = new File(args[2]);
			}
			catch (Exception e)
			{
				output.println("Error creating stats-file. Verify third "
						+ "parameter is a valid String path.");
				goodToGo = false;
			}
			return goodToGo;
		}
	}
}
