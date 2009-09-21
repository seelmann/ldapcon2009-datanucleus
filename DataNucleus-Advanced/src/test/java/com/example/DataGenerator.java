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


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.dao.CountryDao;
import com.example.dao.GroupDao;
import com.example.dao.TeamDao;
import com.example.dao.UnitDao;
import com.example.dao.UnixAccountDao;
import com.example.dao.UnixGroupDao;
import com.example.dao.UserDao;


/**
 * Creates some example data in the LDAP server.
 */
public class DataGenerator
{
    public static void main( String[] args )
    {
        UserDao userDao = new UserDao();
        GroupDao groupDao = new GroupDao();
        CountryDao countryDao = new CountryDao();
        UnitDao unitDao = new UnitDao();
        TeamDao teamDao = new TeamDao();
        UnixGroupDao unixGroupDao = new UnixGroupDao();
        UnixAccountDao unixAccountDao = new UnixAccountDao();

        final int NUM_ORG = 3;
        final int NUM_GROUPS = 3;
        final int NUM_GROUP_LEVEL = 3;
        final int NUM_GROUP_MEMBERS = 3;
        final int NUM_USERS = 3;

        List<String> countryIds = createCountries( countryDao, NUM_ORG );
        List<String> unitIds = createUnits( unitDao, countryDao, countryIds, NUM_ORG );
        List<String> teamIds = createTeams( teamDao, unitDao, unitIds, NUM_ORG );
        List<String> userIds = createUsers( userDao, teamDao, teamIds, NUM_USERS );
        createGroups( groupDao, userDao, userIds, null, NUM_GROUPS, NUM_GROUP_LEVEL, NUM_GROUP_MEMBERS );
        createUnixGroupsAndAccounts( unixGroupDao, unixAccountDao, userDao, userIds, NUM_GROUP_MEMBERS );
    }


    private static List<String> createCountries( CountryDao countryDao, int orgCount )
    {
        List<String> countryIds = new ArrayList<String>();
        for ( int i = 0; i < orgCount; i++ )
        {
            String name = "Country-" + i;
            Country country = new Country( name );
            country = countryDao.save( country );
            countryIds.add( country.getName() );
        }
        return countryIds;
    }


    private static List<String> createUnits( UnitDao unitDao, CountryDao countryDao, List<String> countryIds,
        int orgCount )
    {
        List<String> unitIds = new ArrayList<String>();
        for ( String countryId : countryIds )
        {
            Country country = countryDao.load( countryId );
            for ( int i = 0; i < orgCount; i++ )
            {
                String name = "Unit-" + country.getName().substring( 8 ) + "-" + i;
                Unit unit = new Unit( name, country );
                unit = unitDao.save( unit );
                unitIds.add( unit.getName() );
            }
        }
        return unitIds;
    }


    private static List<String> createTeams( TeamDao teamDao, UnitDao unitDao, List<String> unitIds, int orgCount )
    {
        List<String> teamIds = new ArrayList<String>();
        for ( String unitId : unitIds )
        {
            Unit unit = unitDao.load( unitId );
            for ( int i = 0; i < orgCount; i++ )
            {
                String name = "Team-" + unit.getName().substring( 5 ) + "-" + i;
                Team team = new Team( name, unit );
                team = teamDao.save( team );
                teamIds.add( team.getName() );
            }
        }
        return teamIds;
    }


    private static List<String> createGroups( GroupDao groupDao, UserDao userDao, List<String> userIds,
        String superGroupId, int groupCount, int groupLevels, int groupMemberCount )
    {
        List<String> groupIds = new ArrayList<String>();
        for ( int i = 0; i < groupCount; i++ )
        {
            Group superGroup = superGroupId != null ? groupDao.load( superGroupId ) : null;
            String name = superGroup != null ? superGroup.getId() + "-" + i : "Group-" + i;
            Group group = new Group( name );
            group = groupDao.save( group );
            groupIds.add( group.getId() );

            if ( superGroup != null )
            {
                group = groupDao.loadWithDirectGroups( group.getId() );
                group.getMemberOf().add( superGroup );
                group = groupDao.save( group );
            }

            if ( groupMemberCount > 0 )
            {
                group = groupDao.loadWithDirectMembers( group.getId() );
                for ( int k = 0; k < groupMemberCount; k++ )
                {
                    // TODO: avoid duplicate random index
                    int randomIndex = new Random().nextInt( userIds.size() );
                    String userId = userIds.get( randomIndex );
                    User user = userDao.load( userId );
                    group.getMembers().add( user );
                }
                group = groupDao.save( group );
            }

            if ( groupLevels > 1 )
            {
                List<String> nestedGroups = createGroups( groupDao, userDao, userIds, group.getId(), groupCount,
                    groupLevels - 1, groupMemberCount );
                groupIds.addAll( nestedGroups );
            }

        }
        return groupIds;
    }


    private static List<String> createUsers( UserDao userDao, TeamDao teamDao, List<String> teamIds, int userCount )
    {
        List<String> userIds = new ArrayList<String>();
        for ( String teamId : teamIds )
        {
            Team team = teamDao.load( teamId );
            for ( int i = 0; i < userCount; i++ )
            {
                String name = "User-" + team.getName().substring( 5 ) + "-" + i;
                User user = new User( name, "Foo", "Bar", team );
                user = userDao.save( user );
                user = userDao.load( user.getId() );
                userIds.add( user.getId() );
            }
        }
        return userIds;
    }


    private static void createUnixGroupsAndAccounts( UnixGroupDao unixGroupDao, UnixAccountDao unixAccountDao,
        UserDao userDao, List<String> userIds, int groupMemberCount )
    {
        List<String> unixGroupIds = new ArrayList<String>();
        List<String> unixAccountIds = new ArrayList<String>();
        int gidNumber = 1000;
        int uidNumber = 2000;
        for ( String userId : userIds )
        {
            User user = userDao.load( userId );

            String name = "g-" + user.getId().substring( 5 );
            UnixGroup unixGroup = new UnixGroup( name, gidNumber );
            unixGroupIds.add( name );
            gidNumber++;

            String uid = "u-" + user.getId().substring( 5 );
            UnixAccount unixAccount = new UnixAccount( uid, "secret12", uidNumber, "/home/" + uid, unixGroup, user );
            unixAccountIds.add( uid );
            uidNumber++;
            unixAccountDao.save( unixAccount );
        }

        for ( String accountId : unixAccountIds )
        {
            UnixAccount account = unixAccountDao.loadWithSecondaryUnixGroups( accountId );
            for ( int k = 0; k < groupMemberCount; k++ )
            {
                int randomIndex = new Random().nextInt( unixGroupIds.size() );
                String groupId = unixGroupIds.get( randomIndex );
                UnixGroup group = unixGroupDao.load( groupId );
                account.getSecondaryUnixGroups().add( group );
            }
            unixAccountDao.save( account );
        }
    }

}
