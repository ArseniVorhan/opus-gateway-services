package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;

public class SearchSuggestionModelTest {

	@Field("text")
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
