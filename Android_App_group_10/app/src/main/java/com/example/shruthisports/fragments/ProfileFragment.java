package com.example.shruthisports.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    Context mContext;
    SharedPreferences userPref;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonArrayRequest arrayRequest;
    JSONObject data;

    //UI components
    TextView userProfileName;
    TextView userProfileRoll;
    TextView userProfileMail;
    TextView userProfilePhone;
    TextView userProfileSection;
    TextView userProfileYear;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfileName = view.findViewById(R.id.user_profile_name);
        userProfileRoll = view.findViewById(R.id.user_profile_roll);
        userProfileMail = view.findViewById(R.id.user_profile_mail);
        userProfilePhone = view.findViewById(R.id.user_profile_phone);
        userProfileSection = view.findViewById(R.id.user_profile_section);
        userProfileYear = view.findViewById(R.id.user_profile_year);

        userPref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        final Long userId = userPref.getLong("userId",0);
         queue = Volley.newRequestQueue(mContext);
         String url = "https://group-10-user-api.herokuapp.com/user_info?user_id="+userId;
            arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                JSONObject user = response.getJSONObject(0);

                                Long userId = user.getLong("user_id");
                                String userName = user.getString("user_name");
                                Long phoneNum = user.getLong("phone_num");
                                String branch = user.getString("branch");
                                Integer section = user.getInt("section");
                                Integer year = user.getInt("year_");
                                String email = user.getString("email_id");

                                userProfileName.setText("Name: "+userName);
                                userProfileRoll.setText("Id: "+userId);
                                userProfileMail.setText(email+"");
                                userProfilePhone.setText(phoneNum+"");
                                userProfileSection.setText(branch.toUpperCase()+"-"+section);
                                if(year==1||year==4) {
                                    userProfileYear.setText(year+"th year");
                                }else if(year==2){
                                    userProfileYear.setText(year+"nd year");
                                }else{
                                    userProfileYear.setText(year+"rd year");
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