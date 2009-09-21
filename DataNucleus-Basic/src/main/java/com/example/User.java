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


import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable(table = "ou=People,dc=example,dc=com", schema = "top,person,organizationalPerson,inetOrgPerson")
public class User
{
    @Persistent(column = "cn", primaryKey = "true")
    private String fullName;

    @Persistent(column = "givenName")
    private String firstName;

    @Persistent(column = "sn")
    private String lastName;

    @Persistent(column = "uid")
    private String accountId;

    @Persistent(column = "userPassword")
    private String password;

    @Persistent(column = "employeeNumber")
    private long personNumber;

    @Persistent(column = "description", defaultFetchGroup = "true")
    private Calendar dayOfBirth;

    @Persistent(column = "telephoneNumber", defaultFetchGroup = "true")
    private Set<String> phoneNumbers = new HashSet<String>();


    public User( String fullName, String firstName, String lastName )
    {
        super();
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFullName()
    {
        return fullName;
    }


    public void setFullName( String fullName )
    {
        this.fullName = fullName;
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


    public String getAccountId()
    {
        return accountId;
    }


    public void setAccountId( String accountId )
    {
        this.accountId = accountId;
    }


    public String getPassword()
    {
        return password;
    }


    public void setPassword( String password )
    {
        this.password = password;
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


    @Override
    public String toString()
    {
        return "User [accountId=" + accountId + ", dayOfBirth=" + dayOfBirth + ", firstName=" + firstName
            + ", fullName=" + fullName + ", lastName=" + lastName + ", password=" + password + ", personNumber="
            + personNumber + ", phoneNumbers=" + phoneNumbers + "]";
    }

}
