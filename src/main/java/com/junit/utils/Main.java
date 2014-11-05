package com.junit.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.junit.utils.pojo.Classe;
import com.junit.utils.pojo.Method;
import com.junit.utils.pojo.Root;
import com.junit.utils.pojo.TestcaseExtender;
import com.junit.utils.pojo.generated.Testcase;
import com.junit.utils.pojo.generated.Testsuite;
import com.junit.utils.pojo.generated.Testsuites;

public class Main {

	public static void main(String[] args) throws JAXBException, IOException {
	    String path_javadoc = "C:\\Users\\Julien\\Desktop\\javadoc.xml";
	    String path_report = "D:\\Development\\workspace\\Test\\junit";
	    File file = new File(path_javadoc);
	    Root root_javadoc = readData(file);
	    List<Testsuite> testsuits = readDataTest(path_report);
	    
	    //TODO add description class
	    for (Testsuite ts : testsuits){
	    	for (Classe c : root_javadoc.getClasses()){
	    		if (c.getQualifiedName().equals(ts.getName())){
	    			List<TestcaseExtender> tces = new ArrayList<TestcaseExtender>();
	    			for (Testcase tc : ts.getTestcase()){
	    		    	for (Method m : c.getMethods()){
	    		    		if (m.getQualifiedName().equals(tc.getClassname()+ "." + tc.getName())){
	    		    			TestcaseExtender tce = new TestcaseExtender(tc, m.getDescription(), m.getTags());
	    		    			tces.add(tce);
	    		    		}
	    		    	}
	    		    }
	    			ts.getTestcase().clear();
	    			ts.getTestcase().addAll(tces);
	    		}
	    	}	
	    	writeTS(ts);
	    } 
	    
	    

	}
	
	private static void writeTS(Testsuite ts) throws JAXBException, IOException{
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Testsuite.class, TestcaseExtender.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output",
				Boolean.valueOf(true));
		Files.createDirectories(Paths.get("reports"));
		marshaller.marshal(ts, new File("reports/TEST-" + ts.getName() + ".xml"));
	}
	
	private static Root readData(File f) throws JAXBException{
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Root.class });
		Unmarshaller unmarshall = context.createUnmarshaller();
		
		return (Root) unmarshall.unmarshal(f);
	}
	
	private static List<Testsuite> readDataTest(String path) throws JAXBException{
		List<Testsuite> testcases = new ArrayList<Testsuite>();
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Testsuites.class, Testsuite.class });
		Unmarshaller unmarshall = context.createUnmarshaller();
		
		if (Files.isDirectory(Paths.get(path))){
			for (Path p : getAllFiles(path, null)){
				try{
					Testsuite t = (Testsuite) unmarshall.unmarshal(p.toFile());
					testcases.add(t);
				}catch(JAXBException e){
					
				}
			}
		}else{
			try{
				Testsuites ts = (Testsuites) unmarshall.unmarshal(new File(path));
				return ts.getTestsuite();
			}catch(ClassCastException | JAXBException e){
				
			}
		}
		return testcases;
	}
	
	private static Set<Path> getAllFiles(String dir, Set<Path> ret) {
		if (ret == null){
			ret = new HashSet<Path>();
		}
		Path path = Paths.get(dir);
		if (!Files.isDirectory(path)){
			ret.add(path);
		}
		
        File[] files = path.toFile().listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    ret.addAll(getAllFiles(files[i].getAbsolutePath(), ret));
                }else{
                	ret.add(Paths.get(files[i].getAbsolutePath()));
                }
            }
        }
        return ret;
    }
	
}
