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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;
import com.example.shruthisports.adapters.SportsListDetailsAdapter;
import com.example.shruthisports.classes.SportsListDetails;
import com.example.shruthisports.classes.SportsListHeading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CaptainMatchFragment extends Fragment {

    ListView listView;
    ArrayList<SportsListHeading> scheduledSports;
    ArrayList<String> sportNames = new ArrayList<String>();
    Context mContext = getContext();
    SharedPreferences userPref;
    RecyclerView matchDetailsRecyclerView;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;

    public CaptainMatchFragment() {
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
        return inflater.inflate(R.layout.fragment_captain_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        matchDetailsRecyclerView = view.findViewById(R.id.matchesDetailsRecyclerView);
        matchDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        func();
    }

    public void func(){
        scheduledSports = new ArrayList<SportsListHeading>();
        sportNames.add("cricket"); sportNames.add("football"); sportNames.add("basketball"); sportNames.add("badminton");
        sportNames.add("kabaddi"); sportNames.add("table%20tennis"); sportNames.add("volley%20ball"); sportNames.add("chess");
        sportNames.add("carroms"); sportNames.add("throw%20ball");

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        final Long captainId = Long.parseLong(userPref.getString("userId",""));

        Iterator sportsIterator = sportNames.iterator();
        while(sportsIterator.hasNext()) {
            queue = Volley.newRequestQueue(mContext);
            String url = "https://group-10-user-api.herokuapp.com/reporting_time?team_id="+captainId+"&sport_name="+sportsIterator.next();
            arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if(response.length()>0) {
                                    JSONObject sport = response.getJSONObject(0);

                                    String sportName = sport.getString("sport_name");
                                    String matchTitle = sport.getString("match_title");
                                    String branch1 = sport.getString("team1");
                                    String branch2 = sport.getString("team2");
                                    String startTime = sport.getString("start_time");
                                    String reportingTime = sport.getString("reporting_time");
                                    String matchDate = sport.getString("match_date");
                                    String venue = sport.getString("venue");
                                    ArrayList<SportsListDetails> details = new ArrayList<>();
                                    details.add(new SportsListDetails("Sport: "+sportName));
                                    details.add(new SportsListDetails("Title: "+matchTitle));
                                    details.add(new SportsListDetails("Date: "+matchDate));
                                    details.add(new SportsListDetails("Start TIme: " + startTime));
                                    details.add(new SportsListDetails("Reporting Time: " + reportingTime));
                                    details.add(new SportsListDetails("Venue: "+venue));
                                    SportsListHeading sportsListHeading = new SportsListHeading(branch1 + " vs " + branch2, details);
                                    scheduledSports.add(sportsListHeading);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext,e.toString(),Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                Toast.makeText(mContext,e.toString(),Toast.LENGTH_LONG).show();
                            }
                            SportsListDetailsAdapter adapter = new SportsListDetailsAdapter(scheduledSports);
                            matchDetailsRecyclerView.setAdapter(adapter);
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
            arrayRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }
                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }
                @Override
                public void retry(VolleyError error) throws VolleyError {}
            });
            queue.add(arrayRequest);
        }
    }
}