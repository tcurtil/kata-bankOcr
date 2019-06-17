package com.tcurtil.kata.bankocr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcurtil.kata.bankocr.model.AccountIdentifier;
import com.tcurtil.kata.bankocr.model.ParsingStatus;
import com.tcurtil.kata.bankocr.service.OCRService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankOcrApplicationTests {

	@Resource
	private OCRService ocrService;
	
	@Test
	public void testOneAccount() {
		List<AccountIdentifier> parse = ocrService.parse("account_list_1.txt");
		Assert.assertEquals("One account was expected", 1, parse.size());
		Assert.assertEquals("Account 123456789", "123456789", parse.get(0).getParsedId());
	}
	
	@Test
	public void testInvalidFile_MissingLines() {
		try {
			ocrService.parse("account_list_invalid_1.txt");
			Assert.fail("Parsing should have failed : invalid file (missing lines).");
		} catch(Exception e) {
			// expected
		}
	}
	
	@Test
	public void testInvalidFile_WrongSizedLines() {
		try {
			ocrService.parse("account_list_invalid_2.txt");
			Assert.fail("Parsing should have failed : invalid file (some lines have less than 27 chars).");
		} catch(Exception e) {
			// expected
		}
	}
	
	@Test
	public void testInvalidFile_FolderEscape() {
		try {
			ocrService.parse("../../etc/passwd");
			Assert.fail("Parsing should have failed : invalid file (outside folder root).");
		} catch(Exception e) {
			// expected
		}
	}
	
	@Test
	public void testInvalidChecksum() {
		List<AccountIdentifier> parse = ocrService.parse("account_list_2.txt");
		
		Assert.assertEquals("Two accounts were expected", 2, parse.size());
		
		Assert.assertEquals("Account 123456789", "123456789", parse.get(0).getParsedId());
		Assert.assertEquals("Account 123456789 is valid", ParsingStatus.VALID, parse.get(0).getParsingStatus());
		
		Assert.assertEquals("Account 888888888", "888888888", parse.get(1).getParsedId());
		Assert.assertNotEquals("Account 888888888 has invalid checksum", ParsingStatus.VALID, parse.get(1).getParsingStatus());
	}
	
	@Test
	public void testInvalidCharacters() {
		List<AccountIdentifier> parse = ocrService.parse("account_list_3.txt");
		
		Assert.assertEquals("1 accounts were expected", 1, parse.size());

		Assert.assertEquals("Account 86110??36", "86110??36", parse.get(0).getParsedId());
		Assert.assertEquals("Account 86110??36 has invalid characters", ParsingStatus.INVALID_CHARACTER, parse.get(0).getParsingStatus());
	}
	
	@Test
	public void testFixesOnInvalidIdentifier() {
		List<AccountIdentifier> parse = ocrService.parse("account_list_4.txt");
		
		/*
expected :
=> 711111111
=> 777777177
=> 200800000
=> 333393333 
=> 888888888 AMB ['888886888', '888888880', '888888988']
=> 555555555 AMB ['555655555', '559555555']
=> 666666666 AMB ['666566666', '686666666']
=> 999999999 AMB ['899999999', '993999999', '999959999']
=> 490067715 AMB ['490067115', '490067719', '490867715']
=> 123456789
=> 000000051
=> 490867715 
		 */
		 
		String[] expectedResults = new String[] {
				"711111111", "777777177", "200800000", "333393333", "888888888", "555555555",
				"666666666", "999999999", "490067715", "123456789", "000000051", "490867715"};
		ParsingStatus[] expectedParsingStatus = new ParsingStatus[] {
				ParsingStatus.VALID, ParsingStatus.VALID, ParsingStatus.VALID, ParsingStatus.VALID, ParsingStatus.AMBIGUOUS, ParsingStatus.AMBIGUOUS,
				ParsingStatus.AMBIGUOUS, ParsingStatus.AMBIGUOUS, ParsingStatus.AMBIGUOUS, ParsingStatus.VALID, ParsingStatus.VALID, ParsingStatus.VALID};
		List<Set<String>> expectedAmbiguities = new ArrayList<>(Arrays.asList(null, null, null, null, 
				new HashSet<>(Arrays.asList("888886888", "888888880", "888888988")),
				new HashSet<>(Arrays.asList("555655555", "559555555")),
				new HashSet<>(Arrays.asList("666566666", "686666666")),
				new HashSet<>(Arrays.asList("899999999", "993999999", "999959999")),
				new HashSet<>(Arrays.asList("490067115", "490067719", "490867715")),
				null, null, null));
		
		
		for(int i = 0; i < expectedResults.length; i++) {
			AccountIdentifier accountIdentifier = parse.get(i);
			Assert.assertEquals("Account " + expectedResults[i] + " should have status : " + expectedParsingStatus[i], expectedParsingStatus[i], accountIdentifier.getParsingStatus());
			Assert.assertEquals("Account " + expectedResults[i], expectedResults[i], accountIdentifier.getParsedId());
			if (accountIdentifier.getParsingStatus() == ParsingStatus.AMBIGUOUS) {
				for(String s : accountIdentifier.getOtherPossibleIdentifiers()) {
					Assert.assertTrue("found a possibility : " + s + " that should not.", expectedAmbiguities.get(i).contains(s));
				}
				for(String s : expectedAmbiguities.get(i)) {
					Assert.assertTrue("this possibility : " + s + " was not found.", accountIdentifier.getOtherPossibleIdentifiers().contains(s));
				}
			}
		}
	}
	
	
	
	
	@Test
	public void testBuildingInvalidAccountIdentifier() {
		try {
			new AccountIdentifier(
					"    _  _     _  _  _  _  _ ", 
					"  | _| _||_||_ |_   ||_||_|",
					"  ||_  _|  | _||_|  ||_| _|",
					"    _  _     _  _  _  _  _ ");
			Assert.fail("Building an account identifier with more than 3 strings should fail");
		} catch(Exception e) {
			// expected branch
		}
		
		try {
			new AccountIdentifier(
					"    _  _     _  _  _  _  _ ", 
					"  | _| _||_||_ |_   ||_||_|");
			Assert.fail("Building an account identifier with less than 3 strings should fail");
		} catch(Exception e) {
			// expected branch
		}
	}

}
