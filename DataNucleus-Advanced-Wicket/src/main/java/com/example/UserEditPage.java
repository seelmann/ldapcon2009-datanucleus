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

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import com.example.dao.GroupDao;
import com.example.dao.TeamDao;
import com.example.dao.UserDao;


public class UserEditPage extends WebPage
{
    private final UserSearchPage userSearchPage;
    private final User user;


    public UserEditPage( UserSearchPage userSearchPage, User user )
    {
        this.userSearchPage = userSearchPage;
        this.user = new UserDao().loadWithDirectGroups( user.getId() );
        add( new FeedbackPanel( "feedback" ) );
        add( new EditForm( "userEditForm" ) );
    }

    public final class EditForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;

        List<Group> memberOf = new ArrayList<Group>();


        public EditForm( final String componentName )
        {

            super( componentName );
            add( new TextField<User>( "firstName", new PropertyModel<User>( user, "firstName" ) ) );
            add( new TextField<User>( "lastName", new PropertyModel<User>( user, "lastName" ) ) );
            add( new TextField<User>( "personNumber", new PropertyModel<User>( user, "personNumber" ) ) );

            TeamDao teamDao = new TeamDao();
            List<Team> allTeams = new ArrayList<Team>( teamDao.loadAll() );
            IModel<Team> teamModel = new PropertyModel<Team>( user, "team" );
            IChoiceRenderer<Team> teamRenderer = new ChoiceRenderer<Team>( "name", "name" );
            DropDownChoice<Team> teamChoice = new DropDownChoice<Team>( "team", teamModel, allTeams, teamRenderer );
            add( teamChoice );

            memberOf.addAll( user.getMemberOf() );
            List<Group> allGroups = new ArrayList<Group>( new GroupDao().loadAll() );
            IChoiceRenderer<Group> groupRenderer = new ChoiceRenderer<Group>( "id", "id" );
            Palette<Group> groupPalette = new Palette<Group>( "groups", new ListModel<Group>( memberOf ),
                new ListModel<Group>( allGroups ), groupRenderer, 10, true );
            add( groupPalette );

            //ListView<T>
            Button cancelButton = new Button( "cancelButton" )
            {
                private static final long serialVersionUID = 1L;


                public void onSubmit()
                {
                    setResponsePage( userSearchPage );
                }
            };
            cancelButton.setDefaultFormProcessing( false );
            add( cancelButton );
            Button saveButton = new Button( "saveButton" )
            {
                private static final long serialVersionUID = 1L;


                public void onSubmit()
                {
                    user.getMemberOf().clear();
                    user.getMemberOf().addAll( memberOf );
                    new UserDao().save( user );
                    setResponsePage( userSearchPage );
                }
            };
            add( saveButton );

        }

    }

}
