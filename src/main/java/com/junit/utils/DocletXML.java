package com.junit.utils;

import com.junit.utils.pojo.Classe;
import com.junit.utils.pojo.Method;
import com.junit.utils.pojo.Root;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class DocletXML extends Doclet{
	
	private static final String JAVADOC_DOCLET_FILE = "/javadoc.xml";
	private static String outputdir = "c:\\";
	
	public static boolean start(RootDoc rootdoc) {
		
		String outputdirOptions = readOptions(rootdoc.options());
		if (outputdirOptions != null){
			outputdir = outputdirOptions;
		}
		
		List<Classe> classes = new ArrayList<Classe>();
		for (ClassDoc c : rootdoc.classes()) {
			List<Method> methods = new ArrayList<Method>();
			for (MethodDoc m : c.methods()) {
				Method method = new Method(m.name(), m.qualifiedName(),
						m.commentText());
				for (Tag t : m.tags()){
					method.getTags().put(t.name(), t.text());
				}
				methods.add(method);
			}
			Classe classe = new Classe(c.name(), c.qualifiedName(),
					c.commentText(), methods);
			classes.add(classe);
		}
		Root root = new Root(classes);

		
		try {
			JAXBContext context = JAXBContext
					.newInstance(new Class[] { Root.class });
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",
					Boolean.valueOf(true));
			marshaller.marshal(root, new File(outputdir + JAVADOC_DOCLET_FILE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
    
	
	private static String readOptions(String[][] options) {
        String tagName = null;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt.length >= 2 && opt[0].equals("-outputdir")){
            	return opt[1];
            }
        }
        return tagName;
    }
	
	public static int optionLength(String option) {
        if(option.equals("-outputdir")) {
            return 2;
        }
        return 0;
    }

    public static boolean validOptions(String options[][], 
                                       DocErrorReporter reporter) {
        boolean foundTagOption = false;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals("-outputdir")) {
                if (foundTagOption) {
                    reporter.printError("Only one -outputdir option allowed.");
                    return false;
                } else { 
                    foundTagOption = true;
                }
            } 
        }
        if (!foundTagOption) {
            reporter.printError("Usage: javadoc -outputdir mytag -doclet ...");
        }
        return foundTagOption;
    }
}
