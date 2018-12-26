package com.test2gis.database;

import com.test2gis.domain.entities.Hall;
import com.test2gis.domain.entities.Seat;
import org.glassfish.hk2.api.Factory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactoryFactory implements Factory<SessionFactory> {

     @Override
     public SessionFactory provide() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .loadProperties("db.properties")
                .build();

        MetadataSources metadata = new MetadataSources(registry);
        metadata.addAnnotatedClass(Hall.class);
        metadata.addAnnotatedClass(Seat.class);

        return metadata.buildMetadata().buildSessionFactory();
    }

    @Override
    public void dispose(SessionFactory sf) {}
}
