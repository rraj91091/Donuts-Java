package com.project.donuts.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonutRepository extends JpaRepository<Donut, UUID> {

    public Donut findByFlavour(String flavour);

}