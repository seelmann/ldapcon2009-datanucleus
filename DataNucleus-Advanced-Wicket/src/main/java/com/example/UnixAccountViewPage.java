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

import com.example.dao.UnixAccountDao;


public class UnixAccountViewPage extends WebPage
{
    private final WebPage previousPage;
    private final UnixAccount unixAccount;


    public UnixAccountViewPage( WebPage previousPage, UnixAccount unixAccount )
    {
        this.previousPage = previousPage;
        this.unixAccount = new UnixAccountDao().loadWithSecondaryUnixGroups( unixAccount.getUid() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "unixAccountViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "uid", new PropertyModel<UnixAccount>( unixAccount, "uid" ) ) );
            add( new Label( "uidNumber", new PropertyModel<UnixAccount>( unixAccount, "uidNumber" ) ) );
            add( new Label( "homeDirectory", new PropertyModel<UnixAccount>( unixAccount, "homeDirectory" ) ) );

            Link<UnixAccount> userViewLink = new Link<UnixAccount>( "userView", new Model<UnixAccount>( unixAccount ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new UserViewPage( UnixAccountViewPage.this, getModelObject().getUser() ) );
                }
            };
            add( userViewLink );
            userViewLink.add( new Label( "userLabel", unixAccount.getUser().getId() ) );

            Link<UnixAccount> primaryGroupViewLink = new Link<UnixAccount>( "primaryGroupView", new Model<UnixAccount>(
                unixAccount ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new UnixGroupViewPage( UnixAccountViewPage.this, getModelObject()
                        .getPrimaryUnixGroup() ) );
                }
            };
            add( primaryGroupViewLink );
            primaryGroupViewLink.add( new Label( "primaryGroupLabel", unixAccount.getPrimaryUnixGroup().getName() ) );

            List<UnixGroup> secondaryGroups = new ArrayList<UnixGroup>( unixAccount.getSecondaryUnixGroups() );
            add( new ListView<UnixGroup>( "secondaryGroups", secondaryGroups )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<UnixGroup> item )
                {
                    UnixGroup group = item.getModelObject();
                    Link<UnixGroup> secondaryGroupsViewLink = new Link<UnixGroup>( "secondaryGroupsView", item
                        .getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UnixGroupViewPage( UnixAccountViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( secondaryGroupsViewLink );
                    secondaryGroupsViewLink.add( new Label( "secondaryGroupsLabel", group.getName() ) );
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
