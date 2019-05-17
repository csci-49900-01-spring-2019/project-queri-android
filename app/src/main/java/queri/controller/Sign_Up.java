package queri.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.queri.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import queri.model.AsteriskPasswordTransformationMethod;

public class Sign_Up<mAuthListener> extends AppCompatActivity {

    private EditText username, password, email, reenter;
    private FirebaseAuth mAuth;
    private Button Signup;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;
    private String TAG = "Android Sign On";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username = (EditText) findViewById(R.id.editText8);
        email = (EditText) findViewById(R.id.editText7);
        password = (EditText) findViewById(R.id.editText5);
        reenter = (EditText) findViewById(R.id.editText6);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        reenter.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        Signup = (Button) findViewById(R.id.button3);
        Signup.setOnClickListener(signListener);

    }

    private View.OnClickListener signListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String emailgiven = email.getText().toString();
            final String name = username.getText().toString();
            String pass1 = password.getText().toString();
            String pass2 = reenter.getText().toString();
            if(!checkInput(emailgiven, pass1, pass2, name)){
                Toast.makeText(Sign_Up.this, "All fields must have input.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(!pass1.equals(pass2)){
                Toast.makeText(Sign_Up.this, "Both Passwords gotta match, Stupid.",
                        Toast.LENGTH_LONG).show();
                return;
            }else{
                mAuth.createUserWithEmailAndPassword(emailgiven,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Log.d(TAG, "createUserWithEmail:success");
                            assert user != null;
                            sendVerificationEmail();
                            mDatabase.child("users").child(user.getUid()).child("user_info").child("username").setValue(name);

                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Sign_Up.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }
    };

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(Sign_Up.this, "Please Verify your email",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Sign_Up.this, LoginPage.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    private Boolean checkInput(String email, String Password, String reenter, String username){
        if(email.isEmpty() || Password.isEmpty() || reenter.isEmpty() || username.isEmpty())
            return false;
        else
            return true;
    }


}
