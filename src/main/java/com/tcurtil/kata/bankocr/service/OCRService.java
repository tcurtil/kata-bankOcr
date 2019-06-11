package com.tcurtil.kata.bankocr.service;

import java.io.File;
import java.util.List;

public interface OCRService {

	List<String> parse(String filename);

	List<File> listFilenames();

}
