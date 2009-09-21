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
    { @FetchGroup(name = "members", members =
        { @Persistent(name = "primaryMembers"), @Persistent(name = "secondaryMembers") }) })
@PersistenceCapable(table = "ou=UnixGroups,dc=example,dc=com", schema = "top,posixGroup", detachable = "true")
public class UnixGroup implements Serializable
{
    private static final long serialVersionUID = -9148490053803454465L;

    @Persistent(column = "cn", primaryKey = "true")
    protected String name;

    @Persistent(column = "gidNumber")
    protected int gidNumber;

    @Persistent(mappedBy = "primaryUnixGroup")
    private Set<UnixAccount> primaryMembers = new HashSet<UnixAccount>();

    @Persistent(column = "memberUid")
    @Join(column = "uid")
    private Set<UnixAccount> secondaryMembers = new HashSet<UnixAccount>();


    public UnixGroup()
    {
    }


    public UnixGroup( String name, int gidNumber )
    {
        this.name = name;
        this.gidNumber = gidNumber;
    }


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public int getGidNumber()
    {
        return gidNumber;
    }


    public void setGidNumber( int gidNumber )
    {
        this.gidNumber = gidNumber;
    }


    public Set<UnixAccount> getPrimaryMembers()
    {
        return primaryMembers;
    }


    public void setPrimaryMembers( Set<UnixAccount> primaryMembers )
    {
        this.primaryMembers = primaryMembers;
    }


    public Set<UnixAccount> getSecondaryMembers()
    {
        return secondaryMembers;
    }


    public void setSecondaryMembers( Set<UnixAccount> secondaryMembers )
    {
        this.secondaryMembers = secondaryMembers;
    }


    @Override
    public String toString()
    {
        return "UnixGroup [name=" + name + ", gidNumber=" + gidNumber + "]";
    }

}
