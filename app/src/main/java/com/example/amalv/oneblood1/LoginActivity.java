package com.example.amalv.oneblood1;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amalv.oneblood1.R;
import com.example.amalv.oneblood1.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;
    Intent homeIntent;
    SharedPreferences sharedPreferences;

    EditText _emailText;
   EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
   int flag=0,flag1=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        _emailText=(EditText)findViewById(R.id.input_email);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _loginButton=(Button)findViewById(R.id.btn_login);
        _signupLink=(TextView)findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    if (_emailText.getText().toString().length() == 0) {
                        _emailText.setError("Please Enter Email");
                    }
                    else {
                        flag=1;
                    }
                    if (_passwordText.getText().toString().length() < 6) {
                        _passwordText.setError("Password must be atleast 6 characters long");
                    }
                    else{
                        flag1=1;
                    }
                if(flag==1&&flag1==1) {
                    signIn(_emailText.getText().toString(), _passwordText.getText().toString());
                    Log.d(TAG, "Login clicked");
                }
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        mAuth=FirebaseAuth.getInstance();
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);




        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Log.d(TAG,user.getEmail());
                            sharedPreferences= getSharedPreferences("mypref", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("LoggedIn", true);
                            editor.putString("Userid",user.getUid());
                            editor.commit();
                            Intent HomeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                            //processingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(HomeIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            Log.d(TAG,"signinfailed");
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            // mStatusTextView.setText(R.string.auth_failed);
                            Log.d(TAG,"signinfailed 2");
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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
