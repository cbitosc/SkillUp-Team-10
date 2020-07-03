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
import com.example.shruthisports.adapters.SportsListDetailsAdapter;
import com.example.shruthisports.classes.SportsListDetails;
import com.example.shruthisports.classes.SportsListHeading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


public class SportsDetailsFragment extends Fragment {

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

    public SportsDetailsFragment() {
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
        sportCategoryNames.add("indoor"); sportCategoryNames.add("outdoor");

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");

        Iterator sportsIterator = sportCategoryNames.iterator();
        while(sportsIterator.hasNext()) {
            queue = Volley.newRequestQueue(mContext);
            String url = "https://group-10-user-api.herokuapp.com/sportcategory?sport_category="+sportsIterator.next();
            arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                for(int i=0;i<response.length();i++) {
                                    JSONObject sport = response.getJSONObject(i);

                                    String sportName = sport.getString("sport_name");
                                    String sportCategory = "Sport Category: " + sport.getString("sport_category");
                                    Integer minTeamSize = sport.getInt("mini_team_size");
                                    Integer maxTeamSize = sport.getInt("max_team_size");
                                    String gender = sport.getString("gender");
                                    String startDate = sport.getString("start_date");//.substring(1,3);
                                    String endDate = sport.getString("end_date");//.substring(4,7);
                                    if(startDate!=null&&startDate!="null"&&endDate!=null&&endDate!="null") {
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                                        Date date = inputFormat.parse(startDate);
                                        startDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
                                        date = inputFormat.parse(endDate);
                                        endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
                                    }
                                    ArrayList<SportsListDetails> details = new ArrayList<>();

                                    if (sportCategory != null) {
                                        if (gender != null) {
                                            details.add(new SportsListDetails(sportCategory + " (" + gender + ")"));
                                        } else {
                                            details.add(new SportsListDetails(sportCategory));
                                        }
                                    }
                                    if (minTeamSize != null && maxTeamSize != null) {
                                        details.add(new SportsListDetails("Team Size: " + minTeamSize + "-" + maxTeamSize));
                                    }
                                    if ((!startDate.equals("null")) && (!endDate.equals("null"))) {
                                        details.add(new SportsListDetails("Dates: " + startDate + " - " + endDate));
                                    }
                                    SportsListHeading sportsListHeading = new SportsListHeading(sportName, details);
                                    scheduledSports.add(sportsListHeading);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"error2",Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
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
            queue.add(arrayRequest);
        }
    }
}