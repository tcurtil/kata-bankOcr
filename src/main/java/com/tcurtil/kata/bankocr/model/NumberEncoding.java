package com.tcurtil.kata.bankocr.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class to convert a number produce by the "ingenious machine" to
 * a ascii character.
 * 
 * Each number is identified by the 9 characters joined without line break
 */
public final class NumberEncoding {

	private static final Map<String, Character> numberEncoding = new HashMap<>();
	
	static {
		numberEncoding.put(
				" _ " +
				"| |" +
				"|_|"
			, '0');
		
		numberEncoding.put(
				"   " +
				"  |" +
				"  |"
			, '1');
		
		numberEncoding.put(
				" _ " +
				" _|" +
				"|_ "
			, '2');
		
		numberEncoding.put(
				" _ " +
				" _|" +
				" _|"
			, '3');
		
		numberEncoding.put(
				"   " +
				"|_|" +
				"  |"
			, '4');
		
		numberEncoding.put(
				" _ " +
				"|_ " +
				" _|"
			, '5');
		
		numberEncoding.put(
				" _ " +
				"|_ " +
				"|_|"
			, '6');
		
		numberEncoding.put(
				" _ " +
				"  |" +
				"  |"
			, '7');
		
		numberEncoding.put(
				" _ " +
				"|_|" +
				"|_|"
			, '8');
		
		numberEncoding.put(
				" _ " +
				"|_|" +
				" _|"
			, '9');
	}
	
	/**
	 * Parse an 'ingenous manchine' number (3x3 chars (&lt;space&gt;, | or _), joined) into the corresponding Character.
	 * 
	 * If it can't be parsed, an empty Optional is returned.
	 * 
	 * @param key joined 3x3 chars
	 * @return number a char or an empty optional
	 */
	public static Optional<Character> parseNumber(String key) {
		return Optional.ofNullable(numberEncoding.get(key));
	}
	
}
