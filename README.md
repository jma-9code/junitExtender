# junitExtender
The purpose of this project is to merge the information of the Javadoc with JUnit report.
This project includes two tools:
- An XML doclet
- A new ant task that merges the Javadoc and the junit report

How to use ?
1) Import the maven project
2) call package goal
3) add ant-junitextender.jar to the lib directory of ant
4) Add these elements to build ant :
//define the doclet
<property name="javadoc.doclet.path" value="..../lib/ant-junitextender.jar" />
<property name="javadoc.doclet.class" value="com.junit.utils.DocletXML" />
//define the task...this way can be used only if the plugin has been added to the ant lib directory
<taskdef name="junitReportExtender" classname="com.junit.utils.JUnitExtender"/>
//create javadoc target with this element...generate the javadoc with the xml doclet
<javadoc access="private" classpathref="tests.only.classpath">
		<doclet name="${javadoc.doclet.class}" path="${javadoc.doclet.path}">
			<param name="-outputdir" value=".../javadoc.xml"/>
		</doclet>
		<fileset dir=".../test/" defaultexcludes="yes">
			<include name="**/*.java" />
		</fileset>
</javadoc>

//call the default junitreport task
<junitreport todir=".../results">
	<fileset dir=".../results">
		<include name="*.xml" />
	</fileset>
	<report format="noframes" todir=".../html-results" />
</junitreport>

//then call the junitextendertask
<junitReportExtender junitreport=".../results/TESTS-TestSuites.xml" javadoc=".../javadoc.xml" junitreportextender=".../results/TESTS-TestSuites-extended.xml"/>

//and finally, call xslt task for transform TESTS-TestSuites-extended.xml to HTML frame
<xslt in=".../results/TESTS-TestSuites-extended.xml" out=".../TESTS-TestSuites-extended.html" style="transformToHTML.xsl">
			<param name="TITLE" expression="PROJECT NAME"/>
</xslt>
