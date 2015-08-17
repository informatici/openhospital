/**
 * 
 */
package org.isf.utils.db;

import java.text.Normalizer;

/**
 * @author Mwithi
 *
 */
public class NormalizeString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("\u00E0\u00E8\u00EC\u00F2\u00F9" + " -> " + normalizeString("\u00E0\u00E8\u00EC\u00F2\u00F9"));
		System.out.println(normalizeCompareTo("\u00E0\u00E8\u00EC\u00F2\u00F9", "aeiou"));
		System.out.println(normalizeCompareToIgnorecase("\u00E0\u00E8\u00EC\u00F2\u00F9", "AEIOU"));
		System.out.println(normalizeContains("\u00E0\u00E8\u00EC\u00F2\u00F9", "aeiou"));

	}
	
	/**
	 * Use java.text.Normalizer to reduce LOCALE strings for comparing 
	 * @param string - the string to normalize
	 * @return the string normalized
	 */
	public static String normalizeString(String string) {
		
		String normalizedString = Normalizer.normalize(string, Normalizer.Form.NFD);
		String newString = normalizedString.replaceAll("[^\\p{ASCII}]", "");
		return newString;
	}
	
	/**
	 * String.compareToIgnorecase() method over two normalized strings
	 * @param first - the string to normalize
	 * @param second - the string to normalize and compare
	 * @return 
	 */
	public static int normalizeCompareTo(String first, String second) {
		
		String newFirst = normalizeString(first);
		String newSecond = normalizeString(second);
		
		return newFirst.compareTo(newSecond);
	}
	
	/**
	 * String.compareTo() method over two normalized strings
	 * @param first - the string to normalize
	 * @param second - the string to normalize and compare
	 * @return
	 */
	public static int normalizeCompareToIgnorecase(String first, String second) {
		
		String newFirst = normalizeString(first);
		String newSecond = normalizeString(second);
		
		return newFirst.compareToIgnoreCase(newSecond);
	}
	
	/**
	 * String.contains() method over two normalized strings
	 * @param string - the string to normalize
	 * @param token - the string to normalize and search for
	 * @return
	 */
	public static boolean normalizeContains(String string, String token) {
		
		String containingString = normalizeString(string);
		String tokenString = normalizeString(token);
		
		boolean result = containingString.contains(tokenString);
		
		return result;
	}

}
