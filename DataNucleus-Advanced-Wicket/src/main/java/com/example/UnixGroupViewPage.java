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

import com.example.dao.UnixGroupDao;


public class UnixGroupViewPage extends WebPage
{
    private final WebPage previousPage;
    private final UnixGroup unixGroup;


    public UnixGroupViewPage( WebPage previousPage, UnixGroup unixGroup )
    {
        this.previousPage = previousPage;
        this.unixGroup = new UnixGroupDao().loadWithMembers( unixGroup.getName() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "unixGroupViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {
            super( componentName );
            add( new Label( "name", new PropertyModel<UnixGroup>( unixGroup, "name" ) ) );
            add( new Label( "gidNumber", new PropertyModel<UnixGroup>( unixGroup, "gidNumber" ) ) );

            List<UnixAccount> primaryMembers = new ArrayList<UnixAccount>( unixGroup.getPrimaryMembers() );
            add( new ListView<UnixAccount>( "primaryMembers", primaryMembers )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<UnixAccount> item )
                {
                    UnixAccount member = item.getModelObject();
                    Link<UnixAccount> primaryMemberViewLink = new Link<UnixAccount>( "primaryMemberView", item
                        .getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UnixAccountViewPage( UnixGroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( primaryMemberViewLink );
                    primaryMemberViewLink.add( new Label( "primaryMemberLabel", member.getUid() ) );
                }
            } );

            List<UnixAccount> secondaryMembers = new ArrayList<UnixAccount>( unixGroup.getSecondaryMembers() );
            add( new ListView<UnixAccount>( "secondaryMembers", secondaryMembers )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<UnixAccount> item )
                {
                    UnixAccount member = item.getModelObject();
                    Link<UnixAccount> secondaryMemberViewLink = new Link<UnixAccount>( "secondaryMemberView", item
                        .getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UnixAccountViewPage( UnixGroupViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( secondaryMemberViewLink );
                    secondaryMemberViewLink.add( new Label( "secondaryMemberLabel", member.getUid() ) );
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
