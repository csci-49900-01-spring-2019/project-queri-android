package queri.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.capstone.queri.R;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeaturedFragment())
                .commit();
    }
    public void replies(@Nullable Bundle bundle)
    {
        Fragment replyFragment = new RepliesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, replyFragment)
                .commit();
        replyFragment.setArguments(bundle);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                     switch (menuItem.getItemId()){
                         case R.id.navigation_home:
                             selectedFragment = new FeaturedFragment();
                             break;

                         case R.id.navigation_dashboard:
                             selectedFragment = new ArchivedFragment();
                             break;

                         case R.id.navigation_notifications:
                             selectedFragment = new VotingFragment();


                     }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                     return true;
                }
            };
}
