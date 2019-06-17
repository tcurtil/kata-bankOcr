package com.tcurtil.kata.bankocr.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tcurtil.kata.bankocr.dao.RawIdentifierDao;
import com.tcurtil.kata.bankocr.model.AccountIdentifier;

@Service
public class DefaultOCRService implements OCRService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private RawIdentifierDao rawIdentifierDao;
	
	@Override
	public List<AccountIdentifier> parse(String filename) {
		logger.info("Parsing file {}", filename);
		List<AccountIdentifier> accountIdentifiers = new ArrayList<>();
		
		List<String> lines = rawIdentifierDao.readFully(filename);
		
		for(int i = 0; i < lines.size(); i+=4) {
			String line1 = lines.get(i);
			String line2 = i+1 < lines.size() ? lines.get(i+1) : null;
			String line3 = i+2 < lines.size() ? lines.get(i+2) : null;
			
			if (line1 == null || line2 == null || line3 == null) {
				throw new RuntimeException("file " + filename + " do not contain a number of lines that is a multiple of 4.");
			}
			
			accountIdentifiers.add(new AccountIdentifier(line1, line2, line3));
		}
			
		return accountIdentifiers;
	}
	
	@Override
	public List<String> listFilenames() {
		logger.info("Listing all available files.");
		return rawIdentifierDao.listFilenames();
	}

	@Override
	public List<String> getFileContent(String filename) {
		logger.info("Reading file {} content", filename);
		return rawIdentifierDao.readFully(filename);
	}
	
}
