package com.vijay.fastfooder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etMobileNo, etEmailId,etUsername,etPassword;

    Button btnRegister;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_registration);

        setTitle("Register Here");

        preferences = PreferenceManager.getDefaultSharedPreferences (RegistrationActivity.this);
        editor = preferences.edit ();

        etName = findViewById(R.id.etRegisterName);
        etMobileNo = findViewById(R.id.etRegisterMobileNo);
        etEmailId = findViewById(R.id.etRegisterEmailId);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);

        btnRegister = findViewById(R.id.btnRegisterRegister);

        btnRegister.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (etName.getText ().toString ().isEmpty ())
                {
                    etName.setError ("Please Enter Your Name");
                } else if (etMobileNo.getText ().toString ().isEmpty ())
                {
                    etMobileNo.setError ("Please Enter Your Mobile Number");
                } else if (etMobileNo.getText ().toString ().length ()!=10)
                {
                    etMobileNo.setError ("Mobile Number must contain 10  digits");
                } else if (etEmailId.getText ().toString ().isEmpty ())
                {
                    etEmailId.setError ("Please Enter Your E-mail ID");
                } else if (!etEmailId.getText ().toString ().contains ("@")||!etEmailId.getText ().toString ().contains (".com")) {
                    etEmailId.setError ("Please Enter valid E-mail ID");
                } else if (etUsername.getText ().toString ().isEmpty ()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etUsername.getText ().toString ().length ()<8) {
                    etUsername.setError ("Username must contain 8 characters");
                } else if (etPassword.getText ().toString ().isEmpty ()) {
                    etPassword.setError ("Please Enter Your Password");
                } else if (etPassword.getText ().toString ().length()<8) {
                    etPassword.setError("Password must contain 8 characters");

                }else if (!etUsername.getText().toString().matches (".*[A-Z].*")) {
                    etUsername.setError("Username must contain a Uppercase Character");
                }else if (!etUsername.getText().toString ().matches (".*[a-z].*")){
                    etUsername.setError("Username must contain a Lowercase Character");
                }else if (!etUsername.getText().toString ().matches (".*[0-9].*")){
                    etUsername.setError("Username must contain a Number");
                }else if (!etUsername.getText().toString ().matches(".*[@,#,$,&].*")){
                    etUsername.setError("Username must contain a Special Character");
                }else{

                    progressDialog = new ProgressDialog (RegistrationActivity.this);
                    progressDialog.setTitle ("Please Wait a Moment.....");
                    progressDialog.setMessage ("Login Under Progress");
                    progressDialog.setCanceledOnTouchOutside (true);
                    progressDialog.show ();
             // Verify Mobile No
                    PhoneAuthProvider.getInstance().verifyPhoneNumber (
                            "+91" + etMobileNo.getText ().toString (), 60, TimeUnit.SECONDS, RegistrationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss ();
                                    Toast.makeText (RegistrationActivity.this,"Registration Susscessful" , Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    progressDialog.dismiss ();
                                    Toast.makeText (RegistrationActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    Intent intent = new Intent (RegistrationActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra ("verificationCode", verificationCode); // key => string, value => string
                                    intent.putExtra ("name",etName.getText().toString());
                                    intent.putExtra ("mobileno",etMobileNo.getText().toString());
                                    intent.putExtra ("emailid",etEmailId.getText().toString());
                                    intent.putExtra ("username",etUsername.getText().toString());
                                    intent.putExtra ("password",etPassword.getText().toString());
                                    startActivity (intent);
                                }
                            }
                    );
                }
            }
        });
    }
}

