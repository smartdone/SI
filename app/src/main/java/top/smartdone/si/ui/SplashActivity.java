package top.smartdone.si.ui;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.smartdone.si.R;
import top.smartdone.si.core.Config;
import top.smartdone.si.core.Global;
import top.smartdone.si.util.CryptUtil;
import top.smartdone.si.util.SharedPreferencesUtils;


public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    private static final String TAG = "SplashActivity";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                finish();
                if (intent != null) {
                    startActivity(intent);
                }
            } else if (msg.what == 1) {
                try {
                    String key = new String(CryptUtil.decryptWithPublicKey(
                            Base64.decode((String) msg.obj, Base64.NO_WRAP),
                            CryptUtil.getPublicKey()));
                    Global.password = key;
                    SharedPreferencesUtils.setParam(SplashActivity.this, Config.USERKEY, key);
                    finish();
                    startActivity(new Intent(SplashActivity.this, SIMainActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (msg.what == 2) {
                Log.d(TAG, msg.obj.toString());
                finish();
                startActivity(new Intent(SplashActivity.this, PswActivity.class));
            }
        }
    };

    private Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                sleep(2000);
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Thread getThread = new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                RequestBody requestBodyPost = new FormBody.Builder()
                        .add("token", Global.token)
                        .build();
                Request requestPost = new Request.Builder()
                        .url(Config.HOST + Config.GET)
                        .post(requestBodyPost)
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(requestPost);
                Response response = call.execute();
                String rspstr = response.body().string();
                Log.d(TAG, rspstr);
                JSONObject jsonObject = new JSONObject(rspstr);
                if(jsonObject.getBoolean("success")) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = jsonObject.getString("content");
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = jsonObject.getString("message");
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hide();

        String token = (String) SharedPreferencesUtils.getParam(this, Config.TOKEN, "");
        if (token.equals("")) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            thread.start();
        } else {
            Global.token = token;
            String key = (String) SharedPreferencesUtils.getParam(this, Config.USERKEY, "");
            if(key.equals("")) {
                getThread.start();
            } else {
                Global.password = key;
                intent = new Intent(SplashActivity.this, SIMainActivity.class);
                thread.start();
            }
        }

    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
