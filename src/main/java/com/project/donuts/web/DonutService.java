package com.project.donuts.web;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class DonutService {

    private final DonutRepository donutRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public DonutService(DonutRepository donutRepository) {
        this.donutRepository = donutRepository;
    }

    public Donut createDonut(DonutDTO donut) {
        return donutRepository.save(
                new Donut(
                        donut.getFlavour(),
                        donut.getDiameter(),
                        donut.getQuantity()
                )
        );
    }

    public List<Donut> getDonuts() {
        logger.info("Fetching All Donuts from Donuts Repository");
        return donutRepository.findAll();
    }

    public Donut getDonutById(UUID id) {
        logger.info("Fetching Donut from Donuts Repository using ID");
        return donutRepository.findById(id).orElse(null);
    }
}
