package com.project.donuts.web;

import java.util.List;

public class Donuts {
    public List<Donut> donuts;

    public Donuts(List<Donut> donuts) {
        this.donuts = donuts;
    }

    public Donuts() {
    }

    public List<Donut> getDonuts() {
        return donuts;
    }

    public void setDonuts(List<Donut> donuts) {
        this.donuts = donuts;
    }
}
