package com.tcurtil.kata.bankocr.service;

import java.io.File;
import java.util.List;

import com.tcurtil.kata.bankocr.model.AccountIdentifier;

public interface OCRService {

	/**
	 * Fully parse a file identified by its filename (it will be resolved against the configured base folder).
	 * 
	 * @param filename to parse
	 * @return the list of account id parsed from the file.
	 */
	List<AccountIdentifier> parse(String filename);

	/**
	 * List the available files for parsing
	 * @return a list of java.io.File
	 */
	List<File> listFilenames();

}
