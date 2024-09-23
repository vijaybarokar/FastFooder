package com.vijay.fastfooder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vijay.fastfooder.Comman.NetworkChangeListener;
import com.vijay.fastfooder.Comman.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    CheckBox cbShowHidePassword;
    Button btnLogin;
    TextView tvSignUp, tvForgotPassword;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    LottieAnimationView lottieAnimationView;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener ();

    GoogleSignInOptions googleSignInOptions; // show option of gmail
    GoogleSignInClient googleSignInClient; // stores selected gmail
    // GoogleSignInOption => GoogleSignIn => GoogleSignInClient => GoogleSignAccount (name,email,photo)
    AppCompatButton btnSignInWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        setTitle ("Login ");

        preferences = PreferenceManager.getDefaultSharedPreferences (LoginActivity.this);
        editor = preferences.edit ();

//        preferences = PreferenceManager.getDefaultSharedPreferences (LoginActivity.this);
//        editor = preferences.edit ();

        if(preferences.getBoolean("isLogin", false))
        {
            Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
           startActivity(i);

        }
        etUsername = findViewById (R.id.etLoginUsername);
        etPassword = findViewById (R.id.etLoginPassword);
        tvForgotPassword = findViewById (R.id.tvLoginForgetPassword);
        cbShowHidePassword = findViewById (R.id.cbLoginShowHidePassword);
        btnLogin = findViewById (R.id.btnLoginLogin);
        tvSignUp = findViewById (R.id.tvLoginSignUp);
        btnSignInWithGoogle = findViewById (R.id.acbtnLoginSignInWithGoogle);

        googleSignInOptions = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail ().build ();
        googleSignInClient = GoogleSignIn.getClient (LoginActivity.this,googleSignInOptions);

        btnSignInWithGoogle.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                signIn();
            }
        }
        );


        cbShowHidePassword.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean
                    isChecked) {
                if (isChecked) {

                    etPassword.setTransformationMethod (HideReturnsTransformationMethod.getInstance ());
                                                                                                                    } else {
                    etPassword.setTransformationMethod (PasswordTransformationMethod.getInstance ()
                    );
                }
            }
        });
        btnLogin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (etUsername.getText ().toString ().isEmpty ()) {
                    etUsername.setError ("Please Enter Username");
                } else if (etPassword.getText ().toString ().isEmpty ()) {
                    etPassword.setError ("Please Enter Password");
                } else if (etUsername.getText ().toString ().length () < 8) {
                    etUsername.setError ("Username must contain 8 Characters");
                } else if (etPassword.getText ().toString ().length () < 8) {
                    etPassword.setError ("Password must contain 8 Characters");
                } else if (!etUsername.getText ().toString ().matches (".*[A-Z].*")) {
                    etUsername.setError ("Username must contain a Uppercase Character");
                } else if (!etUsername.getText ().toString ().matches (".*[a-z].*")) {
                    etUsername.setError ("Username must contain a Lowercase Character");
                } else if (!etUsername.getText ().toString ().matches (".*[0-9].*")) {
                    etUsername.setError ("Username must contain a Number");
                } else if (!etUsername.getText ().toString ().matches (".*[@,#,$,&].*")) {
                    etUsername.setError ("Username must contain a Special Character");
                } else {
                    

                    progressDialog = new ProgressDialog (LoginActivity.this);
                    progressDialog.setTitle ("Login Under Progress");
                    progressDialog.setMessage ("Please Wait a Moment");
                    progressDialog.show ();
                    userLogin();


//                    Intent i = new Intent (LoginActivity.this,HomeActivity.class);
//                    editor.putString ("username", etUsername.getText ().toString ()).commit ();
//                    editor.putBoolean ("isLogin", true).commit ();
//                    startActivity (i);
                }
            }

        });


        tvForgotPassword.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, ConfirmRegisterMobileNoActivity.class);
                startActivity (intent);
            }
        });


        tvSignUp.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (LoginActivity.this,RegistrationActivity.class);
                startActivity (i);
            }
        });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent ();
        startActivityForResult (intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == 999)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent (data);
            try {
                task.getResult (ApiException.class);
                Intent intent = new Intent (LoginActivity.this,MyProfileActivity.class);
                startActivity (intent);
                finish ();
            } catch (ApiException e) {
                Toast.makeText (LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show ();
                    }
        }
    }

    //     onCreate() = initialization
//     onStart() =
//    onResume() =
//    onPause() =
//    onStop() =
//    onDestroy() =


    @Override
    protected void onStart() {
        super.onStart ();

        IntentFilter intentFilter = new IntentFilter (ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver (networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop ();
        unregisterReceiver (networkChangeListener);
    }
    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient ();// client  and server communication // over network data transfer
        RequestParams params = new RequestParams ();// data put

        params.put ("username",etUsername.getText ().toString ());
        params.put ("password",etPassword.getText ().toString ());

        client.post(Urls.loginUserWebService,params,
                new JsonHttpResponseHandler () {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess (statusCode, headers, response);

                        progressDialog.dismiss();
                        try {
                            String status = response.getString ("success");
                            if (status.equals("1"))
                            {
                                Intent intent = new Intent (LoginActivity.this,HomeActivity.class);
                                editor.putString ("username",etUsername.getText ().toString ()).commit ();
                                startActivity (intent);
                                finish();
                            }
                            else{
                                Toast.makeText (LoginActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException (e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure (statusCode, headers, throwable, errorResponse);
                    progressDialog.dismiss ();
                        Toast.makeText (LoginActivity.this,"Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}



