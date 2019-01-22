package org.sagebionetworks.schema.util;

/**
 *
 */
public class CompareUtils {
	
	/**
	 * The absolute error used for comparing two doubles (epsilon - absolute error).
	 */
	public static final double ABSOLUTE_ERROR = 0.000000000000001d;
	
	/**
	 * Since GWT does not support Double.doubleToLongBits() for comparing doubles, we needed an alternative.
	 * This method simply compares two doubles with epsilon - absolute error
	 * 
	 *  @see <a href="Comparing floating point numbers">http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm</a>
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean doubleEquals(double a, double b){
		// epsilon - absolute error
		return Math.abs(a-b) < ABSOLUTE_ERROR;
	}
}