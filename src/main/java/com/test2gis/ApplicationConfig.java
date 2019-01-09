package com.test2gis;

import com.test2gis.api.v1.service.HallService;
import com.test2gis.api.v1.converter.HallDtoConverter;
import com.test2gis.api.v1.converter.SeatDtoConverter;
import com.test2gis.database.HibernateSessionFactory;
import com.test2gis.database.HibernateSessionFactoryFactory;
import com.test2gis.domain.HallRepository;
import com.test2gis.errorhandling.AppExceptionMapper;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("com.test2gis.api");
        register(LoggingFeature.class);
        register(JacksonFeature.class);
        register(ImmediateFeature.class);
        register(AppExceptionMapper.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(HibernateSessionFactoryFactory.class)
                        .to(SessionFactory.class)
                        .in(Immediate.class);
                bindFactory(HibernateSessionFactory.class)
                        .to(Session.class)
                        .in(RequestScoped.class);
                bindAsContract(HallRepository.class);
                bindAsContract(HallDtoConverter.class);
                bindAsContract(SeatDtoConverter.class);
                bindAsContract(HallService.class);
            }
        });
    }
}
