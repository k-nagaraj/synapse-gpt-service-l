package com.smartdocs.gpt.openai.model;

import java.util.List;

import lombok.Data;

@Data
public class ChatResponse {

	private List<Choice> choices;

	public static class Choice {

		private int index;
		private Message message;

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

	}
}