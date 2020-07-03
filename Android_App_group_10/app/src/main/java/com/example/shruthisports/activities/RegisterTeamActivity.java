package com.example.shruthisports.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterTeamActivity extends AppCompatActivity {

    public LinearLayout linearLayoutParent;
    public EditText e;
    public String s1,s2;
    String sportName;
    String branch;
    Integer section;
    String gender;
    int teamSize;
    long teamId;
    String teamName;

    private int k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);

        sportName = getIntent().getStringExtra("sportName");
        branch = getIntent().getStringExtra("branch");
        section = getIntent().getIntExtra("section",1);
        gender = getIntent().getStringExtra("gender");
        teamSize = getIntent().getIntExtra("teamSize",1);
        teamId = getIntent().getLongExtra("teamId",0);
        teamName = generateTeamName();

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.linearLayoutparent);
        //this layout still needs to be vertical to hold the children.
        for (int i = 0; i < teamSize; i++) {
            LinearLayout temp = new LinearLayout(this);
            temp.setOrientation(LinearLayout.VERTICAL);

            LinearLayout temp1 = new LinearLayout(this);
            temp1.setOrientation(LinearLayout.HORIZONTAL);
            temp1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            EditText editText1 = new EditText(this);
            editText1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            editText1.setId(i*10+1);
            editText1.setHint("Player"+(i+1)+"'s name");
            temp1.addView(editText1);


            LinearLayout temp2 = new LinearLayout(this);
            temp2.setOrientation(LinearLayout.HORIZONTAL);
            temp2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            EditText editText2 = new EditText(this);
            editText2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            editText2.setId(i*10+2);
            editText2.setHint("Player"+(i+1)+"'s id");
            temp2.addView(editText2);

            temp.addView(temp1);
            temp.addView(temp2);
            rootLayout.addView(temp);
        }

        Button btn=new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        btn.setText("Register");
        btn.setTextColor(Color.WHITE);
        btn.setBackground(this.getResources().getDrawable(R.drawable.rounded_background_primary));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)btn.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(params);
        rootLayout.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func();
            }
        });
    }

    private void func() {
        final SharedPreferences userPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        for(int i=0;i<teamSize;i++){
            EditText e1=(EditText)findViewById(10*i+1);
            EditText e2=(EditText)findViewById(10*i+2);
            s1=e1.getText().toString();
            s2=e2.getText().toString();
            if(s1.equals("")||s2.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter all the details",Toast.LENGTH_LONG).show();
                return;
            }
        }
        for(int i=0;i<teamSize;i++){
            EditText e1=(EditText)findViewById(10*i+1);
            EditText e2=(EditText)findViewById(10*i+2);
            s1=e1.getText().toString();
            s2=e2.getText().toString();

            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            try {
                data.put("sport_name",sportName);
                data.put("member_ID",Long.parseLong(s2));
                data.put("team_id",teamId);
                data.put("team_name",teamName);
                data.put("member_name",s1);
                data.put("section",section.toString());
                data.put("branch",branch);
                data.put("gender",gender);
            }catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Team couldn't be registered",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"Members Successfully Registered",Toast.LENGTH_LONG).show();
            }
            String url = "https://group-10-user-api.herokuapp.com/registration";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"Members Successfully Registered",Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Members Successfully Registered",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + accessTkn);
                    return params;
                }
            };
            queue.add(objectRequest);
        }
        Intent intent = new Intent(getApplicationContext(), CaptainActivity.class);
        startActivity(intent);
    }

    private String generateTeamName() {
        int teamNameLength = branch.length()+sportName.length()+3;
        char[] teamNameArray = new char[teamNameLength];
        int j=-1;
        for(int i=0;i<branch.length();i++){
            teamNameArray[++j]=branch.charAt(i);
        }
        teamNameArray[++j]='-';
        teamNameArray[++j]='0';
        teamNameArray[j]+=section;
        for(int i=0;i<sportName.length();i++){
            teamNameArray[++j]=sportName.charAt(i);
        }
        if(gender.equals("MALE")) {
            teamNameArray[teamNameLength - 1] = 'M';
        }else{
            teamNameArray[teamNameLength - 1] = 'G';
        }

        return new String(teamNameArray);
    }
}