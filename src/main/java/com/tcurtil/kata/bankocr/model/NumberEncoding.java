package com.tcurtil.kata.bankocr.model;

import java.util.HashMap;
import java.util.Map;

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
	
	public static Character parseNumber(String key) {
		return numberEncoding.get(key);
	}
	
}
