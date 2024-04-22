package com.project.donuts.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DonutDTO {

    @NotBlank(message = "Field `flavour` must not be blank")
    private String flavour;

    @Positive(message = "Field `diameter` must be greater than zero")
    @NotNull(message = "Field `diameter` must not be null")
    private Double diameter;

    @Positive(message = "Field `quantity` must be greater than zero")
    @NotNull(message = "Field `quantity` must not be null")
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
