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


import static com.example.AssertUtils.assertAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.directory.server.core.filtering.EntryFilteringCursor;
import org.apache.directory.server.core.integ.Level;
import org.apache.directory.server.core.integ.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.core.integ.annotations.Factory;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.shared.ldap.filter.ExprNode;
import org.apache.directory.shared.ldap.filter.PresenceNode;
import org.apache.directory.shared.ldap.filter.SearchScope;
import org.apache.directory.shared.ldap.message.AliasDerefMode;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.dao.CountryDao;
import com.example.dao.TeamDao;
import com.example.dao.UnitDao;


@RunWith(SiRunner.class)
@CleanupLevel(Level.SYSTEM)
@Factory(ApacheDsFactory.class)
@ApplyLdifFiles(
    { "/dit.ldif" })
public class OrgDaoTest
{

    public static LdapServer ldapServer;


    @Test
    public void testCreateCountry() throws Exception
    {
        // create the department
        Country c = new Country( "C-1" );
        CountryDao dao = new CountryDao();
        dao.save( c );

        // assert the department was created
        assertAttribute( ldapServer, "c=C-1,ou=Users,dc=example,dc=com", "c", "C-1" );

        // assert no sub departments
        ExprNode filter = new PresenceNode( "objectClass" );
        EntryFilteringCursor result = ldapServer.getDirectoryService().getAdminSession().search(
            new LdapDN( "c=C-1,ou=Users,dc=example,dc=com" ), SearchScope.ONELEVEL, filter,
            AliasDerefMode.DEREF_ALWAYS, null );
        result.beforeFirst();
        assertFalse( result.next() );
        result.close();
    }


    @Test
    public void testCreateTeam() throws Exception
    {
        // create the department including sub departments
        Country c1 = new Country( "C-1" );
        Unit unit11 = new Unit( "Unit-1-1", c1 );
        Team team111 = new Team( "Team-1-1-1", unit11 );
        Unit unit12 = new Unit( "Unit-1-2", c1 );
        Team team121 = new Team( "Team-1-2-1", unit12 );
        Country c2 = new Country( "C-2" );
        Unit unit21 = new Unit( "Unit-2-1", c2 );
        Team team211 = new Team( "Team-2-1-1", unit12 );
        Unit unit22 = new Unit( "Unit-2-2", c2 );
        Team team221 = new Team( "Team-2-2-1", unit22 );
        CountryDao countryDao = new CountryDao();
        UnitDao unitDao = new UnitDao();
        TeamDao teamDao = new TeamDao();
        countryDao.save( c1 );
        unitDao.save( unit11 );
        teamDao.save( team111 );
        unitDao.save( unit12 );
        teamDao.save( team121 );
        countryDao.save( c2 );
        unitDao.save( unit21 );
        teamDao.save( team211 );
        unitDao.save( unit22 );
        teamDao.save( team221 );

        // assert the departments were created
        AssertUtils.assertEntryExists( ldapServer, "c=C-1,ou=Users,dc=example,dc=com" );
        assertAttribute( ldapServer, "c=C-1,ou=Users,dc=example,dc=com", "c", "C-1" );
        AssertUtils.assertEntryExists( ldapServer, "o=Unit-1-1,c=C-1,ou=Users,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "o=Unit-1-2,c=C-1,ou=Users,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "ou=Team-1-2-1,o=Unit-1-2,c=C-1,ou=Users,dc=example,dc=com" );

        // assert they could be loaded again
        Team load = teamDao.load( "Team-1-2-1" );
        assertNotNull( load.getUnit() );
        assertEquals( "Unit-1-2", load.getUnit().getName() );
        assertNotNull( load.getUnit().getCountry() );
        assertEquals( "C-1", load.getUnit().getCountry().getName() );
        
        c1 = countryDao.loadWithUnits( "C-1" );
        assertEquals( 2, c1.getUnits().size() );
        c2 = countryDao.loadWithUnits( "C-2" );
        assertEquals( 2, c2.getUnits().size() );
        unit11 = unitDao.loadWithTeams( "Unit-1-1" );
        assertEquals(1, unit11.getTeams().size());
    }

}
