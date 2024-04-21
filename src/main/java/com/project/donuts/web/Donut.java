package com.project.donuts.web;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "donut")
public class Donut {

    @Id
    @Column(name = "id")
    private final UUID id = UUID.randomUUID();

    @Column(name = "flavour", length = 100)
    private String flavour;

    @Column(name = "diameter")
    private Double diameter;

    @Column(name = "quantity")
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "createdAt")
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private ZonedDateTime updatedAt;

    public Donut() {
    }

    public Donut(String flavour, Double diameter, Integer quantity) {
        this.flavour = flavour;
        this.diameter = diameter;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}