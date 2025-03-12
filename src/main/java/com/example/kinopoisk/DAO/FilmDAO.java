package com.example.kinopoisk.DAO;

import com.example.kinopoisk.Configs.HibernateUtil;
import com.example.kinopoisk.Entities.Film;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    public void addFilm(Film film) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(film);
            transaction.commit();
        }
    }

    public Film getFilmById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Film.class, id);
        }
    }

    public List<Film> getAllFilms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Film", Film.class).list();
        }
    }

    public void updateFilm(Film film) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(film);
            transaction.commit();
        }
    }

    public void deleteFilm(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Film film = session.get(Film.class, id);
            if (film != null) {
                session.remove(film);
            }
            transaction.commit();
        }
    }

    public List<Film> searchFilms(String title, String director, Integer year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Film> cq = cb.createQuery(Film.class);
            Root<Film> film = cq.from(Film.class);

            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(film.get("title"), "%" + title + "%"));
            }
            if (director != null && !director.isEmpty()) {
                predicates.add(cb.like(film.get("director"), "%" + director + "%"));
            }
            if (year != null) {
                predicates.add(cb.equal(film.get("year"), year));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            return session.createQuery(cq).getResultList();
        }
    }
}