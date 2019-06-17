package com.tcurtil.kata.bankocr.dao;

import java.util.List;

/**
 * Interface that gives access to the text from the ingenuous machine.
 * 
 */
public interface RawIdentifierDao {

	/**
	 * Returns the whole text file split in lines.
	 * <p>
	 * This works because the files are small (max 500 account identifiers).
	 * We would have used streams or pagination in case of bigger files.
	 * 
	 * @param filename
	 * @return file content split in lines.
	 */
	List<String> readFully(String filename);

	/**
	 * Filenames the system have access to. 
	 * 
	 * @return a list of File names that will be accepted by the parser.
	 */
	List<String> listFilenames();

}
