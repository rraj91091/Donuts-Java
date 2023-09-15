package com.project.donuts.web;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonutService {

    private final DonutRepository donutRepository;

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
        return donutRepository.findAll();
    }
}
