package Queri.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.queri.R;

public class LoginPage extends AppCompatActivity {

    public static Button sign_uper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        sign_uper = (Button)findViewById(R.id.button3);
        sign_uper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginPage.this, Sign_Up.class);
                LoginPage.this.startActivity(myIntent);
            }
        });
    }


}
