package com.vijay.fastfooder;

import static com.vijay.fastfooder.R.*;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vijay.fastfooder.Comman.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {

    TextView tvMobileNo, tvResentOTP;
    EditText etInputCode1, etInputCode2, etInputCode3, etInputCode4, etInputCode5, etInputCode6;
    AppCompatButton btnVerify;

    ProgressDialog progressDialog;

    private String strVerificationCode, strName, strMobileNo,strEmailId,strUsername,strPassword;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_verify_otp);

        tvMobileNo = findViewById (R.id.tvVerifyOTPMobileno);
        tvResentOTP = findViewById (R.id.tvVerifyOTPResentOTP);
        etInputCode1 = findViewById (R.id.etVerifyOPTInputCode1);
        etInputCode2 = findViewById (R.id.etVerifyOPTInputCode2);
        etInputCode3 = findViewById (R.id.etVerifyOPTInputCode3);
        etInputCode4 = findViewById (R.id.etVerifyOPTInputCode4);
        etInputCode5 = findViewById (R.id.etVerifyOPTInputCode5);
        etInputCode6 = findViewById (R.id.etVerifyOPTInputCode6);
        btnVerify = findViewById (id.btnVerifyOTPVerify);

        strVerificationCode = getIntent ().getStringExtra ("verificationCode");
        strName = getIntent ().getStringExtra ("name");
        strMobileNo = getIntent ().getStringExtra ("mobileno");
        strEmailId = getIntent ().getStringExtra ("emailid");
        strUsername = getIntent ().getStringExtra ("username");
        strPassword = getIntent ().getStringExtra ("password");

        btnVerify.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                        if(etInputCode1.getText ().toString ().trim ().isEmpty()
                        || etInputCode2.getText ().toString ().trim ().isEmpty()
                        || etInputCode3.getText ().toString ().trim ().isEmpty()
                        || etInputCode4.getText ().toString ().trim ().isEmpty()
                        || etInputCode5.getText ().toString ().trim ().isEmpty()
                        || etInputCode6.getText ().toString ().trim ().isEmpty()){


                            Toast.makeText (VerifyOTPActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show ();
                        }


                        String otpCode = etInputCode1.getText ().toString ()
                                + etInputCode2.getText ().toString ()
                                + etInputCode3.getText ().toString ()
                                + etInputCode4.getText ().toString ()
                                + etInputCode5.getText ().toString ()
                                + etInputCode6.getText ().toString ();

                        if(strVerificationCode!=null)
                        {
                            progressDialog = new ProgressDialog (VerifyOTPActivity.this);
                            progressDialog.setTitle ("Verifying OTP...");
                            progressDialog.setMessage ("Please wait...");
                            progressDialog.setCanceledOnTouchOutside (false);
                            progressDialog.show ();

                            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential (strVerificationCode, otpCode);

                            FirebaseAuth.getInstance ().signInWithCredential (phoneAuthCredential)
                                    .addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful ())
                                            {
                                                progressDialog.dismiss ();
                                                userRegisterDetails();
                                            }
                                            else{
                                                progressDialog.dismiss ();
                                                Toast.makeText (VerifyOTPActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show ();
                                            }
                                        }
                                    });

                        }

            }
        });

        tvResentOTP.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber (
                        "+91" + strMobileNo, 60, TimeUnit.SECONDS, VerifyOTPActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText (VerifyOTPActivity.this,"Registration Successful" , Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText (VerifyOTPActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationCode, @NonNull
                            PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                strVerificationCode = newVerificationCode;

                            }
                        }
                );


            }
        });


        setupInputOTP();


    }

    private void userRegisterDetails() {
        {
// client  and server communication // over network data transfer or manipulater
            AsyncHttpClient client = new AsyncHttpClient ();
            RequestParams params = new RequestParams (); //put the data
            params.put("name", strName);
            params.put("mobileno",strMobileNo);
            params.put("emailid",strEmailId);
            params.put("username", strUsername);
            params.put("password", strPassword);

            client.post(Urls.registerUserWebService, params,
                    new JsonHttpResponseHandler () {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            progressDialog.dismiss();

                            try {
                                String status = response.getString("Success");
                                if (status.equals("1")) {
                                    Toast.makeText(VerifyOTPActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                } else
                                {
                                    Toast.makeText(VerifyOTPActivity.this, "Data Already Present", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }


                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure (statusCode, headers, throwable, errorResponse);
                            progressDialog.dismiss ();

                            super.onFailure (statusCode, headers, throwable, errorResponse);
                            Toast.makeText (VerifyOTPActivity.this,"Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private void setupInputOTP() {
        etInputCode1.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString ().trim ().isEmpty()) {
                    etInputCode2.requestFocus ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        etInputCode2.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString ().trim ().isEmpty()) {
                    etInputCode3.requestFocus ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });




        etInputCode3.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString ().trim ().isEmpty()) {
                    etInputCode4.requestFocus ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        etInputCode4.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString ().trim ().isEmpty()) {
                    etInputCode5.requestFocus ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        etInputCode5.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString ().trim ().isEmpty()) {
                    etInputCode6.requestFocus ();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }
}