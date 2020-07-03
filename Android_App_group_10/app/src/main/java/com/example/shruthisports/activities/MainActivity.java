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
import com.example.shruthisports.fragments.HomeFragment;
import com.example.shruthisports.fragments.LoginAsCaptainFragment;
import com.example.shruthisports.fragments.LogoutFragment;
import com.example.shruthisports.fragments.ProfileFragment;
import com.example.shruthisports.fragments.ScheduleFragment;
import com.example.shruthisports.fragments.SettingsFragment;
import com.example.shruthisports.fragments.SportsDetailsFragment;
import com.example.shruthisports.fragments.UserTeamRegisterFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView navDrawerUsername;
    TextView navDrawerUserid;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNavDetails();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navDrawerUsername = headerView.findViewById(R.id.nav_drawer_username);
        navDrawerUserid = headerView.findViewById(R.id.nav_drawer_userid);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            toolbar.setTitle("Shruthi Sports");
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(getCheckedItem(navigationView)==0){
            return;
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            toolbar.setTitle("Shruthi Sports");
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                toolbar.setTitle("Shruthi Sports");
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                toolbar.setTitle("User Profile");
                break;
            case R.id.nav_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScheduleFragment()).commit();
                toolbar.setTitle("Sports Schedules");
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                toolbar.setTitle("Settings");
                break;
            case R.id.nav_details:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SportsDetailsFragment()).commit();
                toolbar.setTitle("Sports Details");
                break;
            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserTeamRegisterFragment()).commit();
                toolbar.setTitle("Register Team");
                break;
            case R.id.nav_captain_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LoginAsCaptainFragment()).commit();
                toolbar.setTitle("Login as Captain");
                break;
            case R.id.nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LogoutFragment()).commit();
                toolbar.setTitle("Logout");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
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
                            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
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
