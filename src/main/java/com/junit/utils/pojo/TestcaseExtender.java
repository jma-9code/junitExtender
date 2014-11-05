package com.junit.utils.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.junit.utils.pojo.generated.Failure;
import com.junit.utils.pojo.generated.Testcase;

@XmlRootElement(name = "Testcase")
public class TestcaseExtender extends Testcase{

    @XmlAttribute(name = "description")
    protected String description;
    
    @XmlJavaTypeAdapter(MapAdapter.class)
    protected Map<String, String> tags;
    
    public TestcaseExtender(){
    	super();
    }
    
    public TestcaseExtender(Testcase tc, String description, Map<String, String> tags){
    	super();
    	skipped = tc.getSkipped();
        error = tc.getError();
    	failure = tc.getFailure();
    	systemOut = tc.getSystemOut();
    	systemErr = tc.getSystemErr();
    	name =tc.getName();
    	assertions = tc.getAssertions();
    	time = tc.getTime();
    	classname = tc.getClassname();
    	status = tc.getClassname();
    	this.description = description;
    	this.tags = tags;
    }
    
    public String getDescription() {
 		return description;
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
