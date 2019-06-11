package com.tcurtil.kata.bankocr.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tcurtil.kata.bankocr.service.OCRService;

@Controller
public class HomeController {
	
	@Resource
	private OCRService ocrService;
	
	@RequestMapping("/")
	public String home(ModelMap model, String filename) {
		model.put("availableFiles", ocrService.listFilenames());
		if (filename != null) {
			model.put("accountIdentifiers", ocrService.parse(filename));
		}
		return "parse_result";
	}
	
}
