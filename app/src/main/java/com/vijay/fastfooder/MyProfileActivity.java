package com.vijay.fastfooder;

import static com.airbnb.lottie.utils.Logger.error;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vijay.fastfooder.Comman.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    ImageView ivProfilePicture;
    TextView tvName, tvMobileNo, tvEmailId, tvUsername;
    AppCompatButton btnEditProfile, btnSignOut;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    SharedPreferences preferences;

    String strUsername;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences (MyProfileActivity.this);
        strUsername = preferences.getString ("username", null);

        ivProfilePicture = findViewById (R.id.MyProfileProfilePhoto);
        btnEditProfile = findViewById (R.id.acbtnMyProfileEditProfile);
        tvName = findViewById (R.id.tvMyProfileName);
        tvMobileNo = findViewById (R.id.tvMyProfileMobileNo);
        tvEmailId = findViewById (R.id.tvMyProfileEmailID);
        tvUsername = findViewById (R.id.tvMyProfileUsername);
        btnSignOut = findViewById (R.id.btnSignOut);

        googleSignInOptions = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail ().build ();
        googleSignInClient = GoogleSignIn.getClient (MyProfileActivity.this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount (this);

        if (googleSignInAccount != null) {
            String name = googleSignInAccount.getDisplayName ();
            String email = googleSignInAccount.getEmail ();

            tvName.setText (name);
            tvEmailId.setText (email);
            btnSignOut.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {

                    googleSignInClient.signOut ().addOnCompleteListener (new OnCompleteListener<Void> () {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent (MyProfileActivity.this, LoginActivity.class);
                            startActivity (intent);
                            finish ();
                        }
                    });
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart ();
        progressDialog = new ProgressDialog (MyProfileActivity.this);
        progressDialog.setTitle ("My Profile");
        progressDialog.setMessage ("Please Wait....");
        progressDialog.setCanceledOnTouchOutside (true);
        progressDialog.show ();


        getMyDetails ();
    }

    private void getMyDetails() {
        AsyncHttpClient client = new AsyncHttpClient (); //client-server communication // over network data pass
        RequestParams params = new RequestParams (); // put the data in AsyncHttpClient object

        params.put ("username", strUsername);

        client.post (Urls.myDetailsWebService,params,new JsonHttpResponseHandler ()
                {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess (statusCode, headers, response);


                        try {
                            JSONArray jsonArray = response.getJSONArray ("getMyDetails");
                            for (int i=0;i<jsonArray.length ();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject (i);
                                String strid = jsonObject.getString ("id");
                                String strImage = jsonObject.getString ("image");
                                String strname = jsonObject.getString ("name");
                                String strmobileNo = jsonObject.getString ("mobile_no");
                                String stremailId = jsonObject.getString ("email_id");
                                String strusername = jsonObject.getString ("username");

                                tvName.setText (strname);
                                tvMobileNo.setText (strmobileNo);
                                tvEmailId.setText (stremailId);
                                tvUsername.setText (strusername);

                                Glide.with(MyProfileActivity.this)
                                        .load("http://192.168.185.103:80/FastFooderAPI/images/" +strImage)
                                        .skipMemoryCache(true) // Skip memory cache
                                        .error(R.drawable.image_not_available)
                                        .into(ivProfilePicture);




                            }


                        } catch (JSONException e) {
                            throw new RuntimeException (e);
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure (statusCode, headers, throwable, errorResponse);

                        progressDialog.dismiss ();
                        Toast.makeText (MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show ();
                    }
                }

        );



    }
}