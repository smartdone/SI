package top.smartdone.si;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import top.smartdone.si.ui.ImgChoseActivity;
import top.smartdone.si.ui.LoginActivity;
import top.smartdone.si.ui.SIMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }

}
