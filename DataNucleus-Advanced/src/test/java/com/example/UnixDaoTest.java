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


import java.util.ArrayList;
import java.util.List;

import org.apache.directory.server.core.integ.Level;
import org.apache.directory.server.core.integ.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.core.integ.annotations.Factory;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.shared.ldap.entry.Modification;
import org.apache.directory.shared.ldap.entry.ModificationOperation;
import org.apache.directory.shared.ldap.entry.client.ClientModification;
import org.apache.directory.shared.ldap.entry.client.DefaultClientAttribute;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.dao.UnixAccountDao;
import com.example.dao.UnixGroupDao;


@RunWith(SiRunner.class)
@CleanupLevel(Level.SYSTEM)
@Factory(ApacheDsFactory.class)
@ApplyLdifFiles(
    { "/dit.ldif" })
public class UnixDaoTest
{

    public static LdapServer ldapServer;


    @Before
    public void setUp() throws Exception
    {
        List<Modification> mods = new ArrayList<Modification>();
        Modification mod = new ClientModification( ModificationOperation.REMOVE_ATTRIBUTE, new DefaultClientAttribute(
            "m-disabled" ) );
        mods.add( mod );
        ldapServer.getDirectoryService().getAdminSession().modify( new LdapDN( "cn=nis,ou=schema" ), mods );
    }


    @Test
    public void testCreate() throws Exception
    {
        UnixAccountDao accountDao = new UnixAccountDao();

        // create users
        Country country = new Country( "Country" );
        Unit unit = new Unit( "Unit", country );
        Team team = new Team( "Team", unit );
        User adan = new User( "Adan Abrams", "Adan", "Abrams", team );

        // create account and save
        UnixGroup group = new UnixGroup( "group10", 10 );
        UnixAccount account = new UnixAccount( "user1000", "secret12", 1000, "/home/user1000", group, adan );
        group.getSecondaryMembers().add( account );
        accountDao.save( account );

        // assert entries were created
        AssertUtils.assertEntryExists( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "uidNumber", "1000" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "gidNumber", "10" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "homeDirectory",
            "/home/user1000" );

        AssertUtils.assertEntryExists( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com", "gidNumber", "10" );
        AssertUtils.assertAttribute( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com", "memberUid", "user1000" );

        AssertUtils
            .assertEntryExists( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com",
            "uid", "user1000" );

    }


    @Test
    public void testCRUD() throws Exception
    {
        UnixAccountDao accountDao = new UnixAccountDao();
        UnixGroupDao groupDao = new UnixGroupDao();

        // create users
        Country country = new Country( "Country" );
        Unit unit = new Unit( "Unit", country );
        Team team = new Team( "Team", unit );
        User adan = new User( "Adan Abrams", "Adan", "Abrams", team );

        // create account and save
        UnixGroup group = new UnixGroup( "group10", 10 );
        UnixAccount account = new UnixAccount( "user1000", "secret12", 1000, "/home/user1000", group, adan );
        accountDao.save( account );

        // assert entries were created
        AssertUtils.assertEntryExists( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "uidNumber", "1000" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "gidNumber", "10" );
        AssertUtils.assertAttribute( ldapServer, "uid=user1000,ou=UnixAccounts,dc=example,dc=com", "homeDirectory",
            "/home/user1000" );

        AssertUtils.assertEntryExists( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com", "gidNumber", "10" );

        AssertUtils
            .assertEntryExists( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=Adan Abrams,ou=Team,o=Unit,c=Country,ou=Users,dc=example,dc=com",
            "uid", "user1000" );

        // update
        group = groupDao.load( "group10" );
        account = accountDao.loadWithSecondaryUnixGroups( "user1000" );
        account.getSecondaryUnixGroups().add( group );
        //groupDao.save( group );
        accountDao.save( account );

        AssertUtils.assertEntryExists( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com" );
        AssertUtils.assertAttribute( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com", "gidNumber", "10" );
        AssertUtils.assertAttribute( ldapServer, "cn=group10,ou=UnixGroups,dc=example,dc=com", "memberUid", "user1000" );
    }

}
