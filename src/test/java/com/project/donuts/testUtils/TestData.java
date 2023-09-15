package com.project.donuts.testUtils;

import com.project.donuts.web.Donut;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<Donut> donuts() {
        return Arrays.asList(
                new Donut("chocolate", 16.3, 4),
                new Donut("sugar-glazed", 16.0, 3)
        );
    }

}
