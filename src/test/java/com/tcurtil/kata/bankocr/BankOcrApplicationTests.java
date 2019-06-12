package com.tcurtil.kata.bankocr;

import java.util.List;

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
		
		Assert.assertEquals("Two accounts was expected", 2, parse.size());
		
		Assert.assertEquals("Account 123456789", "123456789", parse.get(0).getParsedId());
		Assert.assertEquals("Account 123456789 is valid", ParsingStatus.VALID, parse.get(0).getParsingStatus());
		
		Assert.assertEquals("Account 223456789", "223456789", parse.get(1).getParsedId());
		Assert.assertEquals("Account 223456789 has invalid checksum", ParsingStatus.INVALID_CHECKSUM, parse.get(1).getParsingStatus());
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
