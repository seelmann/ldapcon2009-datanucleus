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
import org.apache.wicket.model.PropertyModel;

import com.example.dao.GroupDao;


public class GroupViewPage extends WebPage
{
    private final WebPage previousPage;
    private final Group group;


    public GroupViewPage( WebPage previousPage, Group group )
    {
        this.previousPage = previousPage;
        this.group = new GroupDao().loadComplete( group.getId() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "groupViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;

        List<Group> memberOf = new ArrayList<Group>();


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "ID", new PropertyModel<Group>( group, "id" ) ) );

            List<Group> groups = new ArrayList<Group>( group.getMemberOf() );
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
                            setResponsePage( new GroupViewPage( GroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( groupViewLink );
                    groupViewLink.add( new Label( "groupID", group.getId() ) );
                }
            } );

            List<GroupMember> members = new ArrayList<GroupMember>( group.getMembers() );
            List<User> userMembers = new ArrayList<User>();
            List<Group> groupMembers = new ArrayList<Group>();
            for ( GroupMember member : members )
            {
                if ( member instanceof User )
                {
                    userMembers.add( ( User ) member );
                }
                else
                {
                    groupMembers.add( ( Group ) member );
                }
            }
            add( new ListView<Group>( "groupMembers", groupMembers )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<Group> item )
                {
                    Group group = item.getModelObject();
                    Link<Group> groupMemberViewLink = new Link<Group>( "groupMemberView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new GroupViewPage( GroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( groupMemberViewLink );
                    groupMemberViewLink.add( new Label( "groupMemberID", group.getId() ) );
                }
            } );
            add( new ListView<User>( "userMembers", userMembers )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<User> item )
                {
                    User user = item.getModelObject();
                    Link<User> userMemberViewLink = new Link<User>( "userMemberView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UserViewPage( GroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( userMemberViewLink );
                    userMemberViewLink.add( new Label( "userMemberID", user.getId() ) );
                }
            } );

            List<GroupMember> transitiveMembers = new ArrayList<GroupMember>( group.getAllMembers() );
            List<User> transitveUserMembers = new ArrayList<User>();
            List<Group> transitiveGroupMembers = new ArrayList<Group>();
            for ( GroupMember member : transitiveMembers )
            {
                if ( member instanceof User )
                {
                    transitveUserMembers.add( ( User ) member );
                }
                else
                {
                    transitiveGroupMembers.add( ( Group ) member );
                }
            }
            add( new ListView<User>( "transitiveUserMembers", transitveUserMembers )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<User> item )
                {
                    User user = item.getModelObject();
                    Link<User> userMemberViewLink = new Link<User>( "transitiveUserMemberView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UserViewPage( GroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( userMemberViewLink );
                    userMemberViewLink.add( new Label( "transitiveUserMemberID", user.getId() ) );
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
