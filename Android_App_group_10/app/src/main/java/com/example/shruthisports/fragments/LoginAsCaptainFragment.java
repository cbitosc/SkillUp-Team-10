package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shruthisports.R;
import com.example.shruthisports.activities.LoginActivity;

public class LoginAsCaptainFragment extends Fragment {

    Context mContext;
    Button redirectBtn;
    SharedPreferences pref;

    public LoginAsCaptainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = getActivity().getSharedPreferences("captain", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = pref.edit();
        mEditor.putBoolean("isCaptainCB", true);
        mEditor.commit();
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_as_captain, container, false);
    }
}