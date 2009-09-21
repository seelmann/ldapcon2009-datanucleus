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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@FetchGroups(
    {
    //
        @FetchGroup(name = "groups", members =
            { @Persistent(name = "memberOf", recursionDepth = 1) }),
        // 
        @FetchGroup(name = "transitiveGroups", members =
            { @Persistent(name = "memberOf", recursionDepth = -1) }),
        //
        @FetchGroup(name = "directMembers", members =
            { @Persistent(name = "members", recursionDepth = 1) }),
        //
        @FetchGroup(name = "transitiveMembers", members =
            { @Persistent(name = "members", recursionDepth = -1) }) })
@PersistenceCapable(table = "ou=Groups,dc=example,dc=com", schema = "top,groupOfNames", detachable = "true")
public class Group extends GroupMember implements Serializable
{
    private static final long serialVersionUID = 1333669774618510795L;

    @Persistent(column = "member")
    @Extension(vendorName = "datanucleus", key = "empty-value", value = "uid=admin,ou=system")
    protected Set<GroupMember> members = new HashSet<GroupMember>();


    public Group()
    {
    }


    public Group( String id )
    {
        super( id );
    }


    public Set<GroupMember> getMembers()
    {
        return members;
    }


    public void setMembers( Set<GroupMember> members )
    {
        this.members = members;
    }


    @Override
    public String toString()
    {
        return "Group [id=" + id + "]";
    }

    /**
     * Gets all members (transitive) of this group.
     * Requires that all members are accessible.
     * @return
     */
    public Collection<GroupMember> getAllMembers()
    {
        Set<GroupMember> membersToProcess = new HashSet<GroupMember>( getMembers() );
        Set<GroupMember> members = new HashSet<GroupMember>();
        while ( !membersToProcess.isEmpty() )
        {
            GroupMember member = membersToProcess.iterator().next();
            membersToProcess.remove( member );
            members.add( member );
            if(member instanceof Group)
            {
                Group group = (Group) member;
                if ( group.getMembers() != null )
                {
                    for ( GroupMember m : group.getMembers() )
                    {
                        if ( !membersToProcess.contains( m ) && !members.contains( m ) )
                        {
                            membersToProcess.add( m );
                        }
                    }
                }
            }
        }
        return members;
    }
}
