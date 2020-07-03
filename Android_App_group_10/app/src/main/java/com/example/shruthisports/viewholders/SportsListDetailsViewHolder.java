package com.example.shruthisports.viewholders;

import android.view.View;
import android.widget.TextView;

import com.example.shruthisports.R;
import com.example.shruthisports.classes.SportsListDetails;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SportsListDetailsViewHolder extends ChildViewHolder {

    private TextView mTextView;

    public SportsListDetailsViewHolder(View itemView) {
        super(itemView);

        mTextView = itemView.findViewById(R.id.textView);
    }

    public void bind(SportsListDetails sportsListDetails){
        mTextView.setText(sportsListDetails.name);
    }

}
