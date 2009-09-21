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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@FetchGroups(
    { @FetchGroup(name = "directGroups", members =
        { @Persistent(name = "memberOf", recursionDepth = 1) }),
    //
        @FetchGroup(name = "transitiveGroups", members =
            { @Persistent(name = "memberOf", recursionDepth = -1) }) })
@PersistenceCapable(table = "{team}", schema = "top,person,organizationalPerson,inetOrgPerson", detachable = "true")
public class User extends GroupMember implements Serializable
{
    private static final long serialVersionUID = -9085471388039051384L;

    @Persistent(column = "givenName")
    protected String firstName;

    @Persistent(column = "sn")
    protected String lastName;

    @Persistent(column = "employeeNumber")
    protected long personNumber;

    @Persistent(column = "description", defaultFetchGroup = "true")
    protected Calendar dayOfBirth;

    @Persistent(column = "telephoneNumber", defaultFetchGroup = "true")
    protected Set<String> phoneNumbers = new HashSet<String>();

    @Persistent(defaultFetchGroup = "true")
    protected Team team;

    @Persistent(column = "uid", defaultFetchGroup = "true")
    @Join(column = "uid")
    protected UnixAccount unixAccount;


    public User()
    {
    }


    public User( String id, String firstName, String lastName, Team team )
    {
        super( id );
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
    }


    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }


    public String getLastName()
    {
        return lastName;
    }


    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }


    public long getPersonNumber()
    {
        return personNumber;
    }


    public void setPersonNumber( long personNumber )
    {
        this.personNumber = personNumber;
    }


    public Calendar getDayOfBirth()
    {
        return dayOfBirth;
    }


    public void setDayOfBirth( Calendar dayOfBirth )
    {
        this.dayOfBirth = dayOfBirth;
    }


    public Set<String> getPhoneNumbers()
    {
        return phoneNumbers;
    }


    public void setPhoneNumbers( Set<String> phoneNumbers )
    {
        this.phoneNumbers = phoneNumbers;
    }


    public Team getTeam()
    {
        return team;
    }


    public void setTeam( Team team )
    {
        this.team = team;
    }


    public UnixAccount getUnixAccount()
    {
        return unixAccount;
    }


    public void setUnixAccount( UnixAccount unixAccount )
    {
        this.unixAccount = unixAccount;
    }


    @Override
    public String toString()
    {
        return "User [id=" + id + ", dayOfBirth=" + dayOfBirth + ", firstName=" + firstName + ", lastName=" + lastName
            + ", personNumber=" + personNumber + ", phoneNumbers=" + phoneNumbers + ", team=" + team + "]";
    }


    /**
     * Gets all groups (transitive) this user is member of.
     * Requires that all groups are accessible.
     * @return
     */
    public Collection<Group> getAllGroups()
    {
        Set<Group> groupsToProcess = new HashSet<Group>( getMemberOf() );
        Set<Group> groups = new HashSet<Group>();
        while ( !groupsToProcess.isEmpty() )
        {
            Group group = groupsToProcess.iterator().next();
            groupsToProcess.remove( group );
            groups.add( group );
            if ( group.getMemberOf() != null )
            {
                for ( Group g : group.getMemberOf() )
                {
                    if ( !groupsToProcess.contains( g ) && !groups.contains( g ) )
                    {
                        groupsToProcess.add( g );
                    }
                }
            }
        }
        return groups;
    }

}
