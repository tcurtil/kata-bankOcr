package com.tcurtil.kata.bankocr.model;

public enum ParsingStatus {
	VALID,
	INVALID_CHECKSUM,
	INVALID_CHARACTER,
	AMBIGUOUS
}