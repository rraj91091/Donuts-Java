package com.project.donuts.domain;

public class DonutDTO {
    private String flavour;
    private Double diameter;
    private Integer quantity;

    public DonutDTO(String flavour, Double diameter, Integer quantity) {
        this.flavour = flavour;
        this.diameter = diameter;
        this.quantity = quantity;
    }

    public DonutDTO() {
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
}
