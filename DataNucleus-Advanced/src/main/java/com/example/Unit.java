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
    { @FetchGroup(name = "teams", members =
        { @Persistent(name = "teams") }) })
@PersistenceCapable(table = "{country}", schema = "top,organization", detachable = "true")
public class Unit implements Serializable
{
    private static final long serialVersionUID = -9148490053803454465L;

    @Persistent(column = "o", primaryKey = "true")
    protected String name;

    @Persistent(defaultFetchGroup = "true")
    protected Country country;

    @Persistent(mappedBy = "unit")
    protected Set<Team> teams = new HashSet<Team>();


    public Unit()
    {
    }


    public Unit( String name, Country country )
    {
        this.name = name;
        this.country = country;
    }


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public Country getCountry()
    {
        return country;
    }


    public void setCountry( Country country )
    {
        this.country = country;
    }


    public Set<Team> getTeams()
    {
        return teams;
    }


    public void setTeams( Set<Team> teams )
    {
        this.teams = teams;
    }


    @Override
    public String toString()
    {
        return "Unit [name=" + name + ", country=" + country + "]";
    }

}
