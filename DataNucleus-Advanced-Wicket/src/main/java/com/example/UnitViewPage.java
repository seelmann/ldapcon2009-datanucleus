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

import com.example.dao.UnitDao;


public class UnitViewPage extends WebPage
{
    private final WebPage previousPage;
    private final Unit unit;


    public UnitViewPage( WebPage previousPage, Unit unit )
    {
        this.previousPage = previousPage;
        this.unit = new UnitDao().loadWithTeams( unit.getName() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "unitViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "name", new PropertyModel<Unit>( unit, "name" ) ) );

            Link<Unit> countryViewLink = new Link<Unit>( "countryView", new Model<Unit>( unit ) )
            {
                private static final long serialVersionUID = 1L;


                public void onClick()
                {
                    setResponsePage( new CountryViewPage( UnitViewPage.this, getModelObject().getCountry() ) );
                }
            };
            add( countryViewLink );
            countryViewLink.add( new Label( "countryLabel", unit.getCountry().getName() ) );

            List<Team> teams = new ArrayList<Team>( unit.getTeams() );
            add( new ListView<Team>( "teams", teams )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<Team> item )
                {
                    Team team = item.getModelObject();
                    Link<Team> teamViewLink = new Link<Team>( "teamView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new TeamViewPage( UnitViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( teamViewLink );
                    teamViewLink.add( new Label( "teamName", team.getName() ) );
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
