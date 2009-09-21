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
package com.example.dao;


import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;


public abstract class AbstractDao<T>
{

    private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory( "datanucleus.properties" );

    private Class<T> type;


    @SuppressWarnings("unchecked")
    protected AbstractDao()
    {
        this.type = ( Class<T> ) ( ( ParameterizedType ) getClass().getGenericSuperclass() ).getActualTypeArguments()[0];
    }


    @SuppressWarnings("unchecked")
    protected Collection<T> findByQuery( String filter, String params, Object value, String... fetchGroups )
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        applyFetchPlan( pm, fetchGroups );
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query query = pm.newQuery( type );
            query.setFilter( filter );
            query.declareParameters( params );
            Collection<T> c = ( Collection<T> ) query.execute( value );
            tx.commit();
            return c;
        }
        finally
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            pm.close();
        }
    }


    protected T load( Object id, String... fetchGroups )
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        applyFetchPlan( pm, fetchGroups );
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            T t = pm.getObjectById( type, id );
            tx.commit();
            return t;
        }
        finally
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            pm.close();
        }
    }


    @SuppressWarnings("unchecked")
    protected Collection<T> loadAll( String... fetchGroups )
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        applyFetchPlan( pm, fetchGroups );
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query query = pm.newQuery( type );
            Collection<T> c = ( Collection<T> ) query.execute();
            tx.commit();
            return c;
        }
        finally
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            pm.close();
        }
    }


    private void applyFetchPlan( PersistenceManager pm, String[] fetchGroups )
    {
        pm.getFetchPlan().setMaxFetchDepth( 10 );
        for ( String fetchGroup : fetchGroups )
        {
            pm.getFetchPlan().addGroup( fetchGroup );
        }

    }


    public T save( T object )
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            object = pm.makePersistent( object );
            tx.commit();
            return object;
        }
        finally
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            pm.close();
        }
    }


    public void delete( T object )
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            pm.deletePersistent( object );
            tx.commit();
        }
        finally
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            pm.close();
        }
    }
}
