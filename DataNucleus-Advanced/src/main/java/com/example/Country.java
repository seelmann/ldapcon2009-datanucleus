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
    { @FetchGroup(name = "units", members =
        { @Persistent(name = "units") }) })
@PersistenceCapable(table = "ou=Users,dc=example,dc=com", schema = "top,country", detachable = "true")
public class Country implements Serializable
{
    private static final long serialVersionUID = -9148490053803454465L;

    @Persistent(column = "c", primaryKey = "true")
    protected String name;

    @Persistent(mappedBy = "country")
    protected Set<Unit> units = new HashSet<Unit>();


    public Country()
    {
    }


    public Country( String name )
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }


    public Set<Unit> getUnits()
    {
        return units;
    }


    public void setUnits( Set<Unit> units )
    {
        this.units = units;
    }


    @Override
    public String toString()
    {
        return "Country [name=" + name + "]";
    }

}
