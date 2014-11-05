package com.junit.utils.pojo;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Root {
	private List<Classe> classes;

	public Root() {
	}

	public Root(List<Classe> classes) {
		this.classes = classes;
	}

	public List<Classe> getClasses() {
		return this.classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}
}
