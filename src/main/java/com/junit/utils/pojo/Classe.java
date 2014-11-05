package com.junit.utils.pojo;

import java.util.List;

public class Classe {
	private String name;
	private String qualifiedName;
	private String description;
	private List<Method> methods;

	public Classe() {
	}

	public Classe(String name, String qualifiedName, String description,
			List<Method> methods) {
		this.name = name;
		this.qualifiedName = qualifiedName;
		this.description = description;
		this.methods = methods;
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

	public List<Method> getMethods() {
		return this.methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
}
