package com.example.amalv.oneblood1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.handle;
import static junit.runner.Version.id;

public class SignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference  userdb= mRootRef.child("users");
    AppCompatButton _signup;
    EditText _password,_email,_name;
    Intent homeIntent;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth,mAuthGoogle;
    FirebaseUser user;
    public static final String TAG="SignupActivity";
    SharedPreferences sharedPreferences;
   int flag=0,flag1=0,flag2=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedPreferences= getSharedPreferences("mypref", 0);
        if(sharedPreferences.contains("LoggedIn")) {
            if (sharedPreferences.getBoolean("LoggedIn", false)) {
                Log.d(TAG, "USER LOGGED IN");
                homeIntent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }
        else{
            Log.d(TAG,"USER NOT LOGGED IN");
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        _name=(EditText) findViewById(R.id.input_name);
        _email=(EditText) findViewById(R.id.input_email);
        _password=(EditText) findViewById(R.id.input_password);
        _signup=(AppCompatButton)findViewById(R.id.btn_signup);

        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_name.getText().toString().length() == 0) {
                    _name.setError("Please Enter Name");
                } else {
                    flag = 1;
                }
                if (_email.getText().toString().length() == 0) {
                    _email.setError("Please Enter Email");
                } else {
                    flag1 = 1;
                }
                if (_password.getText().toString().length() < 6) {
                    _password.setError("Password must be atleast 6 characters long");
                } else {
                    flag2 = 1;
                }
                if (flag == 1 && flag1 == 1 && flag2==1) {
                    createAccount(_email.getText().toString(), _password.getText().toString());
                    Log.d(TAG, "Create account clicked");
                }
            }
        });
        mAuth=FirebaseAuth.getInstance();
        mAuthGoogle=FirebaseAuth.getInstance();
        //updateUI(currentUser);
        AddData();



    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);


        // showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Log.d(TAG,"Account created"+user.getEmail());
                            UserProfileChangeRequest change=new UserProfileChangeRequest.Builder()
                                    .setDisplayName(_name.getText().toString())
                                    .build();
                            user.updateProfile(change)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                Log.d(TAG,user.getDisplayName());
                                                DatabaseReference uid=userdb.child(user.getUid());
                                                Map<String, Object> users = new HashMap<String, Object>();
                                                users.put("name",user.getDisplayName());
                                                users.put("email",user.getEmail());
                                                users.put("Points",0);
                                                users.put("donationdone",0);
                                                        uid.updateChildren(users);
                                                sharedPreferences= getSharedPreferences("mypref", 0);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("LoggedIn", true);
                                                editor.putString("Userid",user.getUid());
                                                editor.commit();
                                                Log.d(TAG,"Created user"+user.getDisplayName());
                                                Intent processingIntent = new Intent(SignupActivity.this,HomeActivity.class);
                                                processingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(processingIntent);

                                                        finish();

                                           }
                                            else{
                                                Log.d(TAG,"FAILED");
                                            }
                                        }



                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if(requestCode==RC_SIGN_IN){
        GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            // Google Sign In failed, update UI appropriately
            // [START_EXCLUDE]
            Log.d(TAG,"NO GOOGLE ACCOUNT");
            // [END_EXCLUDE]
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,"Google User Logged in"+user.getDisplayName());
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    public void handleResult(GoogleSignInResult result){
        Log.d(TAG,"HandleResult"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct=result.getSignInAccount();
            Log.d(TAG,"HEllo"+acct.getDisplayName());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Onconnectionfailed"+connectionResult);
    }

    public  void AddData() {

    }
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("mypref", 0);
        if (sharedPreferences.contains("LoggedIn")) {
            if (sharedPreferences.getBoolean("LoggedIn", false)) {
                Log.d(TAG, "USER LOGGED IN");
                homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
            }
        } else {
            Log.d(TAG, "USER NOT LOGGED IN");
        }
    }
}
