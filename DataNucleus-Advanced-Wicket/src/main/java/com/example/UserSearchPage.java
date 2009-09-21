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
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import com.example.dao.UserDao;


public class UserSearchPage extends WebPage
{
    private final List<User> searchResult = new ArrayList<User>();


    public UserSearchPage()
    {
        add( new SearchForm( "userSearchForm" ) );
        add( new ListView<User>( "users", searchResult )
        {
            private static final long serialVersionUID = 1L;


            @Override
            protected void populateItem( ListItem<User> item )
            {
                User user = item.getModelObject();
                Link<User> userViewLink = new Link<User>( "userView", item.getModel() )
                {
                    private static final long serialVersionUID = 1L;


                    public void onClick()
                    {
                        setResponsePage( new UserViewPage( UserSearchPage.this, ( User ) getModelObject() ) );
                    }
                };
                item.add( userViewLink );
                userViewLink.add( new Label( "ID", user.getId() ) );

                item.add( new Label( "firstName", user.getFirstName() ) );

                item.add( new Label( "lastName", user.getLastName() ) );

                Link<User> teamViewLink = new Link<User>( "teamView", item.getModel() )
                {
                    private static final long serialVersionUID = 1L;


                    public void onClick()
                    {
                        setResponsePage( new TeamViewPage( UserSearchPage.this, ( ( User ) getModelObject() ).getTeam() ) );
                    }
                };
                item.add( teamViewLink );
                teamViewLink.add( new Label( "teamLabel", user.getTeam().getName() ) );

                Link<User> unitViewLink = new Link<User>( "unitView", item.getModel() )
                {
                    private static final long serialVersionUID = 1L;


                    public void onClick()
                    {
                        setResponsePage( new UnitViewPage( UserSearchPage.this, ( ( User ) getModelObject() ).getTeam()
                            .getUnit() ) );
                    }
                };
                item.add( unitViewLink );
                unitViewLink.add( new Label( "unitLabel", user.getTeam().getUnit().getName() ) );

                Link<User> countryViewLink = new Link<User>( "countryView", item.getModel() )
                {
                    private static final long serialVersionUID = 1L;


                    public void onClick()
                    {
                        setResponsePage( new CountryViewPage( UserSearchPage.this, ( ( User ) getModelObject() )
                            .getTeam().getUnit().getCountry() ) );
                    }
                };
                item.add( countryViewLink );
                countryViewLink.add( new Label( "countryLabel", user.getTeam().getUnit().getCountry().getName() ) );

                item.add( new Link<User>( "userEdit", item.getModel() )
                {
                    private static final long serialVersionUID = 1L;


                    public void onClick()
                    {
                        setResponsePage( new UserEditPage( UserSearchPage.this, ( User ) getModelObject() ) );
                    }
                } );
            }
        } );
    }

    public final class SearchForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;
        private final User user = new User( "User-0-0-0", null, null, null );


        public SearchForm( final String componentName )
        {
            super( componentName );
            add( new TextField<User>( "name", new PropertyModel<User>( user, "id" ) ) );
        }


        public final void onSubmit()
        {
            String name = user.getId() != null ? user.getId() : "";
            Collection<User> users = new UserDao().findByName( name );
            searchResult.clear();
            searchResult.addAll( users );
        }
    }

}
