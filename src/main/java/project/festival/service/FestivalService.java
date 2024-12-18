package project.festival.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.festival.domain.FestivalFilter;
import project.festival.domain.Festivals;
import project.festival.repository.FestivalRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;

    @Autowired
    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

//    public List<Festivals> getAllFestivals() {
//        return festivalRepository.findAll();
//    }

    public Optional<Festivals> getFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId);
    }

    public List<Festivals> getFestivalByFilter(FestivalFilter festivalFilter) {

        return festivalRepository.findByFilter(festivalFilter);
    }
}
