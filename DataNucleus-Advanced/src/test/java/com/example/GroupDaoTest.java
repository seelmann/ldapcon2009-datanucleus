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


import org.apache.directory.server.core.integ.Level;
import org.apache.directory.server.core.integ.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.core.integ.annotations.Factory;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.dao.GroupDao;


@RunWith(SiRunner.class)
@CleanupLevel(Level.SYSTEM)
@Factory(ApacheDsFactory.class)
@ApplyLdifFiles(
    { "/dit.ldif" })
public class GroupDaoTest
{

    public static LdapServer ldapServer;


    @Test
    public void testCreate() throws Exception
    {
        GroupDao dao = new GroupDao();

        // create groups
        Group sales = new Group( "Sales" );
        dao.save( sales );
        Group engineering = new Group( "Engineering" );
        dao.save( engineering );

        // assert entries were created
        AssertUtils.assertEntryExists( ldapServer, "cn=Sales,ou=Groups,dc=example,dc=com" );
        AssertUtils.assertEntryExists( ldapServer, "cn=Engineering,ou=Groups,dc=example,dc=com" );
    }

}
