package com.junit.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.junit.utils.pojo.Classe;
import com.junit.utils.pojo.Method;
import com.junit.utils.pojo.Root;
import com.junit.utils.pojo.TestcaseExtender;
import com.junit.utils.pojo.generated.Testcase;
import com.junit.utils.pojo.generated.Testsuite;
import com.junit.utils.pojo.generated.Testsuites;
import com.sun.tools.javadoc.Main;

public class JUnitExtender extends Task {
	
	private String path_javadoc;
	private File path_docletXML;
	private String path_junitreport;
	private String path_junitreportextender;
	
	public void execute() throws BuildException{
		try{
			path_docletXML = Files.createTempFile("junitextender", "doclet.xml").toFile();
			String[] args ={"-doclet","com.junit.utils.DocletXML", "-sourcepath", path_javadoc, "-subpackages", "main" 
					,  "-classpath", "d:\\Development\\IDE\\eclipse\\plugins\\org.junit_4.11.0.v201303080030\\junit.jar","-outputdir", path_docletXML.getAbsolutePath()};
			System.out.println(Arrays.toString(args));
			Main.execute(args);
			
			System.out.println("BACK TO JAVADOC");
		    Root root_javadoc = readData(path_docletXML);
		    System.out.println(root_javadoc);
		    System.out.println("READ " + path_junitreport);
		    List<Testsuite> testsuits = readDataTest(path_junitreport);
		    System.out.println("BACK TO PROGRAMS" + testsuits);
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
		}catch (JAXBException | IOException e) {
			throw new BuildException(e);
		}
	}
	
	public void setJUnitReport(String path){
		path_junitreport = path;
	}
	
	public void setJavadoc(String path){
		path_javadoc = path;
	}
	
	public void setJUnitReportExtender(String path){
		path_junitreportextender = path;
	}

	
	private void writeTS(Testsuite ts) throws JAXBException, IOException{
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Testsuite.class, TestcaseExtender.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				Boolean.valueOf(true));
		System.out.println("CREATE " + path_junitreportextender);
		Files.createDirectories(Paths.get(path_junitreportextender));
		marshaller.marshal(ts, new File(path_junitreportextender + "/TEST-" + ts.getName() + ".xml"));
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
