This project contains a basic example for LDAP persistence using DataNucleus [1].
It includes the minimal set of libraries and dependencies.

Description:
The com.example.User class describes user objects with its attributes. JDO Annotations 
are used to specify where and how objects of this class are persisted in the LDAP DIT.
The com.example.Main class uses the JDO API to create, query and delete an object.
The connection parameters to the LDAP server are configured in datanucleus.properties. 

Requirements:
This example requires a running LDAP server with a standard schema (object class inetOrgperson) 
and an example DIT (ou=Users,dc=example,dc=com). To setup an LDAP server within your Eclipse
IDE you could use Apache Directory Studio [2]. A step-by-step tutorial is provided at [3].

Build the example:
- You need Eclipse
- Install the DataNucleus Eclipse Plugin from http://www.datanucleus.org/downloads/eclipse-update/
- Import the project into Eclipse
- Build the project, should be done automatically
- Run the DataNucleus Enhancer (righ-click the project, select DataNucleus->Run Enhancer Tool

Run the example:
Warning: the com.example.Main class creates an entry in the LDAP server and deletes it afterwards!
Run com.example.Main class

[1] http://www.datanucleus.org
[2] http://directory.apache.org/studio
[3] http://www.stefan-seelmann.de/index.php?/archives/8-Setting-up-an-LDAP-server-for-your-development-environment.html