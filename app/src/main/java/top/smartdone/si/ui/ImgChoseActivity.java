package top.smartdone.si.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.scrat.app.selectorlibrary.ImageSelector;

import java.util.List;

import top.smartdone.si.R;

public class ImgChoseActivity extends AppCompatActivity {

    private static final int CHOSE_IMG = 1;
    private static final String TAG = "IMGCHOSE";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_chose);
        button = findViewById(R.id.chose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.show(ImgChoseActivity.this, CHOSE_IMG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOSE_IMG) {
            List<String> yourSelectImgPaths = ImageSelector.getImagePaths(data);
            Log.d(TAG, "paths: " + yourSelectImgPaths);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
