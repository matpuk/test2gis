package com.test2gis.database;

import org.glassfish.hk2.api.Factory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class HibernateSessionFactory implements Factory<Session> {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public Session provide() {
        return sessionFactory.openSession();
    }

    @Override
    public void dispose(Session session) {
        if (session.isOpen())
            session.close();
    }
}
