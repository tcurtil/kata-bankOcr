package com.tcurtil.kata.bankocr.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileRawIdentifierDao implements RawIdentifierDao {

	@Value("${dirpath}")
	private String dirPath;
	private File dir;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * This method check that the configuration is valid so that we can fail early in 
	 * case of badly configured base folder
	 */
	@PostConstruct
	public void init() {
		if (dirPath == null ||  "".equals(dirPath)) {
			logger.error("You need to add to command line : --dirpath={path}");
			System.exit(1); // force application to stop now so that the error message is the last one on console.
		}
		
		dir = new File(dirPath);
		if (!dir.exists()) {
			logger.error("Directory {} not found", dirPath);
			System.exit(1); // force application to stop now so that the error message is the last one on console.
		}
		
		if (!dir.isDirectory()) {
			logger.error("File {} is not a folder", dirPath);
			System.exit(1); // force application to stop now so that the error message is the last one on console.
		}
	}
	
	public List<String> readFully(String filename) {
		File file = new File(dir, filename);
		if (!file.toPath().toAbsolutePath().normalize().startsWith(dir.toPath().normalize().toAbsolutePath())) {
			throw new RuntimeException("Forbidden"); // user trying to escape the configured folder. 
		}
		try {
			return IOUtils.readLines(new FileReader(file));
		} catch (IOException e) {
			throw new RuntimeException("Error while reading file " + filename, e);
		}
	}

	@Override
	public List<String> listFilenames() {
		return Arrays.stream(dir.listFiles())
				.filter(f -> f.isFile())
				.map(f -> f.getName())
				.collect(Collectors.toList());
	}
	
}
