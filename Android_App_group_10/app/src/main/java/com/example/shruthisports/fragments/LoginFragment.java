package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;
import com.example.shruthisports.activities.CaptainActivity;
import com.example.shruthisports.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    //creating objects for UI components
    EditText loginIdET;
    EditText loginPasswordET;
    CheckBox loginCaptainCB;
    Button loginBtn;
    CheckBox keepLoggedInCB;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JSONObject data;
    Context mContext;

    Boolean isCaptainCB;
    SharedPreferences pref;
    SharedPreferences userPref;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing UI component variables with corresponding components
        loginIdET = view.findViewById(R.id.loginIdET);
        loginPasswordET = view.findViewById(R.id.loginPasswordET);
        loginCaptainCB = view.findViewById(R.id.loginCaptainCB);
        loginBtn = view.findViewById(R.id.loginBtn);
        keepLoggedInCB = view.findViewById(R.id.keepLoggedInCB);

        pref = getActivity().getSharedPreferences("captain",Context.MODE_PRIVATE);
        if(pref.contains("isCaptainCB")) {
            isCaptainCB = pref.getBoolean("isCaptainCB", false);
            if (isCaptainCB) {
                loginCaptainCB.setChecked(true);
                SharedPreferences.Editor mEditor = pref.edit();
                mEditor.putBoolean("isCaptainCB", false);
                mEditor.commit();
            }
        }

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        if(!userPref.getBoolean("keepLoggedIn",true)&&(userPref.getString("password","").equals(""))){

            //Intializing JSON object
            data = new JSONObject();

            //setting onClickListener to login Button
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean valid = isValid();
                    if((!loginIdET.getText().toString().equals(""))&&keepLoggedInCB.isChecked()){
                        SharedPreferences.Editor uEditor = userPref.edit();
                        uEditor.putBoolean("keepLoggedIn", true);
                        uEditor.putLong("userId",Long.parseLong(loginIdET.getText().toString()));
                        uEditor.putString("password",loginPasswordET.getText().toString());
                        uEditor.commit();
                    }
                }
            });
        }else{
            Intent intent = new Intent(mContext,MainActivity.class);
            if(loginCaptainCB.isChecked()){
                intent = new Intent(mContext, CaptainActivity.class);
            }
            startActivity(intent);
        }
    }

    //function to validate the details given by the user
    private Boolean isValid() {
        //inserting user given values into JSON object
        final Long userId = Long.parseLong(loginIdET.getText().toString());
        String password=loginPasswordET.getText().toString();
        Boolean captain=loginCaptainCB.isChecked();
        try{
            data.put("user_id",userId);
            data.put("password_",password);
        }catch(Exception e){
            Toast.makeText(mContext,"Please check your details",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        String url="https://group-10-user-api.herokuapp.com/Userlogin";
        queue = Volley.newRequestQueue(mContext);
        objectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accessTkn = null;
                        SharedPreferences.Editor uEditor = userPref.edit();
                        try {
                            accessTkn = response.getString("access_token");
                            uEditor.putString("access_token",accessTkn);
                            uEditor.putLong("userId",userId);
                            uEditor.commit();
                            //Toast.makeText(mContext,"access"+accessTkn,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            if(loginCaptainCB.isChecked()){
                                intent = new Intent(mContext, CaptainActivity.class);
                            }
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginIdET.setText("");
                loginPasswordET.setText("");
                Toast.makeText(mContext,"Please check your credentials and try again",Toast.LENGTH_LONG).show();
            }
        });
        queue.add(objectRequest);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}
