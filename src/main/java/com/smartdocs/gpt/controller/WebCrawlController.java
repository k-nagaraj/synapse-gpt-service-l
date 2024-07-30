package com.smartdocs.gpt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartdocs.gpt.document.service.WebCrawlService;

@RestController
@RequestMapping("/crawler")
public class WebCrawlController {
	
	@Autowired
	private WebCrawlService webCrawlService;
	
	@PostMapping("/crawl")
	public List<String> crawl(String url){
		return webCrawlService.crawl(url, 2);
	}
	
	@PostMapping("/trainUrl/{botId}")
	public boolean trainOnUrl(@RequestBody List<String> url,@PathVariable(value = "botId") String botId) {
		return webCrawlService.trainOnUrl(url, botId);
	}
	
		

}
