package top.smartdone.si.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import top.smartdone.si.R;

public class SIMainActivity extends AppCompatActivity {

    private LinearLayout ll_home;
    private LinearLayout ll_dash;
    private LinearLayout ll_notify;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideall();
                    ll_home.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    hideall();
                    ll_dash.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    ll_notify.setVisibility(View.VISIBLE);
                    hideall();
                    return true;
            }
            return false;
        }
    };

    private void hideall() {
        ll_home.setVisibility(View.GONE);
        ll_dash.setVisibility(View.GONE);
        ll_notify.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simain);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ll_home = findViewById(R.id.ll_home);
        ll_dash = findViewById(R.id.ll_dash);
        ll_notify = findViewById(R.id.ll_notify);
        initButton();
    }

    private void initButton() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
