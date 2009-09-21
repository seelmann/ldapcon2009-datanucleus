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
package com.example.dao;


import java.util.Collection;

import javax.jdo.JDOHelper;

import com.example.User;


public class UserDao extends AbstractDao<User>
{

    public Collection<User> findByName( String name )
    {
        return super.findByQuery( "id.startsWith(s1) || firstName.startsWith(s1) || lastName.startsWith(s1)",
            "java.lang.String s1", name );
    }


    public Collection<User> findByNameWithDirectGroups( String name )
    {
        return super.findByQuery( "id.startsWith(s1) || firstName.startsWith(s1) || lastName.startsWith(s1)",
            "java.lang.String s1", name, "directGroups" );
    }


    public Collection<User> findByNameWithTransitiveGroups( String name )
    {
        return super.findByQuery( "id.startsWith(s1) || firstName.startsWith(s1) || lastName.startsWith(s1)",
            "java.lang.String s1", name, "transitiveGroups" );
    }


    public User load( Object id )
    {
        return super.load( id );
    }


    public User loadWithDirectGroups( Object id )
    {
        return super.load( id, "directGroups" );
    }


    public User loadComplete( Object id )
    {
        return super.load( id, "transitiveGroups" );
    }


    public Collection<User> loadAll()
    {
        return super.loadAll();
    }


    public Collection<User> loadAllWithTransitiveGroups()
    {
        return super.loadAll( "transitiveGroups" );
    }


    @Override
    public User save( User user )
    {
        JDOHelper.makeDirty( user, "memberOf" );
        return super.save( user );
    }

}
