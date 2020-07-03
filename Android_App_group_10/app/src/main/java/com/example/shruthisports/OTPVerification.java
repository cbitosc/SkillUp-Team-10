package com.example.shruthisports;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shruthisports.fragments.RegisterFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.shruthisports.fragments.RegisterFragment.register_data;

public class OTPVerification extends Dialog implements TextWatcher {

    EditText editText_one;
    EditText editText_two;
    EditText editText_three;
    EditText editText_four;
    Button otp_submit;
    TextView otpDialopTextView;
    String OTP = RegisterFragment.OTP;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    public static boolean Verified = false;

    public OTPVerification(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verify);

        otpDialopTextView = findViewById(R.id.otp_dialog_textView);
        otpDialopTextView.setText("Enter OTP:");
        otp_submit = findViewById(R.id.otp_submit);
        editText_one = findViewById(R.id.editTextone);
        editText_two = findViewById(R.id.editTexttwo);
        editText_three = findViewById(R.id.editTextthree);
        editText_four = findViewById(R.id.editTextfour);
        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);

        otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { verifyOTP();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
        public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (editText_one.length() == 1) {
                editText_two.requestFocus();
            }

            if (editText_two.length() == 1) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 1) {
                editText_four.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (editText_four.length() == 0) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 0) {
                editText_two.requestFocus();
            }
            if (editText_two.length() == 0) {
                editText_one.requestFocus();
            }
        }
    }
    public void verifyOTP(){
        char[] userOtp = new char[4];
        userOtp[0]=editText_one.getText().charAt(0);
        userOtp[1]=editText_two.getText().charAt(0);
        userOtp[2]=editText_three.getText().charAt(0);
        userOtp[3]=editText_four.getText().charAt(0);
        String userOTP = new String(userOtp);
        if(OTP.equals(userOTP)){
            Verified = true;
            Toast.makeText(getContext(),"OTP verified",Toast.LENGTH_LONG).show();
            String url="https://group-10-user-api.herokuapp.com/User_reg";
            queue = Volley.newRequestQueue(getContext());
            objectRequest = new JsonObjectRequest(Request.Method.POST, url, register_data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String accessTkn = null;
                            try {
                                accessTkn = response.getString("access_token");
                                Toast.makeText(getContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"Verification failed try again",Toast.LENGTH_LONG).show();
                }
            });
            queue.add(objectRequest);
            Toast.makeText(getContext(),"registered successfully",Toast.LENGTH_LONG).show();
            this.dismiss();
        }else{
            Toast.makeText(getContext(),"Wrong OTP please try again later",Toast.LENGTH_LONG).show();
        }
    }
}