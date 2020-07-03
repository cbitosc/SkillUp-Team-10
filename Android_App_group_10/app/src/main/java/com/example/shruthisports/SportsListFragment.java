package com.example.shruthisports;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shruthisports.adapters.SportsAdapter;
import com.example.shruthisports.classes.Sports;

import java.util.ArrayList;

public class SportsListFragment extends Fragment {

    ListView listView;
    ArrayList<Sports> sportsList = new ArrayList<>();
    Context mContext;

    public SportsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sports_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        for(int i=0;i<10;i++){
//            Sportsk s = new Sportsk((long)i+5,"sport"+i,"indoor",i,i+5);
//            sportsList.add(s);
//        }
        listView = view.findViewById(R.id.listView);
        if(sportsList.isEmpty()){
            Toast.makeText(mContext,"No items in sports", Toast.LENGTH_LONG).show();
        }else {
            SportsAdapter adapter = new SportsAdapter(mContext, R.layout.sports, sportsList);
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}