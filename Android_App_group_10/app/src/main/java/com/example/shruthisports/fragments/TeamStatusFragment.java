package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TeamStatusFragment extends Fragment {

    Context mContext;
    EditText teamid;
    Spinner spinner;
    Button viewstatus;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;
    SharedPreferences userPref;
    String accessTkn;

    public TeamStatusFragment() {
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

        //Intializing JSON object
        data = new JSONObject();

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        accessTkn = userPref.getString("access_token","");

        teamid=(EditText)view.findViewById(R.id.teamid);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        viewstatus=(Button)view.findViewById(R.id.viewstatus);
        viewstatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String teamId=teamid.getText().toString();
                String sport=spinner.getSelectedItem().toString();
                try{
                    data.put("team_id",Long.parseLong(teamId));
                    data.put("sport_name",sport.toLowerCase());
                }catch(Exception e){
                    Toast.makeText(mContext,"Something Went Wrong. Try Again Later",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                String url="https://group-10-user-api.herokuapp.com/team_status?team_id="+teamId+"&sport_name="+sport;
                queue = Volley.newRequestQueue(mContext);
                arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONObject resp = response.getJSONObject(0);
                                    String status = resp.getString("status");
                                    Toast.makeText(mContext,status,Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        teamid.setText("");
                        Toast.makeText(mContext,error.toString()+" Team Status Not Found",Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + accessTkn);
                        return params;
                    }
                };
                queue.add(arrayRequest);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_status, container, false);
    }
}