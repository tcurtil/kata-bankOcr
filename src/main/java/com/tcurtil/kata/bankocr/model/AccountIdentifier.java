package com.tcurtil.kata.bankocr.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * model class that contains an account identifier and its status
 * 
 * The parsing and checks are done when constructing each instance.
 * 
 */
public class AccountIdentifier {

	private String[] rawIdentifier; // 3 lines
	private String parsedId;
	private ParsingStatus parsingStatus;
	
	public AccountIdentifier(String... line) {
		if (line.length != 3) {
			throw new RuntimeException("Expecting 3 lines to parse. Got " + line.length);
		}
		this.rawIdentifier = line;
		parse();
		check();
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
	
	private void check() {
		// checking valid number
		if (this.parsedId.contains("?")) {
			this.parsingStatus = ParsingStatus.INVALID_CHARACTER;
			return;
		}
		
		// now checking checksum
		int sum = 0;
		int length = this.parsedId.length();
		for(int i = 0; i < length; i++) {
			int val = Integer.valueOf(this.parsedId.substring(length - i - 1,  length - i));
			sum += val * (i+1);
		}
		
		if (sum % 11 == 0) {
			this.parsingStatus = ParsingStatus.VALID;
		} else {
			this.parsingStatus = ParsingStatus.INVALID_CHECKSUM;
		}
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
}
