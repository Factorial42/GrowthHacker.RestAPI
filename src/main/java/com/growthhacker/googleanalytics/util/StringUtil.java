package com.growthhacker.googleanalytics.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class StringUtil {

	/**
	 * Checks if is blank.
	 *
	 * @param string
	 *            the string
	 * @return true, if is blank
	 */
	public static boolean isBlank(String string) {
		return checkBlank(string);
	}

	/**
	 * Checks if is not blank.
	 *
	 * @param string
	 *            the string
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(String string) {
		return !checkBlank(string);
	}

	/**
	 * Normalize.
	 *
	 * @param string
	 *            the string
	 * @return the string
	 */
	public static String normalize(String string) {
		return string.replaceAll(" ", "_").toLowerCase();
	}

	public static String replaceSpacesWithUnderscores(String string) {
		return string.replaceAll(" ", "_");
	}

	/**
	 * Check blank.
	 *
	 * @param string
	 *            the string
	 * @return true, if successful
	 */
	private static boolean checkBlank(String string) {
		return (string == null || string.isEmpty());
	}

	public static String capitalizeFirstCharacter(String input) {
		String first = input.substring(0, 1);
		String end = input.substring(1);
		String firstUpper = first.toUpperCase();
		return firstUpper + end;
	}

	public static String camelCaseToUnderscore(String input) {
		StringBuilder output = new StringBuilder();
		for (char c : input.toCharArray()) {
			if ((int) c >= 65 && (int) c < 91) {
				output.append("_");
			}
			output.append(c);
		}
		return output.toString().toLowerCase();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		StringUtil su = new StringUtil();
		System.out.println("test" + " is "
				+ ((su.isBlank("test")) ? "blank" : "not blank"));
		System.out.println("" + " is "
				+ ((su.isBlank("")) ? "blank" : "not blank"));
		System.out.println(null + " is "
				+ ((su.isBlank(null)) ? "blank" : "not blank"));

		System.out.println("test" + " is "
				+ ((su.isNotBlank("test")) ? "not blank" : "blank"));
		System.out.println("" + " is "
				+ ((su.isNotBlank("")) ? "not blank" : "blank"));
		System.out.println(null + " is "
				+ ((su.isNotBlank(null)) ? "not blank" : "blank"));
		System.out.println("testStringIsCool converted to underscores is "
				+ su.camelCaseToUnderscore("testStringIsCool"));

		DateFormat readFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");

		System.out.println(writeFormat.format(readFormat.parse(Instant.now()
				.toString())));
		
		System.out.println(("RAW All Web Site Data").toLowerCase().contains(("All Web Site data").toLowerCase()));

	}
}
