package com.smartdocs.gpt.document.service;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.smartdocs.gpt.document.model.Document;
import com.smartdocs.gpt.document.model.TextSegment;

public class DocumentSplitter {
	private final int segmentLength;
	private final int segmentOverlap;

	public DocumentSplitter(int segmentLength, int segmentOverlap) {
		this.segmentLength = segmentLength;
		this.segmentOverlap = segmentOverlap;

	}

	public List<TextSegment> paragraphSplit(Document document) {
		String text = document.text();
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		String[] paragraphs = text.split("\\s*\\R\\s*\\R\\s*");

		return stream(paragraphs).map(paragraph -> TextSegment.from(paragraph.trim(), document.metadata()))
				.collect(toList());
	}

	public List<TextSegment> lineSplit(Document document) {
		String text = document.text();
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		String[] paragraphs = text.split("\\s*\\R\\s*");

		return stream(paragraphs).map(paragraph -> TextSegment.from(paragraph.trim(), document.metadata()))
				.collect(toList());
	}

	public List<TextSegment> sentencesplit(Document document) {
		String text = document.text();
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		List<String> sentences = splitIntoSentences(text);

		return sentences.stream().map(sentence -> TextSegment.from((sentence).trim(), document.metadata()))
				.collect(toList());
	}

	public List<TextSegment> wordSplit(Document document) {
		String text = document.text();
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		String[] paragraphs = text.split("\\s+");

		return stream(paragraphs).map(paragraph -> TextSegment.from(paragraph.trim(), document.metadata()))
				.collect(toList());
	}

	private List<String> splitIntoSentences(String text) {
		List<String> sentences = new ArrayList<>();

		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.getDefault());
		iterator.setText(text);

		int start = iterator.first();
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
			sentences.add(text.substring(start, end).trim());
		}

		return sentences;
	}

	public List<TextSegment> charsplit(Document document) {
		if (document.text() == null || document.text().isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		String text = document.text();
		int textLength = text.length();

		if (segmentLength <= 0 || segmentOverlap < 0 || segmentLength <= segmentOverlap) {
			throw new IllegalArgumentException(
					String.format("Invalid segmentLength (%s) or segmentOverlap (%s)", segmentLength, segmentOverlap));
		}

		List<TextSegment> segments = new ArrayList<>();
		if (textLength <= segmentLength) {
			segments.add(document.toTextSegment());
		} else {
			for (int i = 0; i < textLength - segmentOverlap; i += segmentLength - segmentOverlap) {
				int endIndex = Math.min(i + segmentLength, textLength);
				String segment = text.substring(i, endIndex);
				segments.add(TextSegment.from(segment, document.metadata()));
				if (endIndex == textLength) {
					break;
				}
			}
		}

		return segments;
	}

	public List regSplit(Document document, String regex) {
		String text = document.text();
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Document text should not be null or empty");
		}

		String[] segments = text.split(regex);

		return stream(segments).map(segment -> TextSegment.from(segment, document.metadata())).collect(toList());
	}

}