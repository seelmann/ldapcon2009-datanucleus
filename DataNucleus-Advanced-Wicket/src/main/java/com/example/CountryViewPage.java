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

import com.example.dao.CountryDao;


public class CountryViewPage extends WebPage
{
    private final WebPage previousPage;
    private final Country country;


    public CountryViewPage( WebPage previousPage, Country country )
    {
        this.previousPage = previousPage;
        this.country = new CountryDao().loadWithUnits( country.getName() );
        add( new FeedbackPanel( "feedback" ) );
        add( new ViewForm( "countryViewForm" ) );
    }

    public final class ViewForm extends Form<User>
    {
        private static final long serialVersionUID = 1L;


        public ViewForm( final String componentName )
        {

            super( componentName );
            add( new Label( "name", new PropertyModel<Unit>( country, "name" ) ) );

            List<Unit> units = new ArrayList<Unit>( country.getUnits() );
            add( new ListView<Unit>( "units", units )
            {
                private static final long serialVersionUID = 1L;


                @Override
                protected void populateItem( ListItem<Unit> item )
                {
                    Unit unit = item.getModelObject();
                    Link<Unit> unitViewLink = new Link<Unit>( "unitView", item.getModel() )
                    {
                        private static final long serialVersionUID = 1L;


                        public void onClick()
                        {
                            setResponsePage( new UnitViewPage( CountryViewPage.this, getModelObject() ) );
                        }
                    };
                    item.add( unitViewLink );
                    unitViewLink.add( new Label( "unitName", unit.getName() ) );
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
