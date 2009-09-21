This project contains a Apache Wicket based web frontend for DataNucleus-Advanced.

Requirements:
Java 5 or later and Maven 2.
This example requires a running LDAP server with a standard schema (object classes inetOrgperson,
posixAccount, posixGroup, groupOfNames, country, organization, organizationalUnit) and an example DIT 
(see src/main/resources/example.ldif). To setup an LDAP server within your Eclipse
IDE you could use Apache Directory Studio [1]. A step-by-step tutorial is provided at [2].

Build
- Build project "DataNucleus-Advanced" first with "mvn clean install"
- Build with "mvn clean install"

Run web application with "mvn jetty:run"

[1] http://directory.apache.org/studio
[2] http://www.stefan-seelmann.de/index.php?/archives/8-Setting-up-an-LDAP-server-for-your-development-environment.html