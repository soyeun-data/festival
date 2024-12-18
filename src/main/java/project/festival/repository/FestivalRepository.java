package project.festival.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import project.festival.domain.FestivalFilter;
import project.festival.domain.Festivals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FestivalRepository{

    private final EntityManager em;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public FestivalRepository(EntityManager em){
        this.em = em;
    }

    public Optional<Festivals> findById(Long id) {
        Festivals festivalData = em.find(Festivals.class, id);
        return Optional.ofNullable(festivalData);
    }

    public List<Festivals> findAll() {
        List<Festivals> festivalList = em.createQuery("select f from Festivals f", Festivals.class).getResultList();
        return festivalList;
    }

    public List<Festivals> findByFilter(FestivalFilter festivalFilter) {

        String locationPrefix = festivalFilter.getLocationPrefix();
        String query = "select f from Festivals f where f.instt_nm LIKE :location";
        String progress = festivalFilter.getProgress();
        String date = festivalFilter.getDate();

        if (progress != null) {
            if ("end".equals(progress)) {
                query += " AND f.fstvl_end_date < :currentDate";
            } else if ("scheduled".equals(progress)) {
                query += " AND f.fstvl_start_date > :currentDate";
            } else if ("progress".equals(progress)) {
                query += " AND f.fstvl_start_date <= :currentDate AND f.fstvl_end_date >= :currentDate";
            }
        }

        if (date != null) {
            if ("asc".equals(date)) {
                query += " ORDER BY f.fstvl_start_date ASC";
            } else if ("desc".equals(date)) {
                query += " ORDER BY f.fstvl_start_date DESC";
            }
        }

        TypedQuery<Festivals> festivalsTypedQuery = em.createQuery(query, Festivals.class);
        festivalsTypedQuery.setParameter("location", locationPrefix + "%");

        if (progress != null) {
            festivalsTypedQuery.setParameter("currentDate", new Date());
        }
        return festivalsTypedQuery.getResultList();
    }
}
