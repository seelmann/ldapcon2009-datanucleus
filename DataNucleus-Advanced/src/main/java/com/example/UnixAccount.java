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


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@FetchGroups(
    { @FetchGroup(name = "secondaryUnixGroups", members =
        { @Persistent(name = "secondaryUnixGroups") }) })
@PersistenceCapable(table = "ou=UnixAccounts,dc=example,dc=com", schema = "top,posixAccount,account", detachable = "true")
public class UnixAccount implements Serializable
{
    private static final long serialVersionUID = -9148490053803454465L;

    @Persistent(column = "uid", primaryKey = "true")
    protected String uid;
    
    @Persistent(column = "userPassword")
    protected String password;

    @Persistent(column = "cn")
    protected String name;

    @Persistent(column = "homeDirectory")
    protected String homeDirectory;

    @Persistent(column = "uidNumber")
    protected int uidNumber;

    @Persistent(mappedBy = "unixAccount", defaultFetchGroup = "true")
    protected User user;

    @Persistent(column = "gidNumber", defaultFetchGroup = "true")
    @Join(column = "gidNumber")
    protected UnixGroup primaryUnixGroup;

    @Persistent(mappedBy = "secondaryMembers")
    protected Set<UnixGroup> secondaryUnixGroups = new HashSet<UnixGroup>();


    public UnixAccount()
    {
    }


    public UnixAccount( String uid, String password, int uidNumber, String homeDirectory, UnixGroup primaryUnixGroup, User user )
    {
        this.uid = uid;
        this.password = password;
        this.name = uid;
        this.uidNumber = uidNumber;
        this.homeDirectory = homeDirectory;
        this.primaryUnixGroup = primaryUnixGroup;
        this.user = user;
    }


    public String getUid()
    {
        return uid;
    }


    public void setUid( String uid )
    {
        this.uid = uid;
    }

    public String getPassword()
    {
        return password;
    }
    
    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public String getHomeDirectory()
    {
        return homeDirectory;
    }


    public void setHomeDirectory( String homeDirectory )
    {
        this.homeDirectory = homeDirectory;
    }


    public int getUidNumber()
    {
        return uidNumber;
    }


    public void setUidNumber( int uidNumber )
    {
        this.uidNumber = uidNumber;
    }


    public User getUser()
    {
        return user;
    }


    public void setUser( User user )
    {
        this.user = user;
    }


    public UnixGroup getPrimaryUnixGroup()
    {
        return primaryUnixGroup;
    }


    public void setPrimaryUnixGroup( UnixGroup primaryUnixGroup )
    {
        this.primaryUnixGroup = primaryUnixGroup;
    }


    public Set<UnixGroup> getSecondaryUnixGroups()
    {
        return secondaryUnixGroups;
    }


    public void setSecondaryUnixGroups( Set<UnixGroup> secondaryUnixGroups )
    {
        this.secondaryUnixGroups = secondaryUnixGroups;
    }


    @Override
    public String toString()
    {
        return "UnixAccount [uid=" + uid + ", name=" + name + ", uidNumber=" + uidNumber + ", homeDirectory="
            + homeDirectory + "]";
    }

}
