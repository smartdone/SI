package top.smartdone.si.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

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

public class PswActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int USERKEY = 0;
    private static final String TAG = "PswActivity";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == USERKEY) {
                Global.password = (String) msg.obj;
                SharedPreferencesUtils.setParam(PswActivity.this, Config.USERKEY, msg.obj.toString());
            }
        }
    };

    private ShowHidePasswordEditText psw = null;
    private Button okbtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw);
        String userkey = (String) SharedPreferencesUtils.getParam(this, Config.USERKEY, "");
        if(userkey.equals("")) {
            psw = findViewById(R.id.psw_et_psw);
            okbtn = findViewById(R.id.psw_btn_ok);
            okbtn.setOnClickListener(this);
        } else {
            Intent intent = new Intent(this, SIMainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.psw_btn_ok) {
            String passWord = psw.getText().toString();
            if(passWord == null) {
                Toast.makeText(this, "密码为空，请重新输入", Toast.LENGTH_LONG).show();
            } else if (passWord.equals("")){
                Toast.makeText(this, "密码为空，请重新输入", Toast.LENGTH_LONG).show();
            } else if (passWord.length() < 16){
                Toast.makeText(this, "密码不足16位，自动填充0", Toast.LENGTH_LONG).show();
                passWord = dealPsw(passWord);
                psw.setText(passWord);
                new PushPsw(passWord).start();
            } else if (passWord.length() >= 16) {
                passWord = dealPsw(passWord);
                psw.setText(passWord);
                Toast.makeText(this, "密码长度大于16的部分将被自动截取", Toast.LENGTH_LONG).show();
                new PushPsw(passWord).start();
            }
        }
    }

    private String dealPsw(String password) {
        if(password.length() < 16) {
            while (password.length() < 16) {
                password = password + "0";
            }
        } else {
            password = password.substring(0, 16);
        }
        return password;
    }

    class PushPsw extends Thread{
        private String password;
        public PushPsw(String password) {
            this.password = password;
        }

        @Override
        public void run() {
            try {
                RequestBody requestBodyPost = new FormBody.Builder()
                        .add("token", Global.token)
                        .add("key", CryptUtil.encryptWithPublicKey(this.password.getBytes("utf-8"), CryptUtil.getPublicKey()))
                        .build();
                Request requestPost = new Request.Builder()
                        .url(Config.HOST + Config.PUSH)
                        .post(requestBodyPost)
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(requestPost);
                Response response = call.execute();
                String rspstr = response.body().string();
                Log.d(TAG, rspstr);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
