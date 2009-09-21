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


import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.shared.ldap.entry.Entry;
import org.apache.directory.shared.ldap.entry.EntryAttribute;
import org.apache.directory.shared.ldap.name.LdapDN;


public class AssertUtils
{

    public static void assertEntryExists( LdapServer ldapServer, String dn ) throws Exception
    {
        assertTrue( ldapServer.getDirectoryService().getAdminSession().exists( new LdapDN( dn ) ) );
    }


    public static void assertEntryNotExists( LdapServer ldapServer, String dn ) throws Exception
    {
        assertFalse( ldapServer.getDirectoryService().getAdminSession().exists( new LdapDN( dn ) ) );
    }


    public static void assertAttribute( LdapServer ldapServer, String dn, String attribute, String... values )
        throws Exception
    {
        Entry entry = ldapServer.getDirectoryService().getAdminSession().lookup( new LdapDN( dn ) );
        assertNotNull( entry );
        EntryAttribute attr = entry.get( attribute );
        assertNotNull( attr );
        assertEquals( values.length, attr.size() );
        assertTrue( attr.contains( values ) );
    }


    public static void assertAttribute( LdapServer ldapServer, String dn, String attribute, byte[]... values )
        throws Exception
    {
        Entry entry = ldapServer.getDirectoryService().getAdminSession().lookup( new LdapDN( dn ) );
        assertNotNull( entry );
        EntryAttribute attr = entry.get( attribute );
        assertNotNull( attr );
        assertEquals( values.length, attr.size() );
        assertTrue( attr.contains( values ) );
    }

}
