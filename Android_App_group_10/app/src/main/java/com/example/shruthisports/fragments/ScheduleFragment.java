package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.shruthisports.activities.MatchSchedulesActivity;
import com.example.shruthisports.R;
import com.example.shruthisports.adapters.SportsAdapter;
import com.example.shruthisports.classes.Sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScheduleFragment extends Fragment {

    ListView listView;
    ArrayList<Sports> sportsList = new ArrayList<>();
    ArrayList<String> sportCategoryNames = new ArrayList<String>();
    Context mContext;
    SharedPreferences userPref;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;

    public ScheduleFragment() {
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
        func();

        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, MatchSchedulesActivity.class);
                intent.putExtra("sport_name",sportsList.get(position).getSport_name());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    public void func(){

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

                                    Integer sportId = sport.getInt("sport_id");
                                    String sportName = sport.getString("sport_name");
                                    String sportCategory = sport.getString("sport_category");
                                    Integer minTeamSize = sport.getInt("mini_team_size");
                                    Integer maxTeamSize = sport.getInt("max_team_size");
                                    String gender = sport.getString("gender");
                                    String startDate = sport.getString("start_date");
                                    String endDate = sport.getString("end_date");
                                    sportsList.add(new Sports(sportId,sportName,sportCategory,minTeamSize,maxTeamSize,
                                            gender,startDate,endDate));
                                }
                                if(sportsList.isEmpty()){
                                    Toast.makeText(mContext,"No items in sports", Toast.LENGTH_LONG).show();
                                }else {
                                    SportsAdapter adapter = new SportsAdapter(mContext, R.layout.sports, sportsList);
                                    listView.setAdapter(adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"error2",Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
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