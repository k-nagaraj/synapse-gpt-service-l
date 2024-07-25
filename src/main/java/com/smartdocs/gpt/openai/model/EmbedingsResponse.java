package com.smartdocs.gpt.openai.model;

import java.util.List;

import lombok.Data;

@Data
public class EmbedingsResponse {
	private String object;

	private List<Datum> data;
	public Usage usage;

	public static class Datum {
		public String object;
		private int index;
		public List<Float> embedding;

		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			this.object = object;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public List<Float> getEmbedding() {
			return embedding;
		}

		public void setEmbedding(List<Float> embedding) {
			this.embedding = embedding;
		}

	}

	public static class Usage {
		public int prompt_tokens;
		public int total_tokens;
		public int getPrompt_tokens() {
			return prompt_tokens;
		}
		public void setPrompt_tokens(int prompt_tokens) {
			this.prompt_tokens = prompt_tokens;
		}
		public int getTotal_tokens() {
			return total_tokens;
		}
		public void setTotal_tokens(int total_tokens) {
			this.total_tokens = total_tokens;
		}
		
	}

}
