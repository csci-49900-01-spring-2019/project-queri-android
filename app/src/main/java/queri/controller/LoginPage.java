package queri.controller;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    public Button sign_uper;
    public Button Loginer;
    public EditText email;
    public EditText password;
    private FirebaseAuth mAuth;
    private String TAG = "Android Login";
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        sign_uper = (Button)findViewById(R.id.button3);
        Loginer = (Button) findViewById(R.id.button2);
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Loginer.setOnClickListener(logListener);
        sign_uper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginPage.this, Sign_Up.class);
                LoginPage.this.startActivity(myIntent);
            }
        });
    }
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(LoginPage.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(LoginPage.this, MainActivity.class);
            LoginPage.this.startActivity(mainIntent);
        }
        else
        {
            Toast.makeText(LoginPage.this, "Please verify your email",
                    Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    private View.OnClickListener logListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!checkInput(email.getText().toString(), password.getText().toString())){
                Toast.makeText(LoginPage.this, "All fields must have input.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                checkIfEmailVerified();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    };

    private Boolean checkInput(String email, String Password){
        if(email.isEmpty() || Password.isEmpty())
            return false;
        else
            return true;
    }
}
