package com.tcurtil.kata.bankocr.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * model class that contains an account identifier and its status
 * 
 * The parsing and checks are done when constructing each instance.
 * 
 */
public class AccountIdentifier {

	/**
	 * 3 lines
	 */
	private String[] rawIdentifier;
	/**
	 * Parsed id from the rawIdentifier. May contains some ? chars
	 */
	private String parsedId;
	
	/**
	 * parsing status
	 */
	private ParsingStatus parsingStatus;
	
	/**
	 * In case the rawIdentifier is not firectly valid,
	 * possible identifiers will be stored here.
	 */
	private List<String> otherPossibleIdentifiers;
	
	public AccountIdentifier(String... line) {
		if (line.length != 3) {
			throw new RuntimeException("Expecting 3 lines to parse. Got " + line.length);
		}
		this.rawIdentifier = line;
		this.parse();
		this.parsingStatus = check(this.parsedId);
		if (parsingStatus != ParsingStatus.VALID) {
			this.otherPossibleIdentifiers = 
				tryToFix("", this.parsedId, 0)
					.stream()
					.filter(id -> check(id) == ParsingStatus.VALID)
					.collect(Collectors.toList());
			if (this.otherPossibleIdentifiers.size() == 1) {
				this.parsedId = this.otherPossibleIdentifiers.get(0);
				this.parsingStatus = ParsingStatus.VALID;
			} else if (this.otherPossibleIdentifiers.size() > 1) {
				this.parsingStatus = ParsingStatus.AMBIGUOUS;
			}
		}
	}

	private void parse() {
		for(String line : rawIdentifier) {
			if (line.length() != 27) {
				throw new RuntimeException("line : '" + line + "' do not contain 27 chars.");
			}
		}
		
		char[] chars = new char[9];
		for(int i = 0; i < 9; i++) {
			int start = i*3;
			int end = (i+1)*3;
			// the key is composed from the 9 characters : 3 from each lines, joined.
			String key = Arrays.stream(rawIdentifier)
					.map(line -> line.substring(start, end))
					.collect(Collectors.joining());
			chars[i] = NumberEncoding.parseNumber(key).orElse('?');
		}
		
		this.parsedId = new String(chars);
	}
	
	private ParsingStatus check(String identifier) {
		// checking valid number
		if (identifier.contains("?")) {
			return ParsingStatus.INVALID_CHARACTER;
		}
		
		// now checking checksum
		int sum = 0;
		int length = identifier.length();
		for(int i = 0; i < length; i++) {
			int val = Integer.valueOf(identifier.substring(length - i - 1,  length - i));
			sum += val * (i+1);
		}
		
		if (sum % 11 == 0) {
			return ParsingStatus.VALID;
		} else {
			return ParsingStatus.INVALID_CHECKSUM;
		}
	}
	
	private List<String> tryToFix(String prefix, String suffix, int currentCharIdx) {
		if (suffix.length() == 0) {
			return Collections.singletonList(prefix);
		}
		
		String key = Arrays.stream(this.rawIdentifier).map(s -> s.substring(3 * currentCharIdx, 3 * (currentCharIdx+1))).collect(Collectors.joining());
		List<Character> possibleFixes = NumberEncoding.getFixesForWronglyFormedChar(key);
		Optional<Character> actualNumber = NumberEncoding.parseNumber(key);
		
		List<String> results = new ArrayList<>();
		if (actualNumber.isPresent()) {
			// current character is valid. Continue exploration with this one.
			results.addAll(tryToFix(prefix + suffix.substring(0,  1), suffix.substring(1), currentCharIdx + 1));
		}
		
		for(Character c : possibleFixes) {
			results.add(prefix + c + suffix.substring(1));
		}
		return results;
	}
	
	public String getParsedId() {
		return parsedId;
	}
	
	public ParsingStatus getParsingStatus() {
		return parsingStatus;
	}
	
	public String[] getRawIdentifier() {
		return rawIdentifier;
	}
	
	public List<String> getOtherPossibleIdentifiers() {
		return otherPossibleIdentifiers;
	}
}
