package com.example.shruthisports.classes;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class SportsListHeading extends ExpandableGroup<SportsListDetails> {
    public SportsListHeading(String title, List<SportsListDetails> items) {
        super(title, items);
    }
}
