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
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@FetchGroups(
    { @FetchGroup(name = "users", members =
        { @Persistent(name = "users") }) })
@PersistenceCapable(table = "{unit}", schema = "top,organizationalUnit", detachable = "true")
public class Team implements Serializable
{
    private static final long serialVersionUID = -9148490053803454465L;

    @Persistent(column = "ou", primaryKey = "true")
    protected String name;

    @Persistent(defaultFetchGroup = "true")
    protected Unit unit;

    @Persistent(mappedBy = "team")
    protected Set<User> users = new HashSet<User>();


    public Team()
    {
    }


    public Team( String name, Unit unit )
    {
        this.name = name;
        this.unit = unit;
    }


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public Unit getUnit()
    {
        return unit;
    }


    public void setUnit( Unit unit )
    {
        this.unit = unit;
    }


    public Set<User> getUsers()
    {
        return users;
    }


    public void setUsers( Set<User> users )
    {
        this.users = users;
    }


    @Override
    public String toString()
    {
        return "Team [name=" + name + ", unit=" + unit + "]";
    }

}
