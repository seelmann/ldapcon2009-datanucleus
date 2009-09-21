/**********************************************************************
Copyright (c) 2009 Stefan Seelmann. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **********************************************************************/
package com.example;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.directory.server.core.integ.Level;
import org.apache.directory.server.core.integ.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.core.integ.annotations.Factory;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.dao.GroupDao;
import com.example.dao.UserDao;


@RunWith(SiRunner.class)
@CleanupLevel(Level.SYSTEM)
@Factory(ApacheDsFactory.class)
@ApplyLdifFiles(
    { "/dit.ldif" })
public class DaoTest
{

    public static LdapServer ldapServer;


    @Test
    public void testGroupMembership() throws Exception
    {
        UserDao userDao = new UserDao();
        GroupDao groupDao = new GroupDao();

        // create users
        Country country = new Country( "Country" );
        Unit unit = new Unit( "Unit", country );
        Team team = new Team( "Team", unit );
        User adan = new User( "Adan Abrams", "Adan", "Abrams", team );
        userDao.save( adan );
        User chuck = new User( "Chuck Brunato", "Chuck", "Brunato", team );
        userDao.save( chuck );

        // create groups
        // employee
        //   salesmen
        //   administrator
        //   engineer
        //     swengineer
        //     hwengineer
        // partner
        Group employee = new Group( "Employee" );
        Group salesperson = new Group( "Salesperson" );
        employee.getMembers().add( salesperson );
        Group administrator = new Group( "Administrator" );
        employee.getMembers().add( administrator );
        Group engineer = new Group( "Engineer" );
        employee.getMembers().add( engineer );
        Group swengineer = new Group( "Software-Engineer" );
        engineer.getMembers().add( swengineer );
        Group hwengineer = new Group( "Hardware-Engineer" );
        engineer.getMembers().add( hwengineer );
        Group partner = new Group( "Partner" );
        groupDao.save( employee );
        groupDao.save( partner );

        // assert entries were created
        AssertUtils.assertEntryExists( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Chuck Brunato,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
        
        AssertUtils.assertEntryExists( ldapServer, "cn=Employee,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Employee,ou=Groups,dc=example,dc=com", "member",
            "cn=Salesperson,ou=Groups,dc=example,dc=com", "cn=Administrator,ou=Groups,dc=example,dc=com",
            "cn=Engineer,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Salesperson,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Administrator,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Engineer,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Engineer,ou=Groups,dc=example,dc=com", "member",
            "cn=Software-Engineer,ou=Groups,dc=example,dc=com", "cn=Hardware-Engineer,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Software-Engineer,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Software-Engineer,ou=Groups,dc=example,dc=com", "member",
            "uid=admin,ou=system" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Hardware-Engineer,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Hardware-Engineer,ou=Groups,dc=example,dc=com", "member",
            "uid=admin,ou=system" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Partner,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Partner,ou=Groups,dc=example,dc=com", "member",
            "uid=admin,ou=system" );

        // add users to groups
        swengineer = groupDao.loadWithDirectMembers( "Software-Engineer" );
        adan = userDao.load( "Adan Abrams" );
        swengineer.getMembers().add( adan );
        swengineer = groupDao.save( swengineer );

        // assert group was changed
        AssertUtils.assertAttribute( ldapServer, "cn=Software-Engineer,ou=Groups,dc=example,dc=com", "member",
            "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );

        adan = userDao.loadComplete( "Adan Abrams" );
        swengineer = groupDao.load( "Software-Engineer" );
        engineer = groupDao.load( "Engineer" );
        employee = groupDao.load( "Employee" );
        assertEquals( 3, adan.getAllGroups().size() );
        assertTrue( adan.getAllGroups().contains( swengineer ) );
        assertTrue( adan.getAllGroups().contains( engineer ) );
        assertTrue( adan.getAllGroups().contains( employee ) );
    }

}
