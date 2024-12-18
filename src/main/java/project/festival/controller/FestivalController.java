package project.festival.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.festival.domain.FestivalFilter;
import project.festival.domain.Festivals;
import project.festival.service.FestivalService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/festivals")
public class FestivalController {

    private final FestivalService festivalService;

    @Autowired
    public FestivalController(FestivalService festivalDataService) {
        this.festivalService = festivalDataService;
    }

//    @GetMapping
//    public List<Festivals> getAllFestivals() {
//        return festivalService.getAllFestivals();
//    }

    @GetMapping("/{festivalId}")
    public Optional<Festivals> getFestivalById(@PathVariable Long festivalId) {
        return festivalService.getFestivalById(festivalId);
    }

    @PostMapping()
    public List<Festivals> getFestivalByFilter(@RequestBody FestivalFilter filter) {
        return festivalService.getFestivalByFilter(filter);
    }
}
