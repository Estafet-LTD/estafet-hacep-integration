
There are two folders hacep-integration which is the maven project and ansible-terraform where all the needed scripts for setting up an HA environment are placed. 
The maven project consists of 3 child projects hacep-integration-app, hacep-integration-module, hacep-integration-rules and event-publisher (This project was constructed following the instructions for hacep integration in the HACEP architecture document).
hacep-integraion can be imported in eclipse as an existing maven project.

To build the project first thing to do is to clone the hacep source code from 

https://github.com/redhat-italy/hacep

After you have cloned the project, amend the pom.xml file under hacep-core by adding the following dependencies:

<dependency>
	<groupId>org.drools</groupId>
	<artifactId>drools-decisiontables</artifactId>
</dependency>
<dependency>
	<groupId>org.drools</groupId>
	<artifactId>drools-templates</artifactId>
</dependency>

Now you can build the hacep core project (There is a detailed instructions how to build the hacep project on the gihub link above).

Once the hacep project is built successfully you can build the hacep-integration project by execution maven clean install under hacep-integration (You need to have maven settings.xml file configured as per instructions from building the hacep source code project).
