package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;
import com.example.shruthisports.TeamMember;
import com.example.shruthisports.adapters.SportsListDetailsAdapter;
import com.example.shruthisports.classes.SportsListDetails;
import com.example.shruthisports.classes.SportsListHeading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CaptainRegisteredSportsFragment extends Fragment {

    ListView listView;
    ArrayList<SportsListHeading> scheduledSports;
    ArrayList<String> sportCategoryNames = new ArrayList<String>();
    Context mContext;
    SharedPreferences userPref;
    RecyclerView matchDetailsRecyclerView;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;

    public CaptainRegisteredSportsFragment() {
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
        return inflater.inflate(R.layout.fragment_sports_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        matchDetailsRecyclerView = view.findViewById(R.id.SportsDetailsRecyclerView);
        matchDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        func();
    }

    public void func(){
        scheduledSports = new ArrayList<SportsListHeading>();

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        final Long userId = userPref.getLong("userId",0);

        queue = Volley.newRequestQueue(mContext);
        String url = "https://group-10-user-api.herokuapp.com/team_member_details?team_id="+userId;
        arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<TeamMember> teamMembers = new ArrayList<>();
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject member = response.getJSONObject(i);
                                Long memberId = member.getLong("member_ID");
                                String sportName = member.getString("sport_name");
                                Long teamId = member.getLong("team_id");
                                String teamName = member.getString("team_name");
                                String status = member.getString("status");
                                String memberName = member.getString("member_name");
                                String section = member.getString("section");
                                String branch = member.getString("branch");
                                String gender = member.getString("gender");
                                TeamMember tm = new TeamMember(memberId, sportName, teamId, teamName, status, memberName,
                                        section, branch, gender);
                                teamMembers.add(tm);
                            }
                            HashSet<String> sportNames = new HashSet<>();
                            for( TeamMember tm : teamMembers ){
                                if(!sportNames.contains(tm.getSportName())){
                                    sportNames.add(tm.getSportName());
                                }
                            }
                            for(String sName : sportNames){
                                ArrayList<SportsListDetails> details = new ArrayList<>();
                                details.add(new SportsListDetails("Team Members:"));
                                for( TeamMember tm : teamMembers ){
                                    if(tm.getSportName().equals(sName)){
                                        details.add(new SportsListDetails(tm.getMemberName()+" ("+tm.getMemberId()+")"));
                                    }
                                }
                                SportsListHeading sportsListHeading = new SportsListHeading(sName, details);
                                scheduledSports.add(sportsListHeading);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"error2",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                        SportsListDetailsAdapter adapter = new SportsListDetailsAdapter(scheduledSports);
                        matchDetailsRecyclerView.setAdapter(adapter);
                    }}, new Response.ErrorListener() {
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
