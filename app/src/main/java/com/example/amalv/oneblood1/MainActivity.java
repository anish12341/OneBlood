package com.example.amalv.oneblood1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN=9001;
    public static final String TAG="MainActivity";

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  userdb= mRootRef.child("users");

    SignInButton _signin;

    GoogleApiClient mGoogleApiClient;

    Intent loginIntent,signIntent,homeIntent;

    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;

    GoogleSignInOptions gso;

    AuthCredential credential;

    AppCompatButton _login,_signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        signIntent = new Intent(getApplicationContext(), SignupActivity.class);

        sharedPreferences = getSharedPreferences("mypref", 0);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        _login = (AppCompatButton) findViewById(R.id.log);

        _signup = (AppCompatButton) findViewById(R.id.sign);




        _signin = (SignInButton) findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);

            }
        });

        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signIntent);


            }
        });


        _signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //-------------------------------------------------------------------------------


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.d(TAG,"result-----"+result.isSuccess());
              if(result.isSuccess()) {

                  GoogleSignInAccount account = result.getSignInAccount();
                  firebaseAuthWithGoogle(account);
                  Log.d(TAG, "google done");
              }
              else{
                  Log.d(TAG,"Not doen");
              }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,"Google User Logged in"+user.getDisplayName());
                            DatabaseReference uid=userdb.child(user.getUid());
                            Map<String, Object> users = new HashMap<String, Object>();
                            Log.d(TAG,"hjhj");
                            users.put("name",user.getDisplayName());
                            users.put("email",user.getEmail());
                            users.put("points",0);
                            uid.updateChildren(users);
                            //updateUI(user);
                            Log.d(TAG,user.getEmail());
                            sharedPreferences= getSharedPreferences("mypref", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("LoggedIn", true);
                            editor.putString("Userid",user.getUid());
                            editor.apply();
                            Intent HomeIntent = new Intent(MainActivity.this,HomeActivity.class);
                            //processingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(HomeIntent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences= getSharedPreferences("mypref", 0);
        if(sharedPreferences.contains("LoggedIn")) {
            if (sharedPreferences.getBoolean("LoggedIn", false)) {
                Log.d(TAG, "USER LOGGED IN");
                homeIntent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
            }
        }
        else{
            Log.d(TAG,"USER NOT LOGGED IN");
        }
      /*  DatabaseReference condition=nn.child("Condition");
        condition.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.getValue(String.class);
                Log.i("temp", temp);
            }
            public void onCancelled(DatabaseError dberror){

            }
        });
*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Onconnectionfailed"+connectionResult);
    }

}

