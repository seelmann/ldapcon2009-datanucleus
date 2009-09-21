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
public class UserDaoTest
{

    public static LdapServer ldapServer;


    @Test
    public void testCreate() throws Exception
    {
        UserDao dao = new UserDao();

        // create users
        Country country = new Country( "Country" );
        Unit unit = new Unit( "Unit", country );
        Team team = new Team( "Team", unit );
        User adan = new User( "Adan Abrams", "Adan", "Abrams", team );
        dao.save( adan );
        User chuck = new User( "Chuck Brunato", "Chuck", "Brunato", team );
        dao.save( chuck );

        // assert entries were created
        AssertUtils
            .assertEntryExists( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer,
            "cn=Chuck Brunato,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
    }


    @Test
    public void testCRUD() throws Exception
    {
        GroupDao groupDao = new GroupDao();
        UserDao userDao = new UserDao();

        // create groups
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

        // create users
        Country country = new Country( "Country" );
        Unit unit = new Unit( "Unit", country );
        Team team = new Team( "Team", unit );
        User adan = new User( "Adan Abrams", "Adan", "Abrams", team );
        userDao.save( adan );
        User chuck = new User( "Chuck Brunato", "Chuck", "Brunato", team );
        userDao.save( chuck );

        adan = userDao.loadWithDirectGroups( "Adan Abrams" );
        assertEquals( 0, adan.getMemberOf().size() );
        userDao.save( adan );

        adan = userDao.loadWithDirectGroups( "Adan Abrams" );
        assertEquals( 0, adan.getMemberOf().size() );
        adan.getMemberOf().add( groupDao.load( "Software-Engineer" ) );
        userDao.save( adan );

        adan = userDao.loadWithDirectGroups( "Adan Abrams" );
        assertEquals( 1, adan.getMemberOf().size() );
        adan.getMemberOf().add( groupDao.load( "Hardware-Engineer" ) );
        userDao.save( adan );

        adan = userDao.loadWithDirectGroups( "Adan Abrams" );
        assertEquals( 2, adan.getMemberOf().size() );
    }

}
