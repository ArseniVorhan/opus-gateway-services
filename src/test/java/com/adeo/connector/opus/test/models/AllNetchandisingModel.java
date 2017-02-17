package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;

public class AllNetchandisingModel {
	
	@Identifier
    private String id;
	
	@Field("/foundation/v2/attributes/name")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
