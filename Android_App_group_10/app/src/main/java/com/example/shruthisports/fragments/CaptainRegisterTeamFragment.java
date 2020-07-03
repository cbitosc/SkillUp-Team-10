package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;
import com.example.shruthisports.activities.RegisterTeamActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CaptainRegisterTeamFragment extends Fragment {

    Context mContext;
    Button registerTeamBtn;
    Spinner sportsSpinner;
    Spinner branchSpinner;
    Spinner sectionSpinner;
    RadioGroup genderGroup;
    RadioGroup sportCategoryGroup;
    Button getSportsDetailsBtn;
    Integer maxTeamSize;
    ArrayAdapter<String> adapter;
    EditText teamSizeET;

    public CaptainRegisterTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] sportsArray = getResources().getStringArray(R.array.sports_array);

        sportsSpinner = view.findViewById(R.id.sportsSpinner);
        registerTeamBtn = view.findViewById(R.id.teamRegisterBtn);
        branchSpinner = view.findViewById(R.id.branchSpinner);
        sectionSpinner = view.findViewById(R.id.sectionSpinner);
        genderGroup = view.findViewById(R.id.genderRB);
        sportCategoryGroup = view.findViewById(R.id.sportCategoryRB);
        getSportsDetailsBtn = view.findViewById(R.id.getSportsDetailsBtn);
        teamSizeET = view.findViewById(R.id.teamSizeET);

        initializeSportDetails(0);

        registerTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sportName = sportsSpinner.getSelectedItem().toString();
                final String branch = branchSpinner.getSelectedItem().toString();
                final int section = Integer.parseInt(sectionSpinner.getSelectedItem().toString());
                int genderNum = genderGroup.getCheckedRadioButtonId();
                final int teamSize = Integer.parseInt(teamSizeET.getText().toString());
                final String gender;
                if (genderNum == R.id.mensRB) {
                    gender = "MALE";
                } else {
                    gender = "FEMALE";
                }
                openregistration(sportName, branch, section, gender,teamSize);
            }
        });

        getSportsDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sportCategoryGroup.getCheckedRadioButtonId()==R.id.indoorRB) {
                    initializeSportDetails(0);
                }else{
                    initializeSportDetails(1);
                }
            }
        });
    }

    private void openregistration(String sportName, String branch, int section, String gender,int teamSize) {
        SharedPreferences userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        final Long userId = userPref.getLong("userId",0);
        Intent intent = new Intent(mContext, RegisterTeamActivity.class);
        String[] tn = sportName.split(" ");
        sportName = tn[0];
        intent.putExtra("sportName", sportName);
        intent.putExtra("teamId",userId);
        intent.putExtra("branch", branch);
        intent.putExtra("section", section);
        intent.putExtra("gender", gender);
        intent.putExtra("teamSize",teamSize);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_captain_register_team, container, false);
    }

    private void initializeSportDetails(int category) {
        ArrayList<String> sportCategoryNames = new ArrayList<String>();
        if(category==0) {
            sportCategoryNames.add("indoor");
        }else {
            sportCategoryNames.add("outdoor");
        }

        SharedPreferences userPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token", "");

        Iterator sportsIterator = sportCategoryNames.iterator();
        while (sportsIterator.hasNext()) {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            String url = "https://group-10-user-api.herokuapp.com/sportcategory?sport_category=" + sportsIterator.next();
            JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONArray response) {
                            String[] sportNames = new String[response.length()];
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject sport = response.getJSONObject(i);
                                    String sportName = sport.getString("sport_name");
                                    Integer minTeamSize = sport.getInt("mini_team_size");
                                    Integer maxTeamSize = sport.getInt("max_team_size");
                                    sportNames[i]=sportName+" ("+minTeamSize+"-"+maxTeamSize+")";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "error2", Toast.LENGTH_LONG).show();
                            }
                            adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, sportNames);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            sportsSpinner.setAdapter(adapter);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
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
    }
}