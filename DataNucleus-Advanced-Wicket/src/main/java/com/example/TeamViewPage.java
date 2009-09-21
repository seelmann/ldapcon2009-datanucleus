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

import com.example.dao.TeamDao;


public class TeamViewPage extends WebPage
{
    private final WebPage previousPage;
    private final Team team;


    public TeamViewPage( WebPage previousPage, Team team )
    {
        this.previousPage = previousPage;
        this.team = new TeamDao().loadWithUsers( team.getName() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "teamViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "name", new PropertyModel<Team>( team, "name" ) ) );

            Link<Team> unitViewLink = new Link<Team>( "unitView", new Model<Team>( team ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new UnitViewPage( TeamViewPage.this, getModelObject().getUnit() ) );
                }
            };
            add( unitViewLink );
            unitViewLink.add( new Label( "unitLabel", team.getUnit().getName() ) );

            List<User> users = new ArrayList<User>( team.getUsers() );
            add( new ListView<User>( "users", users )
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
                            setResponsePage( new UserViewPage( TeamViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( userViewLink );
                    userViewLink.add( new Label( "userID", user.getId() ) );
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
