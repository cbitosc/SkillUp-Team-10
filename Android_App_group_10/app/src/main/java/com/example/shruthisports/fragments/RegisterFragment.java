package com.example.shruthisports.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shruthisports.R;
import com.example.shruthisports.SendMail;
import com.example.shruthisports.OTPVerification;

import org.json.JSONObject;

import java.util.Random;

public class RegisterFragment extends Fragment {

    //creating variables for ui components
    EditText registerIdET;
    EditText registerNameET;
    EditText registerPhoneET;
    EditText registerMailET;
    EditText registerPasswordET;
    Spinner yearSpinner;
    Spinner branchSpinner;
    Spinner sectionSpinner;
    Button registerBtn;

    //creating objects required to interact with REST api
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    public static JSONObject register_data;
    Context mContext;

    public static String OTP="1234";

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing ui variables with corresponding components
        registerIdET = view.findViewById(R.id.registerIdET);
        registerNameET = view.findViewById(R.id.registerNameET);
        registerPhoneET = view.findViewById(R.id.registerPhoneET);
        registerMailET = view.findViewById(R.id.registerMailET);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        branchSpinner = view.findViewById(R.id.branchSpinner);
        registerPasswordET = view.findViewById(R.id.registerPasswordET);
        sectionSpinner = view.findViewById(R.id.sectionSpinner);
        registerBtn = view.findViewById(R.id.registerBtn);

        //Intializing JSON object
        register_data = new JSONObject();

        //setting onClickListener to login Button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { register();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void register() {
        try {
            Long userId = Long.parseLong(registerIdET.getText().toString());
            String userName = registerNameET.getText().toString();
            String password = registerPasswordET.getText().toString();
            Long userPhone = Long.parseLong(registerPhoneET.getText().toString());
            String userMail = registerMailET.getText().toString();
            Integer userYear = (int)(yearSpinner.getSelectedItem().toString().charAt(0)-'0');
            String userBranch = branchSpinner.getSelectedItem().toString();
            Integer userSection = Integer.parseInt(sectionSpinner.getSelectedItem().toString());
            register_data.put("user_id",userId);
            register_data.put("user_name",userName);
            register_data.put("password_",password);
            register_data.put("phone_num",userPhone);
            register_data.put("email_id",userMail);
            register_data.put("year_",userYear);
            register_data.put("branch",userBranch);
            register_data.put("section",userSection);
            registerIdET.setText("");
            registerNameET.setText("");
            registerPasswordET.setText("");
            registerPhoneET.setText("");
            registerMailET.setText("");

            if(checkDetails(userId, userName, userPhone, userMail, password)&&matchDetails(userId,userMail,userBranch)){
                OTPVerify(userMail);
            }
        }catch (Exception e){
            Toast.makeText(mContext,"Please enter valid details",Toast.LENGTH_LONG).show();
        }
    }

    private boolean matchDetails(Long userId, String userMail,String userBranch) {
        String Id = userId.toString();
        if(!Id.substring(4,6).equals(userMail.substring(3,5))){
            Toast.makeText(mContext,"user ID and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else if(!Id.substring(9,12).equals(userMail.substring(5,8))){
            Toast.makeText(mContext,"user ID and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else if(!userMail.substring(9,9+userBranch.length()).equals(userBranch.toLowerCase())){
            Toast.makeText(mContext,"user branch and mail doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean checkDetails(Long userId, String userName, Long userPhone, String userMail, String password) {
        if((userId/100000000)!=1601){
            Toast.makeText(mContext,"enter valid user Id",Toast.LENGTH_LONG).show();
            return false;
        }else if(userName==""){
            Toast.makeText(mContext,"Please enter a valid name",Toast.LENGTH_LONG).show();
            return false;
        }else if(!((userPhone/(long)(1e9))>0)&&((userPhone/(long)(1e10))==0)){
            Toast.makeText(mContext,"Please enter a valid Phone number",Toast.LENGTH_LONG).show();
            return false;
        }else if(!(userMail!=""&&userMail.length()>24)){
            Toast.makeText(mContext,"Please enter a valid mail",Toast.LENGTH_LONG).show();
            return false;
        }else if(password.length()<8){
            Toast.makeText(mContext,"Please enter a password of minimum 8 characters",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public boolean OTPVerify(String userMail){
        //generating OTP
        String numbers = "0123456789";
        char[] otp = new char[4];
        Random rndm_method = new Random();
        for (int i = 0; i < 4; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        OTP = new String(otp);

        //sending email
        String email = userMail;
        String subject = "OTP for Shruthi Sports";
        String message = "Your OTP for registration is : "+OTP;
        SendMail sm = new SendMail(mContext, email, subject, message);
        sm.execute();

        OTPVerification otpVerification = new OTPVerification(mContext);
        otpVerification.show();

        if(OTPVerification.Verified){
            return true;
        }
        return false;
    }
}
