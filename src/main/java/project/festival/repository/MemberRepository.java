package project.festival.repository;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import project.festival.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final EntityManager em;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findById(Long id) {
        Member member = em.find(Member.class, id);
        return member;
    }

    public void deleteById(Long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            em.remove(member);
        }
    }

    public Optional<Member> findByLoginId(String loginId){
        List<Member> result = em.createQuery("select m from Member m where loginId= :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return result.stream().findAny();
    }

    public Member updateById(Member member) {
        em.persist(member);
        return member;
    }
}



















