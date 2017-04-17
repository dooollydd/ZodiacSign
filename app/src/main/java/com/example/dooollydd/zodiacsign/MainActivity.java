package com.example.dooollydd.zodiacsign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    // for button navigation
//    private TextView mTextMessage;

    private LinearLayout profileSection;
    private Button logout;
    private SignInButton login;
    private TextView profileName;
    private TextView profileEmail;
    private ImageView profilePic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 1510;


//    // for button navigation
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_sign_in_page);

//        // for button navigation
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        // end of info for button navigation

        // main profile setting
        profileSection = (LinearLayout) findViewById(R.id.profile_section);
        logout = (Button) findViewById(R.id.logout_button);
        login = (SignInButton) findViewById(R.id.login_button);
        profileName = (TextView) findViewById(R.id.name);
        profileEmail = (TextView) findViewById(R.id.email);
        profilePic = (ImageView) findViewById(R.id.profile_picture);

        login.setOnClickListener(this);
        logout.setOnClickListener(this);

        profileSection.setVisibility(View.GONE); // hide the profile information section

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.login_button:
                login();
                break;
            case R.id.logout_button:
                logout();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    // toast
    }

    private void login(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void logout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).
                setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            //TODO: fix profile image adding
            // String imageUrl = account.getPhotoUrl().toString(); // null object

            profileName.setText(name);
            profileEmail.setText(email);
            // Glide.with(this).load(imageUrl).into(profilePic); // cant use image : null object

            updateUI(true);

        } else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLoggedIn) {

        if (isLoggedIn) {
            profileSection.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else {
            profileSection.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
