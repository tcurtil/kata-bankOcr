package com.tcurtil.kata.bankocr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultOCRService implements OCRService {

	@Value("${dirpath:.}")
	private String dirPath;
	private File dir;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	
	public List<String> parse(String filename) {
		
		List<String> accountIdentifiers = new ArrayList<>();
		
		int currentLine = 1;
		
		File file = new File(dir, filename);
		if (!file.toPath().toAbsolutePath().normalize().startsWith(dir.toPath().normalize().toAbsolutePath())) {
			throw new RuntimeException("Forbidden"); // user trying to escape the configured folder. 
		}
		
		try (FileInputStream fis = new FileInputStream(file);
			 Reader r = new InputStreamReader(fis, StandardCharsets.UTF_8);
			 LineNumberReader reader = new LineNumberReader(r)) {
			
			String line;
			while((line = reader.readLine()) != null) {
				String line1 = line;
				
				currentLine += 1;
				String line2 = reader.readLine();
				
				currentLine += 1;
				String line3 = reader.readLine();
				
				currentLine += 1;
				reader.readLine(); // blank line
				
				if (line1 == null || line2 == null || line3 == null) {
					throw new RuntimeException("file " + dir.getAbsolutePath() + " do not contain a number of lines that is a multiple of 4.");
				}
				
				accountIdentifiers.add(parse(line1, line2, line3, currentLine));
			}
			
		} catch (IOException e) {
			logger.error("Could not parse file {}. Error happened when reading line#{}", e, dirPath, currentLine);
		}
		
		return accountIdentifiers;
	}

	private String parse(String line1, String line2, String line3, int fileLineNumber) {
		if (line1.length() != 27 || line2.length() != 27 || line3.length() != 27) {
			throw new RuntimeException("file " + dir.getName() + " is malformed : line " + fileLineNumber + 
					" or one of the two previous ones do not contain 27 chars...");
		}
		
		char[] chars = new char[9];
		for(int i = 0; i < 9; i++) {
			int start = i*3;
			int end = (i+1)*3;
			String key = line1.substring(start, end) + line2.substring(start, end) + line3.substring(start, end);
			chars[i] = numberEncoding.get(key);
		}
		
		return new String(chars);
	}

	@Override
	public List<File> listFilenames() {
		return Arrays.stream(dir.listFiles())
				.filter(f -> f.isFile())
				.collect(Collectors.toList());
	}
	
}
