package com.test2gis.domain;

import com.test2gis.domain.entities.Hall;
import com.test2gis.domain.entities.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.inject.Inject;

import java.util.List;

public class HallRepository {

    @Inject
    private Session session;

    public List<Hall> findAll() {
        return session.createQuery("from Hall", Hall.class).list();
    }

    public Hall findById(Long id) {
        return session.find(Hall.class, id);
    }

    public void bookSeats(List<Seat> seats) throws Exception {
        Transaction tx = session.beginTransaction();
        try {
            for (Seat s : seats) {
                session.persist(s);
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();

            Throwable t = ex.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t != null) {
                throw (ConstraintViolationException) t;
            }

            throw ex;
        }
    }
}
