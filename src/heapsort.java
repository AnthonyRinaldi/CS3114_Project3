
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
 *
 * @author orionf22
 * @author rinaldi1
 */
public class heapsort
{

	private static File dataFile;
	private static File statsFile;
	private static int buffers;
	private static HeapSorter sorter;
	private static BufferPool pool;
	private static PrintWriter output;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException
	{
		output = new PrintWriter(System.out);
		if (!parseArgs(args))
		{
			output.println("Program initialization failed.");
		}
		else
		{
			pool = new BufferPool(buffers, dataFile);
			sorter = new HeapSorter(new IntegerCollection(pool));
			sorter.sort();
			writeStats();
		}
	}
	
	private static void writeStats() throws IOException
	{
		long time = sorter.getSortTime();
		long numBlocks = dataFile.length() / BufferPool.BLOCK_SIZE;
		FileWriter writer = new FileWriter(statsFile);
		
		try (BufferedWriter bWriter = new BufferedWriter(writer))
		{
			bWriter.write(dataFile.getName() + ", with " + numBlocks 
					+ " blocks and " + buffers + " buffers\n");
			bWriter.write("Cahce hits: " + pool.getCacheHits() 
					+ "  Cache misses: " + pool.getCacheMisses()
					+ "");
		}
	}

	private static boolean parseArgs(String[] args)
	{
		if (args == null || args.length < 1)
		{
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
