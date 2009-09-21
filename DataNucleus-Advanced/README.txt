This project contains an advanced example for LDAP persistence using DataNucleus [1].

Description:
The package com.example contains the Java Bean classes. JDO Annotations 
are used to specify where and how objects of this classes are persisted in the LDAP DIT.
There are multiple relationships between these classes, a class diagram is included
for illustration. Each class has an appropriate DAO in com.example.dao.
The connection parameters to the LDAP server are configured in datanucleus.properties. 
Some basic JUnit tests that use an embedded Apache Directory Server are included.

An additional project (DataNucleus-Advanced-Wicket) provides a web frontend.

Requirements:
- Java 5 or later
- Maven 2

Build with "mvn clean install"


[1] http://www.datanucleus.org
