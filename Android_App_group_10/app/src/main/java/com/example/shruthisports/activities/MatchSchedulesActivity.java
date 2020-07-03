package com.example.shruthisports.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.shruthisports.adapters.SportsListDetailsAdapter;
import com.example.shruthisports.classes.SportsListDetails;
import com.example.shruthisports.classes.SportsListHeading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchSchedulesActivity extends AppCompatActivity {

    SharedPreferences userPref;
    ArrayList<SportsListHeading> scheduledSports;
    RecyclerView matchDetailsRecyclerView;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_schedules);

        scheduledSports = new ArrayList<>();
        matchDetailsRecyclerView = findViewById(R.id.matchDetailsRecyclerView);
        matchDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String sportName = getIntent().getStringExtra("sport_name");

        userPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");

        queue = Volley.newRequestQueue(this);
        String url = "https://group-10-user-api.herokuapp.com/schedule?sport_name="+sportName;
        arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject sport = response.getJSONObject(i);

                                String sportName = sport.getString("sport_name");
                                String matchTitle = sport.getString("match_title");
                                String branch1 = sport.getString("team1");
                                String branch2 = sport.getString("team2");
                                String startTime = sport.getString("start_time");
                                String reportingTime = sport.getString("reporting_time");
                                String matchDate = sport.getString("match_date");
                                String venue = sport.getString("venue");

                                ArrayList<SportsListDetails> details = new ArrayList<>();
                                details.add(new SportsListDetails("Title: "+matchTitle));
                                details.add(new SportsListDetails("Date: "+matchDate));
                                details.add(new SportsListDetails("Start Time: " + startTime));
                                details.add(new SportsListDetails("Reporting Time: " + reportingTime));
                                details.add(new SportsListDetails("Venue: " + venue));
                                SportsListHeading sportsListHeading = new SportsListHeading(branch1 + " vs " + branch2, details);

                                scheduledSports.add(sportsListHeading);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error2",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                        SportsListDetailsAdapter adapter = new SportsListDetailsAdapter(scheduledSports);
                        matchDetailsRecyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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