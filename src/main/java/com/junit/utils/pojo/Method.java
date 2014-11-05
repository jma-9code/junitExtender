package com.junit.utils.pojo;

import java.util.HashMap;
import java.util.Map;

public class Method {
	private String name;
	private String qualifiedName;
	private String description;
	private Map<String, String> tags = new HashMap<>();

	public Method() {
	}

	public Method(String name, String qualifiedName, String description) {
		this.name = name;
		this.qualifiedName = qualifiedName;
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualifiedName() {
		return this.qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
