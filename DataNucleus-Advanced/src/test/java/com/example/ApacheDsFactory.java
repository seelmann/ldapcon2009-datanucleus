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


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.integ.IntegrationUtils;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.integ.LdapServerFactory;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.ldap.handlers.extended.StoredProcedureExtendedOperationHandler;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.xdbm.Index;
import org.apache.mina.util.AvailablePortFinder;


public class ApacheDsFactory implements LdapServerFactory
{
    public LdapServer newInstance() throws Exception
    {
        DirectoryService service = new DefaultDirectoryService();
        IntegrationUtils.doDelete( service.getWorkingDirectory() );
        service.getChangeLog().setEnabled( true );
        service.setAllowAnonymousAccess( true );
        service.setShutdownHookEnabled( false );

        JdbmPartition example = new JdbmPartition();
        example.setCacheSize( 500 );
        example.setSuffix( "dc=example,dc=com" );
        example.setId( "example" );
        Set<Index<?, ServerEntry>> indexedAttrs = new HashSet<Index<?, ServerEntry>>();
        indexedAttrs.add( new JdbmIndex<String, ServerEntry>( "cn" ) );
        indexedAttrs.add( new JdbmIndex<String, ServerEntry>( "ou" ) );
        indexedAttrs.add( new JdbmIndex<String, ServerEntry>( "dc" ) );
        indexedAttrs.add( new JdbmIndex<String, ServerEntry>( "objectClass" ) );
        example.setIndexedAttributes( indexedAttrs );
        service.addPartition( example );

        LdapServer ldapServer = new LdapServer();
        ldapServer.setDirectoryService( service );
        int port = AvailablePortFinder.getNextAvailable( 1024 );
        ldapServer.setTransports( new TcpTransport( port ) );
        ldapServer.setAllowAnonymousAccess( true );
        ldapServer.addExtendedOperationHandler( new StoredProcedureExtendedOperationHandler() );

        configureDataNucleus( ldapServer );

        return ldapServer;
    }


    private void configureDataNucleus( LdapServer ldapServer ) throws Exception
    {
        String connectionURL = "ldap://localhost:" + ldapServer.getPort();
        URL resource = getClass().getResource( "/datanucleus.properties" );
        String file = resource.getFile();
        Properties properties = new Properties();
        properties.load( new FileInputStream( file ) );
        properties.setProperty( "javax.jdo.option.ConnectionURL", connectionURL );
        properties.store( new FileOutputStream( file ), null );
    }
}
