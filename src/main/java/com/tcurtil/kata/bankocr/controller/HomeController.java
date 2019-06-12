package com.tcurtil.kata.bankocr.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tcurtil.kata.bankocr.model.AccountIdentifier;
import com.tcurtil.kata.bankocr.service.OCRService;

@Controller
public class HomeController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OCRService ocrService;
	
	@RequestMapping("/")
	public String home(ModelMap model, String filename) {
		model.put("availableFiles", ocrService.listFilenames());
		if (filename != null) {
			try {
				List<AccountIdentifier> parse = ocrService.parse(filename);
				model.put("accountIdentifiers", parse);
			} catch(Exception e) {
				model.put("errorMsg", e.getMessage());
				logger.error("Error while parsing file " + filename, e);
			}
			model.put("filename", filename);
		}
		return "parse_result";
	}
	
}
