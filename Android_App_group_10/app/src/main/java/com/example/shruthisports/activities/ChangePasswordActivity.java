package com.example.shruthisports.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPasswordET;
    EditText newPasswordET;
    Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordET = findViewById(R.id.oldPasswordET);
        newPasswordET = findViewById(R.id.newPasswordET);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        final SharedPreferences userPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String accessTkn = userPref.getString("access_token","");
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject data = new JSONObject();
        try {
            data.put("password_",oldPasswordET.getText().toString());
            data.put("new_password",newPasswordET.getText().toString());
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Password couldn't be changed",Toast.LENGTH_SHORT).show();
        }
        String url = "https://group-10-user-api.herokuapp.com/change_password";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        oldPasswordET.setText("");
                        newPasswordET.setText("");
                        Toast.makeText(getApplicationContext(),"Password Successfully Changed",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Password couldn't be changed",Toast.LENGTH_LONG).show();
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
}