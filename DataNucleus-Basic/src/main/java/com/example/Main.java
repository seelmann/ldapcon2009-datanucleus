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


import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;


public class Main
{

    @SuppressWarnings("unchecked")
    public static void main( String[] args )
    {
        // obtain the persistence manager
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory( "datanucleus.properties" );
        PersistenceManager pm = pmf.getPersistenceManager();

        // create a user
        User user = new User( "Bugs Bunny", "Bugs", "Bunny" );
        user.setAccountId( "bbunny" );
        user.setPassword( "secret12" );
        user.setPersonNumber( 99 );
        user = pm.makePersistent( user );
        Object id = pm.getObjectId( user );
        System.out.println( "Persisted " + user );

        user = ( User ) pm.getObjectById( id );
        System.out.println( "Retrieved " + user );

        // search for users which last name begins with 'B'
        Query query = pm.newQuery( User.class );
        query.setFilter( "lastName.startsWith('B')" );
        Collection<User> users = ( Collection<User> ) query.execute();
        System.out.println( "Retrieved users which last name starts with a 'B':" );
        for ( User u : users )
        {
            System.out.println( "-> " + u );
        }

        // count number of users
        Query countQuery = pm.newQuery( "SELECT count(fullName) FROM com.example.User" );
        Long count = ( Long ) countQuery.execute();
        System.out.println( "Number of users: " + count );

        // get the max person number
        Query maxPersonNumberQuery = pm.newQuery( "SELECT max(personNumber) FROM com.example.User" );
        Long maxPersonNumber = ( Long ) maxPersonNumberQuery.execute();
        System.out.println( "Max. person number: " + maxPersonNumber );

        // delete the user
        pm.deletePersistent( user );
        System.out.println( "Deleted User" );
        System.out.println( "" );
    }

}
