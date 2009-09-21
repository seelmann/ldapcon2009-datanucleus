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

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.example.dao.UserDao;


public class UserViewPage extends WebPage
{
    private final WebPage previousPage;
    private final User user;


    public UserViewPage( WebPage previousPage, User user )
    {
        this.previousPage = previousPage;
        this.user = new UserDao().loadComplete( user.getId() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "userViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "ID", new PropertyModel<User>( user, "id" ) ) );
            add( new Label( "firstName", new PropertyModel<User>( user, "firstName" ) ) );
            add( new Label( "lastName", new PropertyModel<User>( user, "lastName" ) ) );
            add( new Label( "personNumber", new PropertyModel<User>( user, "personNumber" ) ) );

            Link<User> unixAccountViewLink = new Link<User>( "unixAccountView", new Model<User>( user ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new UnixAccountViewPage( UserViewPage.this, getModelObject().getUnixAccount() ) );
                }
            };
            add( unixAccountViewLink );
            unixAccountViewLink.add( new Label( "unixAccountLabel", user.getUnixAccount().getUid() ) );

            Link<User> teamViewLink = new Link<User>( "teamView", new Model<User>( user ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new TeamViewPage( UserViewPage.this, getModelObject().getTeam() ) );
                }
            };
            add( teamViewLink );
            teamViewLink.add( new Label( "teamLabel", user.getTeam().getName() ) );

            List<Group> groups = new ArrayList<Group>( user.getMemberOf() );
            add( new ListView<Group>( "groups", groups )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<Group> item )
                {
                    Group group = item.getModelObject();
                    Link<Group> groupViewLink = new Link<Group>( "groupView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new GroupViewPage( UserViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( groupViewLink );
                    groupViewLink.add( new Label( "groupID", group.getId() ) );
                }
            } );

            List<Group> transitiveGroups = new ArrayList<Group>( user.getAllGroups() );
            add( new ListView<Group>( "transitiveGroups", transitiveGroups )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<Group> item )
                {
                    Group group = item.getModelObject();
                    Link<Group> transitiveGroupViewLink = new Link<Group>( "transitiveGroupView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new GroupViewPage( UserViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( transitiveGroupViewLink );
                    transitiveGroupViewLink.add( new Label( "transitiveGroupID", group.getId() ) );
                }
            } );

            Button backButton = new Button( "backButton" )
            {
                private static final long serialVersionUID = 1L;


                public void onSubmit()
                {
                    setResponsePage( previousPage );
                }
            };
            backButton.setDefaultFormProcessing( false );
            add( backButton );
        }

    }
}
