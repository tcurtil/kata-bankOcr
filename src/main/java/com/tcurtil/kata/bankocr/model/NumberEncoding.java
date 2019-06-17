package com.tcurtil.kata.bankocr.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class to convert a number produce by the "ingenious machine" to
 * a ascii character.
 * 
 * Each number is identified by the 9 characters joined without line break
 */
public final class NumberEncoding {

	private static final Map<String, Character> numberEncoding = new LinkedHashMap<>();
	
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
	
	private static Map<String, List<Character>> wronglyFormedCharsFixes;
	
	static {
		wronglyFormedCharsFixes = new LinkedHashMap<>(50);
		
		for(Map.Entry<String, Character> entry : numberEncoding.entrySet()) {
			char[] keyAsCharArray = entry.getKey().toCharArray();
			for(int i = 0; i < keyAsCharArray.length; i++) {
				if (keyAsCharArray[i] == ' ') {
					// replacing current space by a _ and a |
					char backup = keyAsCharArray[i]; 
					keyAsCharArray[i] = '_';
					wronglyFormedCharsFixes.computeIfAbsent(new String(keyAsCharArray), k -> new ArrayList<>()).add(entry.getValue());
					keyAsCharArray[i] = '|';
					wronglyFormedCharsFixes.computeIfAbsent(new String(keyAsCharArray), k -> new ArrayList<>()).add(entry.getValue());
					keyAsCharArray[i] = backup;
				} else {
					// replacing current char by a space
					char backup = keyAsCharArray[i]; 
					keyAsCharArray[i] = ' ';
					wronglyFormedCharsFixes.computeIfAbsent(new String(keyAsCharArray), k -> new ArrayList<>()).add(entry.getValue());
					keyAsCharArray[i] = backup;
				}
			}
		}
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
	
	public static List<Character> getFixesForWronglyFormedChar(String key) {
		return Optional.ofNullable(wronglyFormedCharsFixes.get(key)).orElseGet(() -> new ArrayList<>());
	}
	
}
