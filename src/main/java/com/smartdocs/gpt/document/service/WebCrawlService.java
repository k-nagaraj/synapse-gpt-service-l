package com.smartdocs.gpt.document.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartdocs.gpt.mongo.model.TrainingStatus;
import com.smartdocs.gpt.mongo.repository.TrainingStatusRepository;
import com.smartdocs.gpt.mongo.vector.collection.UrlObject;
import com.smartdocs.gpt.mongo.vector.collection.VectorDocuments;
import com.smartdocs.gpt.mongo.vector.repository.UrlObjectRepository;
import com.smartdocs.gpt.mongo.vector.repository.VectorDocumentsRepository;
import com.smartdocs.gpt.openai.service.OpenAIService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebCrawlService {

	@Autowired
	private UrlObjectRepository urlObjectRepository;

	@Autowired
	private VectorDocumentsRepository vectorDocumentsRepository;

	@Autowired
	private OpenAIService openAIService;

	@Autowired
	private TrainingStatusRepository trainingStatusRepository;

	public List<String> crawl(String url, int depth) {
		Set<String> visitedUrls = new HashSet<>();
		String basePath = url;
		List<String> result = new ArrayList<>();
		Map<String, String> urlContent = new HashedMap<>();
		visitedUrls.clear();

		crawlURL(basePath, url, depth, result, urlContent, visitedUrls);

		for (Map.Entry<String, String> entry : urlContent.entrySet()) {
			UrlObject crawlDetails = new UrlObject();
			crawlDetails.setId(entry.getKey());
			crawlDetails.setContent(entry.getValue());
			urlObjectRepository.save(crawlDetails);
		}

		return result;
	}

	private void crawlURL(String basePath, String url, int depth, List<String> result, Map<String, String> urlContent,
			Set<String> visitedUrls) {
		if (depth == 0 || visitedUrls.contains(url) || !isSameBasePath(url, basePath)) {
			return;
		}

		try {
			Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(20000).get();

			String content = document.text();
//	            String safeKey = url.replace(".", "_");
			result.add(url);
			urlContent.put(url, content.toString());
			visitedUrls.add(url);

			Elements links = document.select("a[href]");
			for (Element link : links) {
				String nextUrl = link.absUrl("href");
				crawlURL(basePath, nextUrl, depth - 1, result, urlContent, visitedUrls);
			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}

	private boolean isSameBasePath(String url, String basePath) {
		return url.startsWith(basePath);
	}

	public boolean trainOnUrl(List<String> urls, String botId) {
		for (String url : urls) {
			TrainingStatus trainingStatus = new TrainingStatus();
			trainingStatus.setDocumentId(botId + url);
			trainingStatus.setStatus("In-Progress");
			trainingStatus = trainingStatusRepository.save(trainingStatus);
			try {

				Optional<UrlObject> urlOptional = urlObjectRepository.findById(url);
				if (urlOptional.isPresent()) {
					UrlObject urlObject = urlOptional.get();
					String text = urlObject.getContent();
					text = preprocessText(text);
					List<String> chunks = getChunks(text);

					for (String content : chunks) {

						List<Double> embedding = openAIService.createEmbeddingInDouble(content);
						var vectorDocument = new VectorDocuments();
						vectorDocument.setDocumentContent(content);
						vectorDocument.setEmbeddings(embedding);
						vectorDocument.setPage(0);
						vectorDocument.setSiteId(botId);
						vectorDocument.setDocumentId(url);
						vectorDocument.setDocumentName(url);
						vectorDocument = vectorDocumentsRepository.save(vectorDocument);

					}
					trainingStatus.setStatus("Completed");
				}

			} catch (Exception e) {
				trainingStatus.setErrorMessage(e.getMessage());
				log.info("Some Error occurred while training");
				trainingStatus.setStatus("Some error occurred");
				e.printStackTrace();
			}
			trainingStatusRepository.save(trainingStatus);
		}
		return true;
	}

	public String preprocessText(String inputText) {
		Pattern nonAlphanumericPattern = Pattern.compile("[^a-zA-Z0-9\\s]");
		Pattern extraSpacesPattern = Pattern.compile("\\s{2,}");
		Pattern newlinePattern = Pattern.compile("\\r?\\n");

		Matcher matcher = nonAlphanumericPattern.matcher(inputText);
		String cleanText = matcher.replaceAll(" ");
		matcher = newlinePattern.matcher(cleanText);
		cleanText = matcher.replaceAll(" ");

		matcher = extraSpacesPattern.matcher(cleanText);
		cleanText = matcher.replaceAll(" ");

		return cleanText.trim();
	}

	public static List<String> getChunks(String input) {
		List<String> dividedStrings = new ArrayList<>();
		int maxLength = 7061;

		int length = input.length();
		int startIndex = 0;

		while (startIndex < length) {
			int endIndex = Math.min(startIndex + maxLength, length);
			String substring = input.substring(startIndex, endIndex);
			dividedStrings.add(substring);
			startIndex = endIndex;
		}

		return dividedStrings;
	}

}
