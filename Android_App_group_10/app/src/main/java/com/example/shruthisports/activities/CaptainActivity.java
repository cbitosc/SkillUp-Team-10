package com.example.shruthisports.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;
import com.example.shruthisports.fragments.CaptainHomeFragment;
import com.example.shruthisports.fragments.CaptainMatchFragment;
import com.example.shruthisports.fragments.CaptainRegisterTeamFragment;
import com.example.shruthisports.fragments.CaptainRegisteredSportsFragment;
import com.example.shruthisports.fragments.LogoutFragment;
import com.example.shruthisports.fragments.ProfileFragment;
import com.example.shruthisports.fragments.TeamStatusFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CaptainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout captainDrawerLayout;
    NavigationView captainNavigationView;
    TextView navDrawerUsername;
    TextView navDrawerUserid;
    Toolbar captainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain);

        initializeNavDetails();

        captainToolbar = findViewById(R.id.captain_toolbar);
        setSupportActionBar(captainToolbar);

        captainDrawerLayout = findViewById(R.id.captain_drawer_layout);
        captainNavigationView = findViewById(R.id.captain_nav_view);
        captainNavigationView.setNavigationItemSelectedListener(this);

        View headerView = captainNavigationView.getHeaderView(0);
        navDrawerUsername = headerView.findViewById(R.id.nav_drawer_username);
        navDrawerUserid = headerView.findViewById(R.id.nav_drawer_userid);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, captainDrawerLayout, captainToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        captainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                    new CaptainHomeFragment()).commit();
            captainToolbar.setTitle("Shruthi Sports");
            captainNavigationView.setCheckedItem(R.id.captain_nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if(captainDrawerLayout.isDrawerOpen(GravityCompat.START)){
            captainDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(getCheckedItem(captainNavigationView)==0){
            return;
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                    new CaptainHomeFragment()).commit();
            captainToolbar.setTitle("Shruthi Sports");
            captainNavigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.captain_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new CaptainHomeFragment()).commit();
                captainToolbar.setTitle("Shruthi Sports");
                break;
            case R.id.captain_nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new CaptainRegisterTeamFragment()).commit();
                captainToolbar.setTitle("Register Team");
                break;
            case R.id.captain_nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new ProfileFragment()).commit();
                captainToolbar.setTitle("Captain Profile");
                break;
            case R.id.captain_nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new LogoutFragment()).commit();
                captainToolbar.setTitle("Logout");
                break;
            case R.id.captain_nav_schedules:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new CaptainMatchFragment()).commit();
                captainToolbar.setTitle("Match Schedules");
                break;
            case R.id.captain_nav_registered:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new CaptainRegisteredSportsFragment()).commit();
                captainToolbar.setTitle("Registered Sports");
                break;
            case R.id.captain_nav_status:
                getSupportFragmentManager().beginTransaction().replace(R.id.captain_fragment_container,
                        new TeamStatusFragment()).commit();
                captainToolbar.setTitle("Registration Status");
                break;
        }
        captainDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return i;
            }
        }
        return -1;
    }

    private void initializeNavDetails() {
        final SharedPreferences userPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        final Long userId = userPref.getLong("userId",0);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://group-10-user-api.herokuapp.com/user_info?user_id="+userId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject user = response.getJSONObject(0);
                            String userName = user.getString("user_name");
                            SharedPreferences.Editor uEditor = userPref.edit();
                            uEditor.putString("userName",userName);
                            navDrawerUsername.setText(userName);
                            navDrawerUserid.setText(userId+"");
                            uEditor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error2",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }}, new Response.ErrorListener() {
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
