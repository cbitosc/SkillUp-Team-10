package com.example.shruthisports.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shruthisports.R;
import com.example.shruthisports.classes.SportsListDetails;
import com.example.shruthisports.viewholders.SportsListDetailsViewHolder;
import com.example.shruthisports.classes.SportsListHeading;
import com.example.shruthisports.viewholders.SportsListHeadingViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class SportsListDetailsAdapter extends ExpandableRecyclerViewAdapter<SportsListHeadingViewHolder, SportsListDetailsViewHolder> {
    public SportsListDetailsAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public SportsListHeadingViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_sports_heading, parent,false);
        return new SportsListHeadingViewHolder(view);
    }

    @Override
    public SportsListDetailsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_sports_details, parent,false);
        return new SportsListDetailsViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SportsListDetailsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final SportsListDetails sportsListDetails = (SportsListDetails) group.getItems().get(childIndex);
        holder.bind(sportsListDetails);
    }

    @Override
    public void onBindGroupViewHolder(SportsListHeadingViewHolder holder, int flatPosition, ExpandableGroup group) {
        final SportsListHeading sportsListHeading = (SportsListHeading) group;
        holder.bind(sportsListHeading);
    }
}
